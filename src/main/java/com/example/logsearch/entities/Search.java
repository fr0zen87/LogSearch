package com.example.logsearch.entities;

import com.example.logsearch.utils.ConfigProperties;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Search {

    private boolean isFileFound;
    private ResultLogs resultLogs;
    private SearchInfoResult searchInfoResult;

    private final ConfigProperties configProperties;

    @Autowired
    public Search(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public SearchInfoResult logSearch(SearchInfo searchInfo) {

        List<SignificantDateInterval> dateInterval = searchInfo.getDateIntervals();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        searchInfoResult = new SearchInfoResult();

        Path logFilePath;
        Path defaultPath = Paths.get(System.getProperty("user.dir")).getParent().getParent();
        if (searchInfo.getLocation() != null) {
            logFilePath = Paths.get(defaultPath.toString(), searchInfo.getLocation());
        } else {
            logFilePath = defaultPath;
        }

        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.log*");
        Pattern accessLogPattern = Pattern.compile("access.log*");

        FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
                Path name = file.getFileName();

                if (accessLogPattern.matcher(name.toString()).find()) {
                    return FileVisitResult.CONTINUE;
                }

                if (matcher.matches(name) && checkFileCreationTime(attr, dateInterval)) {
                    try {
                        StringBuilder sb = new StringBuilder();
                        List<String> fileLines = new ArrayList<>();
                        for (String line : Files.readAllLines(file, StandardCharsets.ISO_8859_1)) {
                            if (!line.trim().endsWith(">")) {
                                sb = sb.length() == 0 ? sb.append(line) : sb.append(", ").append(line);
                            } else {
                                sb.append(line.trim());
                                fileLines.add(sb.toString());
                                sb = new StringBuilder();
                            }
                        }
                        List<ResultLogs> resultLogsArray = fileLines.stream()
                                .parallel()
                                .filter(Pattern.compile(searchInfo.getRegularExpression()).asPredicate())
                                .map(line -> {
                                    LocalDateTime parsedDate = LocalDateTime.parse(line.substring(5, 24), formatter);
                                    resultLogs = new ResultLogs();

                                    for (SignificantDateInterval interval : dateInterval) {
                                        if ((parsedDate.isAfter(interval.getDateFrom())
                                                || parsedDate.isEqual(interval.getDateFrom()))
                                                && parsedDate.isBefore(interval.getDateTo())
                                                ) {
                                            String content = line.substring(34);

                                            resultLogs.setTimeMoment(parsedDate);
                                            resultLogs.setContent(content);
                                            resultLogs.setFileName(file.toString());
                                        }
                                    }
                                    return resultLogs;
                                })
                                .filter(log -> log.getFileName() != null)
                                .collect(Collectors.toList());
                        if (!resultLogsArray.isEmpty()) {
                            searchInfoResult.getResultLogs().addAll(resultLogsArray);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(logFilePath, matcherVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (searchInfoResult.getResultLogs().isEmpty()) {
            searchInfoResult.setEmptyResultMessage("No logs found");
        } else {
            searchInfoResult.getResultLogs().sort(Comparator.comparing(ResultLogs::getTimeMoment));
        }
        return searchInfoResult;
    }

    public boolean fileSearch(SearchInfo searchInfo) {
        isFileFound = false;
        Path path = Paths.get(configProperties.getPath());
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                    try {
                        UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
                        if (view == null || !view.list().contains("info")) {
                            return FileVisitResult.CONTINUE;
                        }
                        byte[] bytes = (byte[]) Files.getAttribute(file, "user:info");
                        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                        SearchInfo fileSearchInfo = (SearchInfo) objectInputStream.readObject();

                        if (searchInfo.getRegularExpression().equals(fileSearchInfo.getRegularExpression()) &&
                                searchInfo.getFileExtension().value().equals(fileSearchInfo.getFileExtension().value()) &&
                                searchInfo.getLocation().equals(fileSearchInfo.getLocation()) &&
                                compareDates(searchInfo.getDateIntervals(), fileSearchInfo.getDateIntervals())) {
                            isFileFound = true;
                            configProperties.setFileLink(file.toString());
                            return FileVisitResult.TERMINATE;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isFileFound;
    }

    public void fileGenerate(SearchInfo searchInfo) {

        logSearch(searchInfo);

        Logs logs = new Logs();
        logs.setCreator("Имя, Фамилия создателя, OOO «Siblion»");
        logs.setSearchInfo(searchInfo);
        logs.setSearchInfoResult(searchInfoResult);
        logs.setApplication("Created by LogSearch app");
        try {
            File resultFile = generateUniqueFile(searchInfo.getFileExtension());

            if (searchInfo.getFileExtension().value().equals("PDF")) {
                createFile(logs, resultFile);
                configProperties.setFileLink(resultFile.toString());
                return;
            }

            JAXBContext context = JAXBContext.newInstance(Logs.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            Result streamResult = new StreamResult(resultFile);

            if (searchInfo.getFileExtension().value().equals("XML")) {
                marshaller.marshal(logs, streamResult);
                configProperties.setFileLink(resultFile.toString());
                return;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(logs, outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            Source xml = new StreamSource(inputStream);
            Source xslt = null;

            switch (searchInfo.getFileExtension()) {
                case DOC: {
                    xslt = new StreamSource(Paths.get("src/main/webapp/WEB-INF/xsl/doc.xslt").toFile());
                    break;
                }
                case LOG: {
                    xslt = new StreamSource(Paths.get("src/main/webapp/WEB-INF/xsl/log.xslt").toFile());
                    break;
                }
                case PDF: {
                    xslt = new StreamSource(Paths.get("src/main/webapp/WEB-INF/xsl/pdf.xslt").toFile());
                    break;
                }
                case RTF: {
                    xslt = new StreamSource(Paths.get("src/main/webapp/WEB-INF/xsl/rtf.xslt").toFile());
                    break;
                }
                case HTML: {
                    xslt = new StreamSource(Paths.get("src/main/webapp/WEB-INF/xsl/html.xslt").toFile());
                    break;
                }
                case XML: {
                    //do nothing
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.transform(xml, streamResult);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(searchInfo);
            Files.setAttribute(resultFile.toPath(), "user:info", byteArrayOutputStream.toByteArray());

            configProperties.setFileLink(resultFile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkFileCreationTime(BasicFileAttributes attr, List<SignificantDateInterval> dateInterval) {

        LocalDateTime fileCreationTime = LocalDateTime
                .ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
        LocalDateTime fileModificationTime = LocalDateTime
                .ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());

        return dateInterval.stream().anyMatch(interval -> fileCreationTime.isBefore(interval.getDateTo())
                && fileModificationTime.isAfter(interval.getDateFrom()));
    }

    private boolean compareDates(List<SignificantDateInterval> currentIntervals,
                                 List<SignificantDateInterval> fileIntervals) {
        int count = 0;
        for (SignificantDateInterval currentInterval : currentIntervals) {
            boolean check = false;
            for (SignificantDateInterval fileInterval : fileIntervals) {
                if (check) break;
                if (fileInterval.getDateFrom().isAfter(currentInterval.getDateFrom()) ||
                        fileInterval.getDateTo().isBefore(currentInterval.getDateTo())) {
                    check = false;
                    continue;
                }

                long currentIntervalDifference = Duration.between(currentInterval.getDateTo(), currentInterval.getDateFrom()).toMillis();
                long fileIntervalDifference = Duration.between(fileInterval.getDateTo(), fileInterval.getDateFrom()).toMillis();

                if (fileIntervalDifference / currentIntervalDifference > 1.1) {
                    check = false;
                    continue;
                }
                check = true;
            }
            if (check) {
                count++;
            }
        }
        return count == currentIntervals.size();
    }

    private File generateUniqueFile(FileExtension fileExtension) {
        String extension = "." + fileExtension.value().toLowerCase();
        File dir = Paths.get(configProperties.getPath()).toFile();

        String uniqueName = "result_log";
        File file = new File(dir, uniqueName + extension);

        int num = 0;
        while (file.exists()) {
            num++;
            file = new File(dir, uniqueName + "_" + num + extension);
        }

        return file;
    }

    private void createFile(Logs logs, File file) {
        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            Paragraph paragraph;
            Chapter chapter;
            Section section;
            PdfPTable table;

            paragraph = new Paragraph("LogSearch Result", FontFactory.getFont(FontFactory.HELVETICA,
                    20, Font.BOLDITALIC, new CMYKColor(0, 255, 255, 17)));

            chapter = new Chapter(paragraph, 1);
            chapter.setNumberDepth(0);

            paragraph = new Paragraph("Created by: " + logs.getCreator(),
                    FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            chapter.add(paragraph);

            paragraph = new Paragraph("Search info",
                    FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,
                            new CMYKColor(0, 255, 255, 17)));
            section = chapter.addSection(paragraph);

            paragraph = new Paragraph("Regular expression: " + logs.getSearchInfo().getRegularExpression());
            section.add(paragraph);

            paragraph = new Paragraph("Location: " + logs.getSearchInfo().getLocation());
            section.add(paragraph);

            paragraph = new Paragraph("Date intervals:");
            section.add(paragraph);

            table = new PdfPTable(2);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            table.addCell("Date from");
            table.addCell("Date to");

            List<SignificantDateInterval> intervals = logs.getSearchInfo().getDateIntervals();
            for (int i = 0; i < intervals.size(); i++) {
                table.addCell(logs.getSearchInfo().getDateIntervals().get(i).getDateFrom().toString());
                table.addCell(logs.getSearchInfo().getDateIntervals().get(i).getDateTo().toString());
            }

            section.add(table);

            paragraph = new Paragraph("Search result", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD,
                    new CMYKColor(0, 255, 255, 17)));
            section = chapter.addSection(paragraph);

            Font smallFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            for (ResultLogs resultLogs : searchInfoResult.getResultLogs()) {
                section.add(new Paragraph("File: " + resultLogs.getFileName(), smallFont));
                section.add(new Paragraph("Time moment: " + resultLogs.getTimeMoment(), smallFont));
                section.add(new Paragraph("Content: " + resultLogs.getContent(), smallFont));
                section.add(new Paragraph(" "));
            }

            document.add(chapter);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
