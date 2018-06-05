package co.edu.itm.clinicaldata.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import co.edu.itm.clinicaldata.dto.Output;

@Component
public class Commands {

    //JAVA COMMANDS
    public static final String JAVA_EXECUTE_COMMAND = "java -cp ";
    public static final String JAVA_COMPILE_COMMAND = "javac ";
    public static final String JAVA_COMPILE_COMMAND_RESOURCES = "javac -cp ";
    //PYTHON COMMANDS
    public static final String PYTHON_EXECUTE_COMMAND = "python ";
    //R COMMANDS
    public static final String R_EXECUTE_COMMAND = "R CMD BATCH ";
    //QSUB COMMANDS
    public static final String QSUB_COMMAND = "qsub ";

    private static final Logger LOGGER = Logger.getLogger(Commands.class.getName());

    public Output executeCommand(String baseCommand, String pathFile){
        String command = baseCommand + pathFile;
        LOGGER.info(String.format("Command <%s>", command));
        return executeCommand(command);
    }

    private Output executeCommand(String command) {
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

    private String readOutput(InputStream inputStream) throws IOException {
        StringBuffer output = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String line = "";
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        return output.toString();
    }

}