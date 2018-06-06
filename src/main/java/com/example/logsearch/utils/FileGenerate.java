package com.example.logsearch.utils;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.Logs;
import com.example.logsearch.entities.SearchInfo;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class FileGenerate {

    private final Logger logger = LoggerFactory.getLogger(FileGenerate.class);

    private final ConfigProperties configProperties;
    private final LogsSearch search;
    private final ResourceLoader resourceLoader;

    @Autowired
    public FileGenerate(ConfigProperties configProperties, LogsSearch search, ResourceLoader resourceLoader) {
        this.configProperties = configProperties;
        this.search = search;
        this.resourceLoader = resourceLoader;
    }

    @Async
    public void fileGenerate(SearchInfo searchInfo, File file) {

        logger.info("Starting " + searchInfo.getFileExtension().value() + " file generation");
        long start = System.currentTimeMillis();

        try {
            Logs logs = new Logs("Имя, Фамилия создателя, OOO «Siblion»",
                    searchInfo,
                    search.logSearch(searchInfo),
                    "Created by LogSearch app");

            Files.createFile(file.toPath());

            JAXBContext context = JAXBContext.newInstance(Logs.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String fileExtension = searchInfo.getFileExtension().value();
            Result streamResult;

            if (fileExtension.equals("XML")) {
                streamResult = new StreamResult(file.toString());
                marshaller.marshal(logs, streamResult);
                writeAttribute(searchInfo, file);
                logger.info("File generated in " + (System.currentTimeMillis() - start) + " ms");
                return;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(logs, outputStream);
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            Source streamSource = new StreamSource(inputStream);
            Source xslt = null;

            inputStream.close();
            outputStream.close();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Resource resource;

            switch (fileExtension) {
                case "DOC": {
                    resource = resourceLoader.getResource("classpath:xsl/doc.xslt");
                    xslt = new StreamSource(resource.getFile());
                    break;
                }
                case "LOG": {
                    resource = resourceLoader.getResource("classpath:xsl/log.xslt");
                    xslt = new StreamSource(resource.getFile());
                    break;
                }
                case "HTML": {
                    resource = resourceLoader.getResource("classpath:xsl/html.xslt");
                    xslt = new StreamSource(resource.getFile());
                    break;
                }
                case "RTF": {
                    //same as pdf
                }
                case "PDF": {
                    saveFile(file, fileExtension, streamSource, transformerFactory);
                    writeAttribute(searchInfo, file);
                    logger.info("File generated in " + (System.currentTimeMillis() - start) + " ms");
                    return;
                }
            }

            streamResult = new StreamResult(file.toString());
            transformerFactory.newTransformer(xslt).transform(streamSource, streamResult);
            writeAttribute(searchInfo, file);

            logger.info("File generated in " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception raised: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void saveFile(File file, String fileExtension, Source streamSource, TransformerFactory transformerFactory) {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            Resource resource = resourceLoader.getResource("classpath:xsl/pdf.xslt");
            Source xslt = new StreamSource(resource.getFile());

            resource = resourceLoader.getResource("classpath:fop.xml");
            FopFactory fopFactory = FopFactory.newInstance(resource.getFile());
            Fop fop = fileExtension.equals("RTF") ? fopFactory.newFop(MimeConstants.MIME_RTF, out) :
                    fopFactory.newFop(MimeConstants.MIME_PDF, out);
            Result streamResult = new SAXResult(fop.getDefaultHandler());

            transformerFactory.newTransformer(xslt).transform(streamSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception raised: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void writeAttribute(SearchInfo searchInfo, File resultFile) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(searchInfo);
            Files.setAttribute(resultFile.toPath(), "user:info", byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Exception raised: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public File generateUniqueFile(FileExtension fileExtension) {
        String extension = "." + fileExtension.value().toLowerCase();
        File dir = Paths.get(configProperties.getPath()).toFile();

        String uniqueName = "result_log";
        File file = new File(dir, uniqueName + extension);

        int num = 0;
        while (file.exists()) {
            num++;
            file = new File(dir, uniqueName + "_" + num + extension);
        }
        configProperties.setFileLink(file.toString());

        return file;
    }
}
