package co.edu.itm.clinicaldata.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import co.edu.itm.clinicaldata.model.Languages;

public class FileUtilities {

    private static final String FOLDER_NAME = "clinicaldata";
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final Logger LOGGER = Logger.getLogger(FileUtilities.class.getName());

    public static void createFile(String functionToProcess, String fileName, Languages language) {
        try {
            Files.write(Paths.get(buildBasePath(language) + fileName), functionToProcess.getBytes());
        } catch (IOException ex) {
            LOGGER.error("Ocurrió un error generando el archivo con la función", ex);
        }
    }
    
    public static String buildBasePath(Languages language){
        String basePath = USER_HOME + FILE_SEPARATOR + FOLDER_NAME + FILE_SEPARATOR + language.toString() + FILE_SEPARATOR;
        createBasePath(basePath);
        return basePath;
    }
    
    public static void createBasePath(String basePath){
        new File(basePath).mkdirs();
    }

    public static void main(String[] args) {
        // Aquí las instrucciones del método
        System.getProperties().list(System.out);
        createFile("Esta es la función que quiero ejecutar y ensayar", "Java_123456.java", Languages.JAVA);
        createFile("Esta es la función que quiero ejecutar y ensayar", "Python_123456.py", Languages.PYTHON);
        createFile("Esta es la función que quiero ejecutar y ensayar", "Python_123456.r", Languages.R);
    }
}
