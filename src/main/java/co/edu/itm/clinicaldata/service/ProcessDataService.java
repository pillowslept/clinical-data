package co.edu.itm.clinicaldata.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.model.User;

@Service
public class ProcessDataService {

	@Autowired
	UserService userService;
	
	private static final Logger LOGGER = Logger.getLogger(ProcessDataService.class.getName());

	public String processState(Long processId){
		LOGGER.info("Consultando el estado de la solicitud " + processId);
		return "La solicitud se está procesando, identificador del proceso " + processId;
	}

	public String resultProcess(Long processId){
		LOGGER.info("Consultando el resultado de la solicitud " + processId);
		return "La solicitud " + processId + " terminó su proceso y su respuesta fue exitosa.";
	}

	public String startProcess(Params params) {
		createUser(params);
		Long processId = 102030L;
		LOGGER.info("Comenzando el procesamiento de la solicitud " + processId);
		return "Señor " + params.getUserName() + " su solicitud ha comenzado a ser procesada, el identificador generado es " + processId;
	}

	private void createUser(Params params){
		User user = new User();
		user.setName(params.getUserName());
		user.setSalary(1000);
		user.setAge(25);
		userService.saveUser(user);
	}
}
