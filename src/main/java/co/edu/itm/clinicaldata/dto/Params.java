package co.edu.itm.clinicaldata.dto;

import java.io.Serializable;
import java.util.List;

public class Params implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identifier;
    private List<Resource> resources;

    private Long investigatorId;
    private String investigatorName;
    private String investigatorEmail;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getInvestigatorId() {
        return investigatorId;
    }

    public void setInvestigatorId(Long investigatorId) {
        this.investigatorId = investigatorId;
    }

    public String getInvestigatorName() {
        return investigatorName;
    }

    public void setInvestigatorName(String investigatorName) {
        this.investigatorName = investigatorName;
    }

    public String getInvestigatorEmail() {
        return investigatorEmail;
    }

    public void setInvestigatorEmail(String investigatorEmail) {
        this.investigatorEmail = investigatorEmail;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
