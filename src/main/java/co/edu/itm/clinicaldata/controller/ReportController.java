package co.edu.itm.clinicaldata.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.service.InvestigatorService;
import co.edu.itm.clinicaldata.service.ProcessingRequestService;
import co.edu.itm.clinicaldata.util.FileUtilities;
import co.edu.itm.clinicaldata.util.GeneratePdfReport;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    InvestigatorService investigatorService;

    @Autowired
    ProcessingRequestService processingRequestService;

    @RequestMapping(value = "/request/{identifier}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byRequest(
            @PathVariable("identifier") String identifier)
            throws ValidateException {

        ProcessingRequest processingRequest = processingRequestService.validateAndFindByIdentifier(identifier);
        List<ProcessingRequest> list = new ArrayList<>();
        list.add(processingRequest);

        ByteArrayInputStream bis = GeneratePdfReport.processRequest(processingRequest.getInvestigator(), list);

        return buildResponse(identifier, bis);
    }

    @RequestMapping(value = "/investigator/{investigatorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byInvestigator(
            @PathVariable("investigatorId") Long investigatorId)
            throws ValidateException {

        Investigator investigator = investigatorService.validateAndfind(investigatorId);
        List<ProcessingRequest> listProcessingRequest = processingRequestService.findByInvestigatorId(investigatorId);

        ByteArrayInputStream bis = GeneratePdfReport.processRequest(investigator, listProcessingRequest);

        return buildResponse(investigator.getName(), bis);
    }

    private ResponseEntity<InputStreamResource> buildResponse(String name,
            ByteArrayInputStream bis) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename="
                        + FileUtilities.createFileName(name,
                                FileUtilities.PDF_FILE_EXTENSION));

        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}