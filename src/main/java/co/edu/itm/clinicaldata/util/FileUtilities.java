package co.edu.itm.clinicaldata.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import co.edu.itm.clinicaldata.model.Language;

public class FileUtilities {

    private static final String FOLDER_NAME = "clinicaldata";
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final Logger LOGGER = Logger.getLogger(FileUtilities.class.getName());

    public static void createFile(String functionToProcess, String path) {
        try {
            Files.write(Paths.get(path), functionToProcess.getBytes());
        } catch (IOException ex) {
            LOGGER.error("Ocurrió un error generando el archivo con la función", ex);
        }
    }

    public static String buildBasePath(String languageFolder){
        String basePath = USER_HOME + FILE_SEPARATOR + FOLDER_NAME + FILE_SEPARATOR + languageFolder + FILE_SEPARATOR;
        createBasePath(basePath);
        return basePath;
    }

    public static void createBasePath(String basePath){
        new File(basePath).mkdirs();
    }

    public static String randomIdentifier(){
        return UUID.randomUUID().toString();
    }

    public static String generateFileName(String randomIdentifier, String ext){
        return String.format("%s.%s", randomIdentifier, ext);
    }

    public static void main(String[] args) {
        // Aquí las instrucciones del método
        System.getProperties().list(System.out);
        createFile("Esta es la función que quiero ejecutar y ensayar", buildBasePath(Language.JAVA.getName()) + generateFileName(randomIdentifier(), "java"));
        createFile("Esta es la función que quiero ejecutar y ensayar", buildBasePath(Language.PYTHON.getName()) + generateFileName(randomIdentifier(), "py"));
        createFile("Esta es la función que quiero ejecutar y ensayar", buildBasePath(Language.R.getName()) + generateFileName(randomIdentifier(), "r"));
        System.out.println(String.format("%s.%s", RandomStringUtils.randomAlphanumeric(16), "java"));
        System.out.println(generateFileName(randomIdentifier(), "java"));
    }

}
