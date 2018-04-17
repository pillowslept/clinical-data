package co.edu.itm.clinicaldata.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Logger;

public class Commands {
    
    private static final Logger LOGGER = Logger.getLogger(Commands.class.getName());

    public static void prepareExecute(String pathFile){
        String domainName = "google.com";

        String command = "";

        if(SystemUtils.IS_OS_WINDOWS_10){
            command = "ping -n 3 " + domainName;
        } else if(SystemUtils.IS_OS_LINUX){
            command = "ping -c 3 " + domainName;
        }

        String output = executeCommand(command);

        LOGGER.error(String.format("Resultado de comando ejecutado <%s>", output));
    }

    public static String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error(String.format("Ocurrió un error intentando ejecutar el comando <%s>", command), ex);
        }
        return output.toString();
    }

    public static void main(String[] args) {
        prepareExecute("");
    }

}
