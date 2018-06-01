package co.edu.itm.clinicaldata.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "PROCESS_RESOURCE")
public class ProcessResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "NAME", nullable = false)
    private String name;

    @Size(max = 20)
    @Column(name = "VERSION", nullable = true)
    private String version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROCESSING_REQUEST_ID")
    ProcessingRequest processingRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ProcessingRequest getProcessingRequest() {
        return processingRequest;
    }

    public void setProcessingRequest(ProcessingRequest processingRequest) {
        this.processingRequest = processingRequest;
    }

}
