package co.edu.itm.clinicaldata.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "configuration")
public class FolderConf {

    private String defaultFolder;
    private String templateFolder;
    private String resourcesFolder;
    private String templateName;
    private String errOutputFile;
    private String logOutputFile;
    private String shFileName;
    private String keyToReplace;

    public String getDefaultFolder() {
        return defaultFolder;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getErrOutputFile() {
        return errOutputFile;
    }

    public String getLogOutputFile() {
        return logOutputFile;
    }

    public String getShFileName() {
        return shFileName;
    }

    public String getKeyToReplace() {
        return keyToReplace;
    }

    public String getTemplateFolder() {
        return templateFolder;
    }

    public String getResourcesFolder() {
        return resourcesFolder;
    }

}
