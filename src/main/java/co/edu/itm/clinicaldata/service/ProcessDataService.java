package co.edu.itm.clinicaldata.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Params;

@Service
public class ProcessDataService {

	private static final Logger LOGGER = Logger.getLogger(ProcessDataService.class.getName());

	public String processState(Long processId){
		LOGGER.info("Estado de la solicitud " + processId);
		return "La solicitud se está procesando, identificador del proceso " + processId;
	}

	public String resultProcess(Long processId){
		return "La solicitud " + processId + " terminó su proceso y su respuesta fue exitosa.";
	}

	public Long startProcess(Params params) {
		return 102030L;
	}
}
