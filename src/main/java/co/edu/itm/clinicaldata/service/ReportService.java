package co.edu.itm.clinicaldata.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.util.GeneratePdfReport;

@Service
@Transactional
public class ReportService {

    @Autowired
    ProcessingRequestService processingRequestService;

    @Autowired
    InvestigatorService investigatorService;

    @Autowired
    ProcessResourceService processResourceService;

    public ByteArrayInputStream byRequest(String processIdentifier) throws ValidateException {

        ProcessingRequest processingRequest = processingRequestService.validateAndFindByIdentifier(processIdentifier);
        List<ProcessResource> listProcessResource = processResourceService.findByProcessingRequestId(processingRequest.getId());
        List<ProcessingRequest> listProcessingRequest = new ArrayList<>();
        listProcessingRequest.add(processingRequest);

        return GeneratePdfReport.processRequest(processingRequest.getInvestigator(), listProcessingRequest, listProcessResource);
    }

    public ByteArrayInputStream byInvestigator(Long investigatorId) throws ValidateException {

        Investigator investigator = investigatorService.validateAndFind(investigatorId);
        List<ProcessingRequest> listProcessingRequest = processingRequestService.findByInvestigatorId(investigatorId);

        return GeneratePdfReport.processRequest(investigator, listProcessingRequest, new ArrayList<>());
    }

}
