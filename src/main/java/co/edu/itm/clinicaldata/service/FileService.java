package co.edu.itm.clinicaldata.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.component.RandomUtilities;
import co.edu.itm.clinicaldata.enums.Language;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;

@Service
public class FileService {

    private static final String UPLOAD_SUCCESSFUL = "El archivo ha sido almacenado con éxito, identificador generado para la solicitud: <%s>.";
    private static final String INVALID_FILE_EXTENSION = "La extensión del archivo a procesar no es soportada por la aplicación. Extensiones permitidas: %s";

    @Autowired
    ProcessingRequestService processingRequestService;

    @Autowired
    InvestigatorService investigatorService;

    @Autowired
    FileUtilities fileUtilities;

    @Autowired
    RandomUtilities randomUtilities;

    private final List<String> languagesAllowed = Arrays.asList(
            Language.JAVA.getFileExtension(), Language.PYTHON.getFileExtension(),
            Language.R.getFileExtension());

    /**
     * Se encarga de crear una solicitud con el archivo a procesar y le asigna un identificador único
     * @param file
     * @param resources 
     * @return
     * @throws ValidateException
     */
    public String upload(MultipartFile file, Long investigatorId) throws ValidateException {
        String fileName = file.getOriginalFilename(); 
        Language language = getLanguage(fileName);
        Investigator investigator = investigatorService.validateAndFind(investigatorId);
        byte[] bytes = fileUtilities.getBytesFromFile(file);

        String identifier = randomUtilities.generateIdentifier();
        String basePath = fileUtilities.buildBasePath(language.getName(), identifier);
        fileUtilities.createFile(bytes, buildPath(basePath, file.getOriginalFilename()));

        ProcessingRequest processingRequest = processingRequestService
                .create(identifier, language.getName(), bytes,
                        fileName, basePath, investigator);

        return String.format(UPLOAD_SUCCESSFUL, processingRequest.getIdentifier());
    }

    private String buildPath(String basePath, String fileName){
        return basePath + fileName;
    }

    private Language getLanguage(String fileOriginalName) throws ValidateException {
        String fileExtension = FilenameUtils.getExtension(fileOriginalName);
        Language language = null;
        if (fileExtension.equalsIgnoreCase(Language.JAVA.getFileExtension())) {
            language = Language.JAVA;
        } else if (fileExtension.equalsIgnoreCase(Language.PYTHON
                .getFileExtension())) {
            language = Language.PYTHON;
        } else if (fileExtension
                .equalsIgnoreCase(Language.R.getFileExtension())) {
            language = Language.R;
        } else {
            throw new ValidateException(String.format(INVALID_FILE_EXTENSION,
                    languagesAllowed.toString()));
        }
        return language;
    }

}
