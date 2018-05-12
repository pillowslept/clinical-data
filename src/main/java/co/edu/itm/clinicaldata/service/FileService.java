package co.edu.itm.clinicaldata.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.edu.itm.clinicaldata.dto.ResourcesWrapper;
import co.edu.itm.clinicaldata.enums.Language;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.util.FileUtilities;
import co.edu.itm.clinicaldata.util.RandomUtilities;
import co.edu.itm.clinicaldata.util.Validations;

@Service
public class FileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class
            .getName());

    @Autowired
    ProcessingRequestService processingRequestService;

    @Autowired
    InvestigatorService investigatorService;

    private final List<String> languagesAllowed = Arrays.asList(
            Language.JAVA.toString(), Language.PYTHON.toString(),
            Language.R.toString());

    /**
     * Se encarga de crear una solicitud con el archivo a procesar y le asigna un identificador único
     * @param file
     * @param resources 
     * @return
     * @throws ValidateException
     */
    public String upload(MultipartFile file, Long investigatorId, ResourcesWrapper resources) throws ValidateException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        validateExtensionAllowed(fileExtension);

        Investigator investigator = investigatorService.validateAndfind(investigatorId);

        Language language = getLanguage(fileExtension);
        boolean requiredResources = validateRequiredResources(resources, language.getName());
        byte[] bytes = getBytesFromFile(file);
        String identifier = RandomUtilities.randomIdentifier();
        String fileName = file.getOriginalFilename();
        String basePath = FileUtilities.buildBasePath(language.getName(), identifier);

        FileUtilities.createFile(bytes,
                buildPath(basePath, file.getOriginalFilename()));

        ProcessingRequest processingRequest = processingRequestService
                .create(identifier, language.getName(), bytes,
                        fileName, basePath, investigator);

        return String.format("El archivo ha sido almacenado con éxito, identificador generado para la solicitud: <%s>.",
                        processingRequest.getIdentifier());
    }

    private boolean validateRequiredResources(ResourcesWrapper resources,
            String languageFolder) throws ValidateException {
        boolean requiredResources = !Validations.field(resources.getResources());
        if(requiredResources){
            String resourceLanguageFolder = FileUtilities.resourceLanguageFolder(languageFolder);
            validateResourcesExistence(resources, resourceLanguageFolder);
        }
        return requiredResources;
    }

    private void validateResourcesExistence(ResourcesWrapper resources,
            String resourceLanguageFolder) throws ValidateException {
        for(String resource : resources.getResources()){
            boolean exists = FileUtilities.existsFile(resourceLanguageFolder + resource);
            if (!exists) {
                throw new ValidateException(
                        String.format(
                                "El recurso <%s> no existe actualmente en el servidor, favor solicitar configuración al administrador",
                                resource));
            }
        }
    }

    private String buildPath(String basePath, String fileName){
        return basePath + fileName;
    }

    private byte[] getBytesFromFile(MultipartFile file)
            throws ValidateException {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new ValidateException(
                    "Ocurrió un error obteniendo el archivo a procesar");
        }
        return bytes;
    }

    private void validateExtensionAllowed(String extension)
            throws ValidateException {
        boolean validLanguage = languagesAllowed.stream().anyMatch(
                language -> language.equalsIgnoreCase(extension));
        if (!validLanguage) {
            throw new ValidateException(
                    String.format(
                            "La extensión del archivo a procesar no es soportada por la aplicación. Extensiones permitidas: %s",
                            languagesAllowed.toString()));
        }
    }

    private Language getLanguage(String languageToProcess) {
        Language language = null;
        if (languageToProcess.equalsIgnoreCase(Language.JAVA.getName())) {
            language = Language.JAVA;
        } else if (languageToProcess
                .equalsIgnoreCase(Language.PYTHON.getName())) {
            language = Language.PYTHON;
        } else if (languageToProcess.equalsIgnoreCase(Language.R.getName())) {
            language = Language.R;
        }
        LOGGER.info("El lenguaje a procesar es " + language);
        return language;
    }

}
