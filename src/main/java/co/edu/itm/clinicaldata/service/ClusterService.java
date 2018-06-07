package co.edu.itm.clinicaldata.service;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.component.Commands;
import co.edu.itm.clinicaldata.dto.Output;
import co.edu.itm.clinicaldata.enums.Language;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.queue.ProcessQueue;
import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.util.Constants;
import co.edu.itm.clinicaldata.util.Validations;

@Service
public class ClusterService {

    private static final String LANGUAGE_NOT_SUPPORTED = "El lenguaje enviado a ejecutar no se encuentra configurado";
    private static final String SYSTEM_NOT_VALID = "Sistema no válido para ejecución de archivos con comandos qsub";
    private static final String TEMPLATE_FILE_NOT_EXISTS = "El template <%s> no existe actualmente en el servidor, favor solicitar configuración al administrador";
    private static final String ERROR_CREATING_FILE = "Ocurrió un error creando el archivo .sh en el directorio";
    private static final String ERR_OUTPUT_FILE = "prueba.err";
    private static final String LOG_OUTPUT_FILE = "prueba.out";
    private static final String TEMPLATE_NAME = "template.txt";
    private static final String KEY_TO_REPLACE = "%COMMAND%";
    private static final String SH_FILE_NAME = "qsub.sh";
    private static final String SPACE = " ";

    private static final Logger LOGGER = Logger.getLogger(ClusterService.class.getName());

    @Autowired
    ProcessingRequestService processingRequestService;

    @Autowired
    Commands commands;

    @Autowired
    FileUtilities fileUtilities;

