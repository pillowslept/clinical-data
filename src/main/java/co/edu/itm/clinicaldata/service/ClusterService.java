package co.edu.itm.clinicaldata.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Output;
import co.edu.itm.clinicaldata.enums.Language;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.queue.ProcessQueue;
import co.edu.itm.clinicaldata.util.Commands;
import co.edu.itm.clinicaldata.util.FileUtilities;
import co.edu.itm.clinicaldata.util.Validations;

@Service
public class ClusterService {

    private static final String SPACE = " ";

    private static final String COMMA = ",";

    private static final Logger LOGGER = Logger.getLogger(ClusterService.class.getName());

    @Autowired
    ProcessingRequestService processingRequestService;

    @Async
    public void sendProcessToCluster(ProcessingRequest processingRequest) {
        //ProcessQueue.getInstance().add(processingRequest.getIdentifier());
        sleep();

        Output output = new Output();
        if (processingRequest.getLanguage().equals(Language.JAVA.getName())) {
            output = javaProcess(processingRequest);
        } else if (processingRequest.getLanguage().equals(Language.PYTHON.getName())) {

        } else if (processingRequest.getLanguage().equals(Language.R.getName())) {

        }else{

        }

        updateProcessingRequest(processingRequest, output);
    }
    
    private Output javaProcess(ProcessingRequest processingRequest){
        Output output = new Output();
        String result = "";
        ProcessState processState = null;
        String compileCommand = null;
        String executeCommand = null;
        String compileBaseCommand = null;
        String executeBaseCommand = Commands.JAVA_EXECUTE_COMMAND;

        if (!Validations.field(processingRequest.getResources())) {
            compileBaseCommand = Commands.JAVA_COMPILE_COMMAND_RESOURCES;
            String resourcesPath = buildResourcesPath(processingRequest);
            compileCommand = resourcesPath
                    + SPACE
                    + buildFilePath(processingRequest.getBasePath(),
                            processingRequest.getFileName());

            executeCommand = resourcesPath
                    + FileUtilities.PATH_SEPARATOR
                    + processingRequest.getBasePath()
                    + SPACE
                    + FilenameUtils
                            .getBaseName(processingRequest.getFileName());
        } else {
            compileBaseCommand = Commands.JAVA_COMPILE_COMMAND;
            compileCommand = buildFilePath(processingRequest.getBasePath(),
                    processingRequest.getFileName());

            executeCommand = buildFilePathExecute(
                    processingRequest.getBasePath(),
                    processingRequest.getFileName());
        }

        Output compileOutput = Commands.executeJavaCommand(compileBaseCommand, compileCommand);

        if (!Validations.field(compileOutput.getError())) {
            result = compileOutput.getError();
            processState = ProcessState.FINISHED_WITH_ERRORS;
            LOGGER.info("Clase no compilada, presenta errores");
        } else {
            LOGGER.info("Clase compilada con éxito");
            Output executeOutput = Commands.executeJavaCommand(executeBaseCommand, executeCommand);
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

        output.setResult(result);
        output.setState(processState.getState());
        return output;
    }

    private String buildResourcesPath(ProcessingRequest processingRequest) {
        String resourceLanguageFolder = FileUtilities.resourceLanguageFolder(processingRequest.getLanguage());
        StringBuilder resourcesPath = new StringBuilder();
        List<String> resources = new ArrayList<>(Arrays.asList(processingRequest.getResources().split(COMMA)));
        for (String resource : resources) {
            resourcesPath.append(resourceLanguageFolder);
            resourcesPath.append(resource);
        }
        return resourcesPath.toString();
    }

    private void updateProcessingRequest(ProcessingRequest processingRequest, Output output) {
        processingRequest.setResult(output.getResult());
        processingRequest.setState(output.getState());
        processingRequestService.update(processingRequest);
    }

    private String buildFilePath(String basePath, String fileName) {
        return basePath + fileName;
    }

    private String buildFilePathExecute(String basePath, String fileName) {
        return basePath + FileUtilities.PATH_SEPARATOR + ". " + FilenameUtils.getBaseName(fileName);
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
