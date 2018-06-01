package co.edu.itm.clinicaldata.dto;

import java.io.Serializable;

public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
