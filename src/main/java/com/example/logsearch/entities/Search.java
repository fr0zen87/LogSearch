package com.example.logsearch.entities;

import com.example.logsearch.utils.ConfigProperties;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
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

        try {
            logSearch(searchInfo);

            Logs logs = new Logs();
            logs.setCreator("Имя, Фамилия создателя, OOO «Siblion»");
            logs.setSearchInfo(searchInfo);
            logs.setSearchInfoResult(searchInfoResult);
            logs.setApplication("Created by LogSearch app");

            File resultFile = generateUniqueFile(searchInfo.getFileExtension());
            Files.createFile(resultFile.toPath());

            JAXBContext context = JAXBContext.newInstance(Logs.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String fileExtension = searchInfo.getFileExtension().value();
            if (fileExtension.equals("PDF") || fileExtension.equals("RTF") || fileExtension.equals("DOC")) {
                Path tempFile = Files.createTempFile(Paths.get("C:\\Found logs"), "temp", ".xml");
                marshaller.marshal(logs, tempFile.toFile());
                createReport(tempFile, fileExtension, resultFile);
                Files.delete(tempFile);
                configProperties.setFileLink(resultFile.toString());
                writeAttribute(searchInfo, resultFile);
                return;
            }

            Result streamResult = new StreamResult(resultFile.toString());

            if (fileExtension.equals("XML")) {
                marshaller.marshal(logs, streamResult);
                configProperties.setFileLink(resultFile.toString());
                writeAttribute(searchInfo, resultFile);
                return;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(logs, outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            Source xml = new StreamSource(inputStream);
            Source xslt = null;
            if (fileExtension.equals("LOG")) {
                xslt = new StreamSource(Paths.get("src/main/webapp/WEB-INF/xsl/log.xslt").toFile());
            } else if (fileExtension.equals("HTML")) {
                xslt = new StreamSource(Paths.get("src/main/webapp/WEB-INF/xsl/html.xslt").toFile());
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.transform(xml, streamResult);

            configProperties.setFileLink(resultFile.toString());
            writeAttribute(searchInfo, resultFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeAttribute(SearchInfo searchInfo, File resultFile) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(searchInfo);
        Files.setAttribute(resultFile.toPath(), "user:info", byteArrayOutputStream.toByteArray());
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

    private void createReport(Path tempFile, String fileExtension, File file) {
        try {
            JRXmlDataSource dataSource = new JRXmlDataSource(tempFile.toFile());

            File styleSheet = new File("src/main/resources/jrxml/report_style.jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(styleSheet);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

            switch (fileExtension) {
                case "PDF" : {
                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.toString());
                    break;
                }
                case "RTF" : {
                    JRRtfExporter exporter = new JRRtfExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleWriterExporterOutput(file));
                    exporter.exportReport();
                    break;
                }
                case "DOC" : {
                    JRDocxExporter exporter = new JRDocxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
                    exporter.exportReport();
                    break;
                }
            }

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
