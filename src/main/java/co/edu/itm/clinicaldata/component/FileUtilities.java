package co.edu.itm.clinicaldata.component;

import static co.edu.itm.clinicaldata.util.Constants.FILE_SEPARATOR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import co.edu.itm.clinicaldata.exception.ValidateException;

@Component
public class FileUtilities {

    private static final String ERROR_OBTAINING_BYTES_FROM_FILE = "Ocurrió un error obteniendo el archivo a procesar";
    private static final String ERROR_READING_FILE = "El archivo de la ruta <%s> no pudo ser leído. ";
    private static final String ERROR_CREATING_FILE = "Ocurrió un error creando el archivo con la función a procesar";
    private static final String RESOURCES_FOLDER = "resources";
    private static final String TEMPLATE_FOLDER = "resources";
    private static final String FOLDER_NAME = "clinicaldata";
    private static final String USER_HOME = System.getProperty("user.home");
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final Logger LOGGER = Logger.getLogger(FileUtilities.class.getName());

    public void createFile(byte[] functionToProcess, String path) throws ValidateException {
        try {
            Files.write(Paths.get(path), functionToProcess);
        } catch (IOException ex) {
            LOGGER.error(ERROR_CREATING_FILE, ex);
            throw new ValidateException(ERROR_CREATING_FILE);
        }
    }

    public String buildBasePath(String languageFolder, String identifier){
        String basePath = baseLanguageFolder(languageFolder) + identifier + FILE_SEPARATOR;
        createBasePath(basePath);
        return basePath;
    }

    private String baseLanguageFolder(String languageFolder){
        return USER_HOME + FILE_SEPARATOR + FOLDER_NAME + FILE_SEPARATOR + languageFolder + FILE_SEPARATOR;
    }

    public String resourceLanguageFolder(String languageFolder){
        return baseLanguageFolder(languageFolder) + RESOURCES_FOLDER + FILE_SEPARATOR;
    }

    public String templateLanguageFolder(String languageFolder){
        return baseLanguageFolder(languageFolder) + TEMPLATE_FOLDER + FILE_SEPARATOR;
    }

    public void createBasePath(String basePath){
        new File(basePath).mkdirs();
    }

    public String readFile(String fileName){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                sb.append(sCurrentLine);
                sb.append(LINE_SEPARATOR);
            }
        } catch (IOException ex) {
            LOGGER.error(String.format(ERROR_READING_FILE, fileName), ex);
        }
        return sb.toString();
    }

    public String createFileName(String fileName, String extension){
        return String.format("%s.%s", fileName, extension);
    }

    public boolean existsFile(String filePath){
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }

    public boolean isLinux(){
        boolean isLinux = false;
        if(SystemUtils.IS_OS_LINUX){
            isLinux = true;
        }
        return isLinux;
    }

    public byte[] getBytesFromFile(MultipartFile file) throws ValidateException {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new ValidateException(ERROR_OBTAINING_BYTES_FROM_FILE);
        }
        return bytes;
    }

}
