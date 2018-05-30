package com.example.logsearch.utils;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.Logs;
import com.example.logsearch.entities.SearchInfo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileGenerate {

    private final Logger logger = LoggerFactory.getLogger(FileGenerate.class);

    private final ConfigProperties configProperties;
    private final LogsSearch search;

    @Autowired
    public FileGenerate(ConfigProperties configProperties, LogsSearch search) {
        this.configProperties = configProperties;
        this.search = search;
    }

    public void fileGenerate(SearchInfo searchInfo) {

        logger.info("Starting " + searchInfo.getFileExtension().value() + " file generation");
        long start = System.currentTimeMillis();

        try {
            Logs logs = new Logs();
            logs.setCreator("Имя, Фамилия создателя, OOO «Siblion»");
            logs.setSearchInfo(searchInfo);
            logs.setSearchInfoResult(search.logSearch(searchInfo));
            logs.setApplication("Created by LogSearch app");

            File resultFile = generateUniqueFile(searchInfo.getFileExtension());
            Files.createFile(resultFile.toPath());

            JAXBContext context = JAXBContext.newInstance(Logs.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String fileExtension = searchInfo.getFileExtension().value();
            if (fileExtension.equals("PDF") || fileExtension.equals("RTF") || fileExtension.equals("DOC")) {
                Path tempFile = Files.createTempFile(Paths.get(configProperties.getPath()), "temp", ".xml");
                marshaller.marshal(logs, tempFile.toFile());
                createReport(tempFile, fileExtension, resultFile);
                Files.delete(tempFile);
                configProperties.setFileLink(resultFile.toString());
                writeAttribute(searchInfo, resultFile);
                logger.info("File generated in " + (System.currentTimeMillis() - start) + " ms");
                return;
            }

            Result streamResult = new StreamResult(resultFile.toString());

            if (fileExtension.equals("XML")) {
                marshaller.marshal(logs, streamResult);
                configProperties.setFileLink(resultFile.toString());
                writeAttribute(searchInfo, resultFile);
                logger.info("File generated in " + (System.currentTimeMillis() - start) + " ms");
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

            logger.info("File generated in " + (System.currentTimeMillis() - start) + " ms");
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
}
