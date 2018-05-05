package co.edu.itm.clinicaldata.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "PROCESSING_REQUEST")
public class ProcessingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 40)
    @Column(name = "IDENTIFIER", nullable = false)
    private String identifier;

    @Column(name = "CREATION_DATE", nullable = false)
    private Timestamp creationDate;

    @Column(name = "LAST_UPDATE", nullable = true)
    private Timestamp lastUpdate;

    @Size(max = 10)
    @Column(name = "LANGUAGE", nullable = false)
    private String language;

    @Lob
    @Column(name = "BYTES", nullable = false)
    private byte[] bytes;

    @Column(name = "BASE_PATH", nullable = false)
    private String basePath;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Size(max = 50)
    @Column(name = "STATE", nullable = false)
    private String state;

    @Size(max = 400)
    @Column(name = "RESULT", nullable = true)
    private String result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVESTIGATOR_ID")
    Investigator investigator;

    public Long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Investigator getInvestigator() {
        return investigator;
    }

    public void setInvestigator(Investigator investigator) {
        this.investigator = investigator;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
