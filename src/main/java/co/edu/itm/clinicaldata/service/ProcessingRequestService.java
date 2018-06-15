package co.edu.itm.clinicaldata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.repository.ProcessingRequestRepository;
import co.edu.itm.clinicaldata.util.DateUtilities;
import co.edu.itm.clinicaldata.util.Validations;

@Service
@Transactional
public class ProcessingRequestService {

    private static final String IDENTIFIER_NOT_VALID = "El <identifier> del proceso debe ser válido";
    private static final String PROCESSING_REQUEST_NOT_FOUND = "La solicitud con identificador <%s> no existe en la base de datos";
    private static final String PROCESS_NOT_FINISHED_YET = "La solicitud <%s> no ha terminado su procesamiento";

    @Autowired
    ProcessingRequestRepository processingRequestRepository;

    public ProcessingRequest findByIdentifier(String identifier) {
        return processingRequestRepository.findByIdentifier(identifier);
    }

    public void save(ProcessingRequest processingRequest) {
        processingRequestRepository.save(processingRequest);
    }

    public void update(ProcessingRequest processingRequest) {
        save(processingRequest);
    }

    public List<ProcessingRequest> findByInvestigatorId(Long investigatorId) {
        return processingRequestRepository.findByInvestigatorId(investigatorId);
    }

    public ProcessingRequest validateAndFindByIdentifier(String processIdentifier) throws ValidateException {
        validateProcessIdentifier(processIdentifier);
        ProcessingRequest processingRequest = findByIdentifier(processIdentifier);
        if(processingRequest == null){
            throw new ValidateException(String.format(PROCESSING_REQUEST_NOT_FOUND, processIdentifier));
        }
        return processingRequest;
    }

    public ProcessingRequest validateFinishedProcess(String processIdentifier) throws ValidateException {
        ProcessingRequest processingRequest = validateAndFindByIdentifier(processIdentifier);
        if(processingRequest.getState().equals(ProcessState.CREATED.getState())
                || processingRequest.getState().equals(ProcessState.PROCESSING.getState())){
            throw new ValidateException(String.format(PROCESS_NOT_FINISHED_YET, processingRequest.getIdentifier()));
        }
        return processingRequest; 
    }

    private void validateProcessIdentifier(String processIdentifier) throws ValidateException {
        if (Validations.field(processIdentifier)) {
            throw new ValidateException(IDENTIFIER_NOT_VALID);
        }
    }

    public ProcessingRequest create(String identifier, String language,
            byte[] bytes, String fileName, String basePath,
            Investigator investigator) {
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setIdentifier(identifier);
        processingRequest.setCreationDate(DateUtilities.getTimestamp());
        processingRequest.setFileName(fileName);
        processingRequest.setBasePath(basePath);
        processingRequest.setLanguage(language);
        processingRequest.setBytes(bytes);
        processingRequest.setState(ProcessState.CREATED.getState());
        processingRequest.setInvestigator(investigator);
        save(processingRequest);
        return processingRequest;
    }

    public ProcessingRequest updateState(ProcessingRequest processingRequest, ProcessState processState) {
        processingRequest.setLastUpdate(DateUtilities.getTimestamp());
        processingRequest.setState(processState.getState());
        update(processingRequest);
        return processingRequest;
    }

}
