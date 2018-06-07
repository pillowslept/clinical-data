package co.edu.itm.clinicaldata.controller;

import static co.edu.itm.clinicaldata.util.Constants.PDF_FILE_EXTENSION;

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

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.service.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    FileUtilities fileUtilities;

    @RequestMapping(value = "/request/{identifier}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byRequest(
            @PathVariable("identifier") String identifier)
            throws ValidateException {
        return buildResponse(identifier, reportService.byRequest(identifier));
    }

    @RequestMapping(value = "/investigator/{investigatorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> byInvestigator(
            @PathVariable("investigatorId") Long investigatorId)
            throws ValidateException {
        return buildResponse(String.valueOf(investigatorId), reportService.byInvestigator(investigatorId));
    }

    private ResponseEntity<InputStreamResource> buildResponse(String name,
            ByteArrayInputStream bis) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename="
                        + fileUtilities.createFileName(name,
                                PDF_FILE_EXTENSION));

        return ResponseEntity.ok().headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}