    /**
     * Crea los archivos necesarios para enviar a través del comando qsub una solicitud
     * de procesamiento al servidor
     * @param processingRequest
     */
    @Async
    public void sendProcessToCluster(ProcessingRequest processingRequest, List<ProcessResource> listProcessResource) {
        LOGGER.info(String.format("Iniciando proceso de envío a cluster, el identificador de la solicitud es <%s>", processingRequest.getIdentifier()));
        Output output = new Output();
        if (processingRequest.getLanguage().equals(Language.JAVA.getName())) {
            output = javaProcess(processingRequest, listProcessResource);
        } else if (processingRequest.getLanguage().equals(Language.PYTHON.getName())) {
            String command = Constants.PYTHON_EXECUTE_COMMAND + buildFilePath(processingRequest.getBasePath(), processingRequest.getFileName());
            output = genericProcess(command, processingRequest);
        } else if (processingRequest.getLanguage().equals(Language.R.getName())) {
            String command = Constants.R_EXECUTE_COMMAND + buildFilePath(processingRequest.getBasePath(), processingRequest.getFileName());
            output = genericProcess(command, processingRequest);
        }else{
            output.setResult(LANGUAGE_NOT_SUPPORTED);
            output.setState(ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
        }
        LOGGER.info(String.format("Finalizado el proceso de envío a cluster, el estado de la solicitud es <%s>", output.getState()));

        updateProcessingRequest(processingRequest, output);
    }

    /**
     * Creación de archivos requeridos en procesamiento de archivo .py (Python) o .r (R)
     * @param processingRequest
     * @return
     */
    private Output genericProcess(String command, ProcessingRequest processingRequest) {
        Output output = new Output();

        boolean fileCreated = createBourneShellScript(processingRequest, command);
        if(fileCreated){
            output = executeQsub(processingRequest);
        }else{
            output.setResult(ERROR_CREATING_FILE);
            output.setState(ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
        }

        return output;
    }

    /**
     * Ejecuta el archivo previamente creado y lo encola a través del comando qsub en el servidor
     * Si el sistema operativo no es linux termina la ejecución sin acciones
     * @param processingRequest
     * @param output
     */
    private Output executeQsub(ProcessingRequest processingRequest) {
        Output output = new Output();
        if(fileUtilities.isLinux()){
            String result = "";
            ProcessState processState = null;
            Output executeOutput = commands.executeCommand(Constants.QSUB_COMMAND, processingRequest.getBasePath() + SH_FILE_NAME);
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
            ProcessQueue.getInstance().add(processingRequest.getIdentifier());
        }else{
            output.setResult(SYSTEM_NOT_VALID);
            output.setState(ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
        }
        return output;
    }

    private Output compileJavaFile(ProcessingRequest processingRequest, List<ProcessResource> listProcessResource){
        String compileCommand = null;
        String compileBaseCommand = null;
        if (!Validations.field(listProcessResource)) {
            compileBaseCommand = Constants.JAVA_COMPILE_COMMAND_RESOURCES;
            String resourcesPath = buildResourcesPath(processingRequest, listProcessResource);
            compileCommand = resourcesPath
                    + SPACE
                    + buildFilePath(processingRequest.getBasePath(),
                            processingRequest.getFileName());
        } else {
            compileBaseCommand = Constants.JAVA_COMPILE_COMMAND;
            compileCommand = buildFilePath(processingRequest.getBasePath(),
                    processingRequest.getFileName());
        }

        return commands.executeCommand(compileBaseCommand, compileCommand);
    }

    /**
     * Creación de archivos requeridos en procesamiento de archivo .java (java)
     * y sus recursos necesarios en proceso previo de compilación
     * @param processingRequest
     * @return
     */
    private Output javaProcess(ProcessingRequest processingRequest, List<ProcessResource> listProcessResource){
        Output output = new Output();

        Output compileOutput = compileJavaFile(processingRequest, listProcessResource);

        if (!Validations.field(compileOutput.getError())) {
            output.setResult(compileOutput.getError());
            output.setState(ProcessState.FINISHED_WITH_ERRORS.getState());
            LOGGER.info("Clase no compilada, presenta errores");
        } else {
            String command = Constants.JAVA_EXECUTE_COMMAND + generateExecuteJavaCommand(processingRequest, listProcessResource);
            output = genericProcess(command, processingRequest);
        }

        output.setResult(output.getResult());
        output.setState(output.getState());
        return output;
    }

    private String generateExecuteJavaCommand(ProcessingRequest processingRequest, List<ProcessResource> listProcessResource) {
        String executeCommand = null;

        if (!Validations.field(listProcessResource)) {
            String resourcesPath = buildResourcesPath(processingRequest, listProcessResource);
            executeCommand = resourcesPath
                    + Constants.PATH_SEPARATOR
                    + processingRequest.getBasePath()
                    + SPACE
                    + FilenameUtils
                            .getBaseName(processingRequest.getFileName());
        } else {
            executeCommand = buildFilePathExecute(
                    processingRequest.getBasePath(),
                    processingRequest.getFileName());
        }

        return executeCommand;
    }

    /**
     * Consulta el contenido de un archivo previamente configurado en el servidor,
     * con el contenido crea un archivo .sh en el folder del procesamiento
     * @param processingRequest
     * @param command
     */
    private boolean createBourneShellScript(ProcessingRequest processingRequest, String command) {
        boolean fileCreated = false;
        String templateLanguageFolder = fileUtilities.templateLanguageFolder(processingRequest.getLanguage());
        String readedContent = fileUtilities.readFile(templateLanguageFolder + TEMPLATE_NAME);
        readedContent = readedContent.replace(KEY_TO_REPLACE, command);
        try {
            fileUtilities.createFile(readedContent.getBytes(), processingRequest.getBasePath() + SH_FILE_NAME);
            fileCreated = true;
        } catch (ValidateException e) {
            LOGGER.info(ERROR_CREATING_FILE);
        }
        return fileCreated;
    }

    /**
     * Construe la URL de todos los resources requeridos en el procesamiento
     * @param processingRequest
     * @return
     */
    private String buildResourcesPath(ProcessingRequest processingRequest, List<ProcessResource> listProcessResource) {
        String resourceLanguageFolder = fileUtilities.resourceLanguageFolder(processingRequest.getLanguage());
        StringBuilder resourcesPath = new StringBuilder();
        for (ProcessResource processResource : listProcessResource) {
            resourcesPath.append(resourceLanguageFolder);
            resourcesPath.append(processResource.getName());
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
        return basePath + Constants.PATH_SEPARATOR + ". " + FilenameUtils.getBaseName(fileName);
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
            boolean exists = fileUtilities.existsFile(processingRequest.getBasePath() + LOG_OUTPUT_FILE);
            if(exists){
                hasEndProcess = true;
                result = fileUtilities.readFile(processingRequest.getBasePath() + LOG_OUTPUT_FILE);
                processState = ProcessState.FINISHED_OK;
            }else{
                exists = fileUtilities.existsFile(processingRequest.getBasePath() + ERR_OUTPUT_FILE);
                if(exists){
                    hasEndProcess = true;
                    result = fileUtilities.readFile(processingRequest.getBasePath() + ERR_OUTPUT_FILE);
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
        String templateLanguageFolder = fileUtilities.templateLanguageFolder(processingRequest.getLanguage());
        boolean exists = fileUtilities.existsFile(templateLanguageFolder + TEMPLATE_NAME);
        if (!exists) {
            throw new ValidateException(String.format(TEMPLATE_FILE_NOT_EXISTS,
                    TEMPLATE_NAME));
        }
    }

}
