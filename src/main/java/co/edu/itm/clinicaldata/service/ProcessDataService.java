package co.edu.itm.clinicaldata.service;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.enums.Language;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.model.User;
import co.edu.itm.clinicaldata.util.FileUtilities;
import co.edu.itm.clinicaldata.util.Validations;

@Service
public class ProcessDataService {

    private static final Logger LOGGER = Logger
            .getLogger(ProcessDataService.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    ProcessingRequestService processingRequestService;

    private final List<String> languagesAllowed = Arrays.asList(
            Language.JAVA.toString(), Language.PYTHON.toString(),
            Language.R.toString());

    public String processState(String processIdentifier) throws ValidateException {
        validateProcessIdentifier(processIdentifier);
        ProcessingRequest processingRequest = findProccessByIdentifier(processIdentifier);
        return String
                .format("La solicitud <%s> se encuentra en estado %s",
                        processingRequest.getIdentifier(),
                        processingRequest.getState());
    }

    public String processResult(String processIdentifier) throws ValidateException {
        validateProcessIdentifier(processIdentifier);
        ProcessingRequest processingRequest = findProccessByIdentifier(processIdentifier);
        validateFinishedProcess(processingRequest);
        String fullPath = getProcessFullPath(processingRequest);
        String readedContent = FileUtilities.readFile(fullPath);
        LOGGER.info("Contenido del archivo leído " + readedContent);
        return String
                .format("La solicitud <%s> ha terminado su procesamiento, su estado actual es %s",
                        processingRequest.getIdentifier(),
                        processingRequest.getState());
    }

    private String getProcessFullPath(ProcessingRequest processingRequest) {
        return processingRequest.getBasePath() + processingRequest.getFileName();
    }

    private void validateFinishedProcess(ProcessingRequest processingRequest) throws ValidateException {
        if(processingRequest.getState().equals(ProcessState.CREATED.getState())
                || processingRequest.getState().equals(ProcessState.PROCESSING.getState())){
            throw new ValidateException(String.format("La solicitud <%s> no ha terminado su procesamiento", processingRequest.getIdentifier()));
        }
    }

    private ProcessingRequest findProccessByIdentifier(String processIdentifier) throws ValidateException {
        ProcessingRequest processingRequest = processingRequestService.findByIdentifier(processIdentifier);
        if(processingRequest == null){
            throw new ValidateException(String.format("La solicitud <%s> no existe en la base de datos", processIdentifier));
        }
        return processingRequest;
    }

    public String startProcess(Params params) throws ValidateException {
        validateFields(params);
        validateLanguagesAllowed(params);
        Language language = getLanguage(params.getLanguage());
        ProcessingRequest processingRequest = createProcessingRequest(language, params);
        String fullPath = getProcessFullPath(processingRequest);
        FileUtilities.createFile(processingRequest.getFunction(), fullPath);
        createUser(params);
        LOGGER.info("Comenzando el procesamiento de la solicitud " + processingRequest.getIdentifier());
        return "Señor "
                + params.getUserName()
                + " su solicitud ha comenzado a ser procesada, el identificador generado es: "
                + processingRequest.getIdentifier();
    }

    private ProcessingRequest createProcessingRequest(Language language, Params params){
        ProcessingRequest processingRequest = new ProcessingRequest();
        String identifier = FileUtilities.randomIdentifier();
        processingRequest.setIdentifier(identifier);
        processingRequest.setFileName(FileUtilities.generateFileName(identifier, language.getFileExtension()));
        processingRequest.setBasePath(FileUtilities.buildBasePath(language.getName()));
        processingRequest.setLanguage(language.getName());
        processingRequest.setFunction(params.getFunction());
        processingRequest.setState(ProcessState.CREATED.getState());
        processingRequestService.save(processingRequest);
        return processingRequest;
    }

    private Language getLanguage(String languageToProcess) {
        Language language = null;
        if (languageToProcess.equalsIgnoreCase(Language.JAVA.getName())) {
            language = Language.JAVA;
        } else if (languageToProcess.equalsIgnoreCase(Language.PYTHON.getName())) {
            language = Language.PYTHON;
        } else if (languageToProcess.equalsIgnoreCase(Language.R.getName())) {
            language = Language.R;
        }
        LOGGER.info("El lenguaje a procesar es " + language);
        return language;
    }

    private void validateLanguagesAllowed(Params params)
            throws ValidateException {
        boolean validLanguage = languagesAllowed.stream().anyMatch(
                language -> language.equalsIgnoreCase(params.getLanguage()));
        if (!validLanguage) {
            throw new ValidateException(
                    "La información diligenciada en el campo <language> no es soportada por la aplicación");
        }
    }

    private void validateFields(Params params) throws ValidateException {
        if (Validations.field(params.getLanguage())) {
            throw new ValidateException(
                    "El campo <language> debe ser diligenciado");
        }
        if (Validations.field(params.getFunction())) {
            throw new ValidateException(
                    "El campo <function> debe ser diligenciado");
        }
    }

    private void validateProcessIdentifier(String processId) throws ValidateException {
        if (Validations.field(processId)) {
            throw new ValidateException(
                    "El <identificador> del proceso debe ser válido");
        }
    }

    private void createUser(Params params) {
        User user = new User();
        user.setName(params.getUserName());
        user.setSalary(1000);
        user.setAge(25);
        userService.saveUser(user);
    }
}
