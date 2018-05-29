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
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.queue.ProcessQueue;
import co.edu.itm.clinicaldata.util.Commands;
import co.edu.itm.clinicaldata.util.FileUtilities;
import co.edu.itm.clinicaldata.util.Validations;

@Service
public class ClusterService {

    private static final String ERR_OUTPUT_FILE = "prueba.err";
    private static final String LOG_OUTPUT_FILE = "prueba.out";
    private static final String TEMPLATE_NAME = "template.txt";
    private static final String KEY_TO_REPLACE = "%COMMAND%";
    private static final String SH_FILE_NAME = "qsub.sh";
    private static final String SPACE = " ";
    private static final String COMMA = ",";

    private static final Logger LOGGER = Logger.getLogger(ClusterService.class.getName());

    @Autowired
    ProcessingRequestService processingRequestService;

    /**
     * Crea los archivos necesarios para enviar a través del comando qsub una solicitud
     * de procesamiento al servidor
     * @param processingRequest
     */
    @Async
    public void sendProcessToCluster(ProcessingRequest processingRequest) {
        sleep();

        Output output = new Output();
        if (processingRequest.getLanguage().equals(Language.JAVA.getName())) {
            output = javaProcess(processingRequest);
        } else if (processingRequest.getLanguage().equals(Language.PYTHON.getName())) {
            output = pythonProcess(processingRequest);
        } else if (processingRequest.getLanguage().equals(Language.R.getName())) {
            output = rProcess(processingRequest);
        }else{
            //Lenguage no soportado
        }

        updateProcessingRequest(processingRequest, output);
        ProcessQueue.getInstance().add(processingRequest.getIdentifier());
    }

    /**
     * Creación de archivos requeridos en procesamiento de archivo .py (Python)
     * @param processingRequest
     * @return
     */
    private Output pythonProcess(ProcessingRequest processingRequest) {
        Output output = new Output();

        String command = Commands.PYTHON_EXECUTE_COMMAND + buildFilePath(processingRequest.getBasePath(), processingRequest.getFileName());
        createBourneShellScript(processingRequest, command);
        //Execute qsub command
        //executeQsub(processingRequest, output);

        output.setState(ProcessState.PROCESSING.getState());
        return output;
    }

    /**
     * Creación de archivos requeridos en procesamiento de archivo .r (R)
     * @param processingRequest
     * @return
     */
    private Output rProcess(ProcessingRequest processingRequest) {
        Output output = new Output();

        String command = Commands.R_EXECUTE_COMMAND + buildFilePath(processingRequest.getBasePath(), processingRequest.getFileName());
        createBourneShellScript(processingRequest, command);
        //Execute qsub command
        //executeQsub(processingRequest, output);

        output.setState(ProcessState.PROCESSING.getState());
        return output;
    }

    /**
     * Ejecuta el archivo previamente creado y lo encola a través del comando qsub en el servidor
     * @param processingRequest
     * @param output
     */
    private void executeQsub(ProcessingRequest processingRequest, Output output) {
        String result = "";
        ProcessState processState = null;
        Output executeOutput = Commands.executeCommand(Commands.QSUB_COMMAND, processingRequest.getBasePath() + SH_FILE_NAME);
        if (!Validations.field(executeOutput.getError())) {
            result = executeOutput.getError();
            processState = ProcessState.FINISHED_WITH_ERRORS;
            LOGGER.info("Archivo enviado al cluster presenta errores");
        } else {
            result = executeOutput.getResult();
            processState = ProcessState.FINISHED_OK;
            LOGGER.info("Archivo enviado al cluster ok");
        }

        output.setResult(result);
        output.setState(processState.getState());
    }

    /**
     * Creación de archivos requeridos en procesamiento de archivo .java (java)
     * y sus recursos necesarios en proceso previo de compilación
     * @param processingRequest
     * @return
     */
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

        Output compileOutput = Commands.executeCommand(compileBaseCommand, compileCommand);

        if (!Validations.field(compileOutput.getError())) {
            result = compileOutput.getError();
            processState = ProcessState.FINISHED_WITH_ERRORS;
            LOGGER.info("Clase no compilada, presenta errores");
        } else {
            LOGGER.info("Clase compilada con éxito");

            createBourneShellScript(processingRequest, executeBaseCommand + executeCommand);

            processState = ProcessState.PROCESSING;
            Output executeOutput = Commands.executeCommand(executeBaseCommand, executeCommand);
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

    /**
     * Consulta el contenido de un archivo previamente configurado en el servidor,
     * con el contenido crea un archivo .sh en el folder del procesamiento
     * @param processingRequest
     * @param command
     */
    private void createBourneShellScript(ProcessingRequest processingRequest, String command) {
        String templateLanguageFolder = FileUtilities.templateLanguageFolder(processingRequest.getLanguage());
        String readedContent = FileUtilities.readFile(templateLanguageFolder + TEMPLATE_NAME);
        readedContent = readedContent.replace(KEY_TO_REPLACE, command);
        FileUtilities.createFile(readedContent.getBytes(), processingRequest.getBasePath() + SH_FILE_NAME);
    }

    /**
     * Construe la URL de todos los resources requeridos en el procesamiento
     * @param processingRequest
     * @return
     */
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

    /**
     * Actualiza una solicitud, modificando su estado actual
     * @param processingRequest
     * @param output
     */
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

    /**
     * Valida si un proceso enviado al cluster ha terminado de ser procesado.
     * Valida si existe archivo .out o .err dentro del folder de creación del qsub
     * @param identifier
     * @return
     */
    public boolean validateProcessState(String identifier) {
        boolean hasEndProcess = false;
        String result = "";
        ProcessState processState = null;
        ProcessingRequest processingRequest = processingRequestService.findByIdentifier(identifier);
        LOGGER.info("State" + processingRequest.getState());
        if(!processingRequest.getState().equals(ProcessState.PROCESSING.getState())){
            hasEndProcess = true;
        }else{
            boolean exists = FileUtilities.existsFile(processingRequest.getBasePath() + LOG_OUTPUT_FILE);
            if(exists){
                hasEndProcess = true;
                result = FileUtilities.readFile(processingRequest.getBasePath() + LOG_OUTPUT_FILE);
                processState = ProcessState.FINISHED_OK;
            }else{
                exists = FileUtilities.existsFile(processingRequest.getBasePath() + ERR_OUTPUT_FILE);
                if(exists){
                    hasEndProcess = true;
                    result = FileUtilities.readFile(processingRequest.getBasePath() + ERR_OUTPUT_FILE);
                    processState = ProcessState.FINISHED_WITH_ERRORS;
                }
            }

            if(exists){
                Output output = new Output();
                output.setResult(result);
                output.setState(processState.getState());
                updateProcessingRequest(processingRequest, output);
            }
        }
        return hasEndProcess;
    }

    /**
     * Valida que exista un archivo previamente configurado en el servidor, 
     * que es usado como base de creación de archivo .sh
     * @param processingRequest
     * @throws ValidateException
     */
    public void validateLanguageTemplate(ProcessingRequest processingRequest) throws ValidateException{
        String templateLanguageFolder = FileUtilities.templateLanguageFolder(processingRequest.getLanguage());
        boolean exists = FileUtilities.existsFile(templateLanguageFolder + TEMPLATE_NAME);
        if (!exists) {
            throw new ValidateException(
                    String.format(
                            "El template <%s> no existe actualmente en el servidor, favor solicitar configuración al administrador",
                            TEMPLATE_NAME));
        }
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
