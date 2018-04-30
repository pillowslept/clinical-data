package co.edu.itm.clinicaldata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.repository.ProcessingRequestRepository;
import co.edu.itm.clinicaldata.util.RandomUtilities;

@Service
@Transactional
public class ProcessingRequestService {

    @Autowired
    private ProcessingRequestRepository processingRequestRepository;

    public ProcessingRequest findById(Long id) {
        return processingRequestRepository.findOne(id);
    }

    public ProcessingRequest findByIdentifier(String identifier) {
        return processingRequestRepository.findByIdentifier(identifier);
    }

    public void save(ProcessingRequest processingRequest) {
        processingRequestRepository.save(processingRequest);
    }

    public void update(ProcessingRequest processingRequest) {
        save(processingRequest);
    }

    public void deleteById(Long id) {
        processingRequestRepository.delete(id);
    }

    public void deleteAll() {
        processingRequestRepository.deleteAll();
    }

    public List<ProcessingRequest> findAll() {
        return processingRequestRepository.findAll();
    }

    public ProcessingRequest createProcessingRequest(String language, byte[] bytes, String fileName, String basePath){
        ProcessingRequest processingRequest = new ProcessingRequest();
        String identifier = RandomUtilities.randomIdentifier();
        processingRequest.setIdentifier(identifier);
        processingRequest.setFileName(fileName);
        processingRequest.setBasePath(basePath);
        processingRequest.setLanguage(language);
        processingRequest.setBytes(bytes);
        processingRequest.setState(ProcessState.CREATED.getState());
        save(processingRequest);
        return processingRequest;
    }

}
