package co.edu.itm.clinicaldata.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.util.DateUtilities;
import co.edu.itm.clinicaldata.util.FileUtilities;

@Service
public class ProcessDataService {

    private static final Logger LOGGER = Logger
            .getLogger(ProcessDataService.class.getName());

    @Autowired
    InvestigatorService investigatorService;

    @Autowired
    ProcessingRequestService processingRequestService;

    @Autowired
    ClusterService clusterService;

    /**
     * Se encarga de obtener el estado de una solicitud por medio de su identificador
     * @param processIdentifier
     * @return
     * @throws ValidateException
     */
    public String processState(String processIdentifier) throws ValidateException {
        ProcessingRequest processingRequest = processingRequestService
                .validateAndFindByIdentifier(processIdentifier);
        return String
                .format("La solicitud <%s> con fecha de creación <%s> se encuentra en estado %s",
                        processingRequest.getIdentifier(),
                        DateUtilities.timestampToString(processingRequest.getCreationDate()),
                        processingRequest.getState());
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
        String readedContent = FileUtilities.readFile(fullPath);
        LOGGER.info("Contenido del archivo leído " + readedContent);
        return String
                .format("La solicitud <%s> ha terminado su procesamiento, su estado actual es <%s>, su resultado fue <%s>",
                        processingRequest.getIdentifier(),
                        processingRequest.getState(),
                        processingRequest.getResult());
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
        processingRequest = processingRequestService.updateState(processingRequest, ProcessState.PROCESSING);

        //Se llama proceso del cluster en background
        clusterService.sendProcessToCluster(processingRequest);

        return String.format("Investigador <%s>, la solicitud <%s> ha comenzado a ser procesada por el cluster.",
                        investigator.getName(),
                        processingRequest.getIdentifier());
    }

    private void validateCreatedProcess(ProcessingRequest processingRequest) throws ValidateException {
        if(!processingRequest.getState().equals(ProcessState.CREATED.getState())){
            throw new ValidateException(
                    String.format(
                            "La solicitud <%s> no se encuentra en un estado válido para ser procesada. Estado actual <%s>",
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
            throw new ValidateException(String.format("La solicitud <%s> no ha terminado su procesamiento", processingRequest.getIdentifier()));
        }
    }

}
