package co.edu.itm.clinicaldata.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Output;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.util.Commands;

@Service
public class ClusterService {

    private static final Logger LOGGER = Logger.getLogger(ClusterService.class.getName());

    @Autowired
    ProcessingRequestService processingRequestService;

    @Async
    public void sendProcessToCluster(ProcessingRequest processingRequest) {
        LOGGER.info("Comenzando el proceso en el cluster, simulando espera de 20 segundos");
        sleep();
        LOGGER.info("Cumplida la espera, se comienza a procesar la solicitud");

        Output compileOutput = Commands.executeJavaCommand(
                Commands.JAVA_COMPILE_COMMAND,
                buildFilePath(processingRequest.getBasePath(),
                        processingRequest.getFileName()));

        if(compileOutput.getError() != null && !compileOutput.getError().isEmpty()){
            LOGGER.info("Clase no compilada, presenta errores");
            updateProcessingRequest(processingRequest, compileOutput.getError(), ProcessState.FINISHED_WITH_ERRORS);
        }else{
            LOGGER.info("Clase compilada con éxito");
            Output executeOutput = Commands.executeJavaCommand(
                    Commands.JAVA_EXECUTE_COMMAND,
                    buildFilePathExecute(processingRequest.getBasePath(),
                            processingRequest.getFileName()));
            if(executeOutput.getError() != null && !executeOutput.getError().isEmpty()){
                LOGGER.info("Clase no ejecutada, presenta errores");
                updateProcessingRequest(processingRequest, executeOutput.getError(), ProcessState.FINISHED_WITH_ERRORS);
            }else{
                LOGGER.info("Clase ejecutada con éxito");
                updateProcessingRequest(processingRequest, executeOutput.getResult(), ProcessState.FINISHED_OK);
            }
        }
    }

    private void updateProcessingRequest(ProcessingRequest processingRequest, String output, ProcessState processState){
        processingRequest.setResult(output);
        processingRequest.setState(processState.getState());
        processingRequestService.update(processingRequest);
        LOGGER.info("Solicitud actualizada en BD");
    }

    private String buildFilePath(String basePath, String fileName) {
        return basePath + fileName;
    }

    private String buildFilePathExecute(String basePath, String fileName) {
        return basePath + ";. " + FilenameUtils.getBaseName(fileName);
    }

    private void sleep(){
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
