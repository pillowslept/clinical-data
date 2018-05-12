package co.edu.itm.clinicaldata.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Output;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.queue.ProcessQueue;
import co.edu.itm.clinicaldata.util.Commands;
import co.edu.itm.clinicaldata.util.Validations;

@Service
public class ClusterService {

    private static final Logger LOGGER = Logger.getLogger(ClusterService.class.getName());

    @Autowired
    ProcessingRequestService processingRequestService;

    @Async
    public void sendProcessToCluster(ProcessingRequest processingRequest) {
        ProcessQueue.getInstance().add(processingRequest.getIdentifier());
        sleep();

        Output compileOutput = Commands.executeJavaCommand(
                Commands.JAVA_COMPILE_COMMAND,
                buildFilePath(processingRequest.getBasePath(),
                        processingRequest.getFileName()));

        String result = "";
        ProcessState processState = null;
        if (!Validations.field(compileOutput.getError())) {
            result = compileOutput.getError();
            processState = ProcessState.FINISHED_WITH_ERRORS;
            LOGGER.info("Clase no compilada, presenta errores");
        } else {
            LOGGER.info("Clase compilada con éxito");
            Output executeOutput = Commands.executeJavaCommand(
                    Commands.JAVA_EXECUTE_COMMAND,
                    buildFilePathExecute(processingRequest.getBasePath(),
                            processingRequest.getFileName()));
            if (!Validations.field(executeOutput.getError())) {
                result = executeOutput.getError();
                processState = ProcessState.FINISHED_WITH_ERRORS;
                LOGGER.info("Clase no ejecutada, presenta errores");
            } else {
                result = executeOutput.getResult();
                processState = ProcessState.FINISHED_OK;
                LOGGER.info("Clase ejecutada con éxito");
            }
        }

        updateProcessingRequest(processingRequest, result, processState);
    }

    private void updateProcessingRequest(ProcessingRequest processingRequest,
            String output, ProcessState processState) {
        processingRequest.setResult(output);
        processingRequest.setState(processState.getState());
        processingRequestService.update(processingRequest);
    }

    private String buildFilePath(String basePath, String fileName) {
        return basePath + fileName;
    }

    private String buildFilePathExecute(String basePath, String fileName) {
        return basePath + Commands.PATH_SEPARATOR + ". " + FilenameUtils.getBaseName(fileName);
    }

    private String buildFilePathExecuteWithJar(String basePath, String fileName) {
        return basePath + "." + Commands.PATH_SEPARATOR + " JARs " + FilenameUtils.getBaseName(fileName);
    }

    public boolean validateProcessState(String identifier) {
        boolean hasEndProcess = false;
        ProcessingRequest processingRequest = processingRequestService.findByIdentifier(identifier);
        sleep();
        return hasEndProcess;
    }

    private void sleep() {
        LOGGER.info("Comenzando el proceso en el cluster, simulando espera de 20 segundos");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("Cumplida la espera, se comienza a procesar la solicitud");
    }

}
