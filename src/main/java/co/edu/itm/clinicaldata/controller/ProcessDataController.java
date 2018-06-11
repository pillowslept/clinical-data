package co.edu.itm.clinicaldata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.service.ProcessDataService;

@RestController
@RequestMapping("/api/processData")
public class ProcessDataController {

    @Autowired
    ProcessDataService processDataService;

    @RequestMapping(value = "/state/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<String> processState(
            @PathVariable("identifier") String identifier)
            throws ValidateException {
        return new ResponseEntity<>(
                processDataService.processState(identifier), HttpStatus.OK);
    }

    @RequestMapping(value = "/result/{identifier}", method = RequestMethod.GET)
    public ResponseEntity<String> processResult(
            @PathVariable("identifier") String identifier)
            throws ValidateException {
        return new ResponseEntity<>(
                processDataService.processResult(identifier), HttpStatus.OK);
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseEntity<String> startProcess(@RequestBody Params params)
            throws ValidateException {
        return new ResponseEntity<>(
                processDataService.startProcess(params), HttpStatus.OK);
    }

}