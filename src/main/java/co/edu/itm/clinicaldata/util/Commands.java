package co.edu.itm.clinicaldata.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;

import co.edu.itm.clinicaldata.dto.Output;

public class Commands {

    //JAVA COMMANDS
    public static final String JAVA_EXECUTE_COMMAND = "java -cp ";
    public static final String JAVA_COMPILE_COMMAND = "javac ";
    public static final String JAVA_COMPILE_COMMAND_RESOURCES = "javac -cp ";
    //PYTHON COMMANDS
    public static final String PYTHON_EXECUTE_COMMAND = "python ";
    //R COMMANDS
    public static final String R_EXECUTE_COMMAND = "R CMD BATCH ";

    private static final Logger LOGGER = Logger.getLogger(Commands.class.getName());

    public static void prepareExecute(String pathFile){
        String domainName = "google.com";

        String command = "";

        if(SystemUtils.IS_OS_WINDOWS_10){
            command = "ping -n 3 " + domainName;
        } else if(SystemUtils.IS_OS_LINUX){
            command = "qsub " + pathFile;
        }

        Output output = executeCommand(command);

        LOGGER.error(String.format("Resultado de comando ejecutado <%s>", output));
    }

    private static String readOutput(InputStream inputStream) throws IOException {
        StringBuffer output = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String line = "";
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        return output.toString();
    }
    
    public static Output executeCommand(String command) {
        Output output = new Output();
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            output.setResult(readOutput(p.getInputStream()));
            output.setError(readOutput(p.getErrorStream()));
        } catch (Exception ex) {
            LOGGER.error(String.format("Ocurrió un error intentando ejecutar el comando <%s>", command), ex);
        }
        return output;
    }

    public static Output executeCommand(String baseCommand, String pathFile){
        String command = baseCommand + pathFile;
        LOGGER.info(String.format("Command <%s>", command));
        return executeCommand(command);
    }

    public static void main(String[] args) {
        prepareExecute("");
        executeCommand(JAVA_EXECUTE_COMMAND, "C:\\Users\\ceiba\\clinicaldata\\java\\9be89737-8fc6-467f-a268-0ff3df687e3c\\;. Test");
    }

}
