package co.edu.itm.clinicaldata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.service.ResponseService;

@RestController
@RequestMapping("/api/response")
public class ResponseController {

    @Autowired
    ResponseService responseService;

    @RequestMapping(value = "/result/{identifier}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<FileSystemResource> result(@PathVariable("identifier") String identifier)
            throws ValidateException {
        return buildResponse(responseService.obtainResponseFile(identifier));
    }

    @RequestMapping(value = "/request/{identifier}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<FileSystemResource> request(@PathVariable("identifier") String identifier)
            throws ValidateException {
        return buildResponse(responseService.obtainRequestFile(identifier));
    }

    private ResponseEntity<FileSystemResource> buildResponse(FileSystemResource fileSystemResource) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileSystemResource.getFilename());
        return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN).body(fileSystemResource);
    }

}