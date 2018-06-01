package co.edu.itm.clinicaldata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.dto.Resource;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.repository.ProcessResourceRepository;
import co.edu.itm.clinicaldata.util.Validations;

@Service
@Transactional
public class ProcessResourceService {

    @Autowired
    private ProcessResourceRepository processResourceRepository;

    public ProcessResource create(Resource resource, ProcessingRequest processingRequest) throws ValidateException {
        validateCreate(resource);
        ProcessResource processResource = new ProcessResource();
        processResource.setName(resource.getName());
        processResource.setVersion(resource.getVersion());
        processResource.setProcessingRequest(processingRequest);
        processResourceRepository.save(processResource);
        return processResource;
    }

    private void validateCreate(Resource resource) throws ValidateException {
        if(Validations.field(resource.getName())){
            throw new ValidateException("El campo <name> del recurso no es válido");
        }
    }

    public List<ProcessResource> findByProcessingRequestId(Long processingRequestId) {
        return processResourceRepository.findByProcessingRequestId(processingRequestId);
    }
}
