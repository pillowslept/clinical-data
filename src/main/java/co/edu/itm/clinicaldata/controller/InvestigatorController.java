package co.edu.itm.clinicaldata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.service.InvestigatorService;

@RestController
@RequestMapping("/api/investigator")
public class InvestigatorController {

    @Autowired
    InvestigatorService investigatorService;

    @RequestMapping(value = "/inactive", method = RequestMethod.POST)
    public ResponseEntity<String> inactive(@RequestBody Params params)
            throws ValidateException {
        return new ResponseEntity<String>(investigatorService.inactive(params),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public ResponseEntity<?> create(@RequestBody Params params)
            throws ValidateException {
        return new ResponseEntity<Investigator>(
                investigatorService.create(params), HttpStatus.OK);
    }

}