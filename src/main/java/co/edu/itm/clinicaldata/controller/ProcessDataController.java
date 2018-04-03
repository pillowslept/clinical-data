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
import co.edu.itm.clinicaldata.service.ProcessDataService;

@RestController
@RequestMapping("/api")
public class ProcessDataController {

	@Autowired
	ProcessDataService processDataService;

	@RequestMapping(value = "/processState/{processId}", method = RequestMethod.GET)
	public ResponseEntity<String> processState(@PathVariable("processId") Long processId) {
		return new ResponseEntity<String>(processDataService.processState(processId), HttpStatus.OK);
	}

	@RequestMapping(value = "/resultProcess/{processId}", method = RequestMethod.GET)
	public ResponseEntity<String> resultProcess(@PathVariable("processId") Long processId) {
		return new ResponseEntity<String>(processDataService.resultProcess(processId), HttpStatus.OK);
	}

	@RequestMapping(value = "/startProcess", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody Params params) {
		Long processId = processDataService.startProcess(params);
		String response = "Señor " + params.getUserName() + " su solicitud ha comenzado a ser procesada, el identificador generado es " + processId;
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

}