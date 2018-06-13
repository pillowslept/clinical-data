package co.edu.itm.clinicaldata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "configuration")
public class FolderConf {

    private String defaultFolder;
    private String templateName;
    private String errOutputFile;
    private String logOutputFile;
    private String shFileName;
    private String keyToReplace;

    public String getDefaultFolder() {
        return defaultFolder;
    }

    public void setDefaultFolder(String defaultFolder) {
        this.defaultFolder = defaultFolder;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getErrOutputFile() {
        return errOutputFile;
    }

    public void setErrOutputFile(String errOutputFile) {
        this.errOutputFile = errOutputFile;
    }

    public String getLogOutputFile() {
        return logOutputFile;
    }

    public void setLogOutputFile(String logOutputFile) {
        this.logOutputFile = logOutputFile;
    }

    public String getShFileName() {
        return shFileName;
    }

    public void setShFileName(String shFileName) {
        this.shFileName = shFileName;
    }

    public String getKeyToReplace() {
        return keyToReplace;
    }

    public void setKeyToReplace(String keyToReplace) {
        this.keyToReplace = keyToReplace;
    }

}
