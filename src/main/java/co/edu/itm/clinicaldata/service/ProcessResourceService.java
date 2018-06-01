package co.edu.itm.clinicaldata.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.dto.Resource;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.repository.ProcessResourceRepository;
import co.edu.itm.clinicaldata.util.FileUtilities;
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

    public List<ProcessResource> validateRequiredResources(List<Resource> resources, ProcessingRequest processingRequest) throws ValidateException {
        List<ProcessResource> listProcessResource = new ArrayList<>();
        if(!Validations.field(resources)){
            String resourceLanguageFolder = FileUtilities.resourceLanguageFolder(processingRequest.getLanguage());
            listProcessResource = validateResourcesExistence(resources, resourceLanguageFolder, processingRequest);
        }
        return listProcessResource;
    }

    private List<ProcessResource> validateResourcesExistence(
            List<Resource> resources, String resourceLanguageFolder,
            ProcessingRequest processingRequest) throws ValidateException {
        for (Resource resource : resources) {
            if (Validations.field(resource.getName())) {
                throw new ValidateException("El campo <name> de los recursos debe ser válido");
            }
            boolean exists = FileUtilities.existsFile(resourceLanguageFolder + resource.getName());
            if (!exists) {
                throw new ValidateException(
                        String.format(
                                "El recurso <%s> no existe actualmente en el servidor, favor solicitar configuración al administrador",
                                resource.getName()));
            }
        }
        return createResources(resources, processingRequest);
    }

    private List<ProcessResource> createResources(List<Resource> resources,
            ProcessingRequest processingRequest) throws ValidateException {
        List<ProcessResource> listProcessResource = new ArrayList<>();
        for(Resource resource : resources){
            listProcessResource.add(create(resource, processingRequest));
        }
        return listProcessResource;
    }

}
