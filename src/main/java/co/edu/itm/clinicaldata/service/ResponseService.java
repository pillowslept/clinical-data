package co.edu.itm.clinicaldata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.configuration.FolderConf;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.util.Validations;

@Service
@Transactional
public class ResponseService {

    private static final String ERROR_OBTAINING_RESPONSE_FILE = "No se encontró archivo de respuesta para la solicitud con identificador <%s>";
    private static final String ERROR_OBTAINING_REQUEST_FILE = "No se encontró archivo de petición para la solicitud con identificador <%s>";

    @Autowired
    ProcessingRequestService processingRequestService;

    @Autowired
    FileUtilities fileUtilities;

    @Autowired
    FolderConf folderConf;

    public FileSystemResource obtainResponseFile(String processIdentifier) throws ValidateException {
        ProcessingRequest processingRequest = processingRequestService.validateFinishedProcess(processIdentifier);

        String outputPathFile = processingRequest.getBasePath() + folderConf.getLogOutputFile();
        String filePath = validateExistenceFile(outputPathFile);
        if(Validations.field(filePath)) {
            String errorPathFile = processingRequest.getBasePath() + folderConf.getErrOutputFile();
            filePath = validateExistenceFile(errorPathFile);
            if(Validations.field(filePath)) {
                throw new ValidateException(String.format(ERROR_OBTAINING_RESPONSE_FILE, processIdentifier));
            }
        }

        return new FileSystemResource(filePath);
    }

    public FileSystemResource obtainRequestFile(String processIdentifier) throws ValidateException {
        ProcessingRequest processingRequest = processingRequestService.validateFinishedProcess(processIdentifier);

        String filePath = validateExistenceFile(processingRequest.getBasePath() + processingRequest.getFileName());
        if (Validations.field(filePath)) {
            throw new ValidateException(String.format(ERROR_OBTAINING_REQUEST_FILE, processIdentifier));
        }

        return new FileSystemResource(filePath);
    }

    private String validateExistenceFile(String filePath) {
        return fileUtilities.existsFile(filePath) ? filePath : null;
    }

}
