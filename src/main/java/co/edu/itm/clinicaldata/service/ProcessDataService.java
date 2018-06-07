package co.edu.itm.clinicaldata.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.util.DateUtilities;

@Service
public class ProcessDataService {

    private static final String PROCESS_NOT_FINISHED_YET = "La solicitud <%s> no ha terminado su procesamiento";
    private static final String PROCESS_STATE_NOT_VALID = "La solicitud <%s> no se encuentra en un estado válido para ser procesada. Estado actual <%s>";
    private static final String PROCESS_STARTED = "Investigador <%s>, la solicitud <%s> ha comenzado a ser procesada por el cluster.";
    private static final String PROCESS_RESULT = "La solicitud <%s> ha terminado su procesamiento, su estado actual es <%s>, su resultado fue <%s>";
    private static final String PROCESS_STATE = "La solicitud <%s> con fecha de creación <%s> se encuentra en estado <%s>";

    private static final Logger LOGGER = Logger.getLogger(ProcessDataService.class.getName());

    @Autowired
    InvestigatorService investigatorService;

    @Autowired
    ProcessingRequestService processingRequestService;

    @Autowired
    ClusterService clusterService;

    @Autowired
    ProcessResourceService processResourceService;

    @Autowired
    FileUtilities fileUtilities;

    /**
     * Se encarga de obtener el estado de una solicitud por medio de su identificador
     * @param processIdentifier
     * @return
     * @throws ValidateException
     */
    public String processState(String processIdentifier) throws ValidateException {
        ProcessingRequest processingRequest = processingRequestService
                .validateAndFindByIdentifier(processIdentifier);
        return String.format(PROCESS_STATE, processingRequest.getIdentifier(),
                DateUtilities.timestampToString(processingRequest
                        .getCreationDate()), processingRequest.getState());
    }

    /**
     * Se encarga de procesar el resultado para las solicitudes que hayan finalizado su procesamiento
     * @param processIdentifier
     * @return
     * @throws ValidateException
     */
    public String processResult(String processIdentifier) throws ValidateException {
        ProcessingRequest processingRequest = processingRequestService
                .validateAndFindByIdentifier(processIdentifier);
        validateFinishedProcess(processingRequest);
        String fullPath = getProcessFullPath(processingRequest);
        String readedContent = fileUtilities.readFile(fullPath);
        LOGGER.info("Contenido del archivo leído " + readedContent);
        return String.format(PROCESS_RESULT, processingRequest.getIdentifier(),
                processingRequest.getState(), processingRequest.getResult());
    }

    /**
     * Se encarga de comenzar el proceso de la solicitud previamente creada, envía al cluster el archivo a procesar
     * @param params
     * @return
     * @throws ValidateException
     */
    public String startProcess(Params params) throws ValidateException {
        ProcessingRequest processingRequest = processingRequestService
                .validateAndFindByIdentifier(params.getIdentifier());
        validateCreatedProcess(processingRequest);

        Investigator investigator = investigatorService.validateAndfind(params.getInvestigatorId());

        clusterService.validateLanguageTemplate(processingRequest);

        List<ProcessResource> listProcessResource = processResourceService.validateRequiredResources(params.getResources(), processingRequest);

        processingRequest = processingRequestService.updateState(processingRequest, ProcessState.PROCESSING);

        clusterService.sendProcessToCluster(processingRequest, listProcessResource);

        return String.format(PROCESS_STARTED, investigator.getName(), processingRequest.getIdentifier());
    }

    private void validateCreatedProcess(ProcessingRequest processingRequest) throws ValidateException {
        if(!processingRequest.getState().equals(ProcessState.CREATED.getState())){
            throw new ValidateException(String.format(PROCESS_STATE_NOT_VALID,
                    processingRequest.getIdentifier(),
                    processingRequest.getState()));
        }
    }

    private String getProcessFullPath(ProcessingRequest processingRequest) {
        return processingRequest.getBasePath() + processingRequest.getFileName();
    }

    private void validateFinishedProcess(ProcessingRequest processingRequest) throws ValidateException {
        if(processingRequest.getState().equals(ProcessState.CREATED.getState())
                || processingRequest.getState().equals(ProcessState.PROCESSING.getState())){
            throw new ValidateException(String.format(PROCESS_NOT_FINISHED_YET, processingRequest.getIdentifier()));
        }
    }

}
