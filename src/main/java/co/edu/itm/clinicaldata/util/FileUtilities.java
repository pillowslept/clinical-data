package co.edu.itm.clinicaldata.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

public class FileUtilities {

    private static final String FOLDER_NAME = "clinicaldata";
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final Logger LOGGER = Logger.getLogger(FileUtilities.class.getName());

    public static void createFile(byte[] functionToProcess, String path) {
        try {
            Files.write(Paths.get(path), functionToProcess);
        } catch (IOException ex) {
            LOGGER.error("Ocurrió un error generando el archivo con la función a procesar", ex);
        }
    }

    public static String buildBasePath(String languageFolder, String identifier){
        String basePath = USER_HOME + FILE_SEPARATOR + FOLDER_NAME + FILE_SEPARATOR + languageFolder + FILE_SEPARATOR + identifier + FILE_SEPARATOR;
        createBasePath(basePath);
        return basePath;
    }

    public static void createBasePath(String basePath){
        new File(basePath).mkdirs();
    }

    public static String readFile(String fileName){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
            }
        } catch (IOException ex) {
            LOGGER.error(String.format("El archivo de la ruta <%s> no pudo ser leído. ", fileName), ex);
        }
        return sb.toString();
    }

}
