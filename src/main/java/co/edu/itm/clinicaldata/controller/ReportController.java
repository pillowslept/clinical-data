package co.edu.itm.clinicaldata.controller;

import java.io.ByteArrayInputStream;

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
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.service.ProcessDataService;
import co.edu.itm.clinicaldata.util.FileUtilities;
import co.edu.itm.clinicaldata.util.GeneratePdfReport;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ProcessDataService processDataService;

    @RequestMapping(value = "/result/{identifier}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> resultPDF(
            @PathVariable("identifier") String identifier)
            throws ValidateException {

        ProcessingRequest processingRequest = processDataService.processResultForReport(identifier);

        ByteArrayInputStream bis = GeneratePdfReport.processResult(processingRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                "inline; filename=" + FileUtilities.createFileName(identifier, FileUtilities.PDF_FILE_EXTENSION));

        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}