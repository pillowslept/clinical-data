package co.edu.itm.clinicaldata.service;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Languages;
import co.edu.itm.clinicaldata.model.User;
import co.edu.itm.clinicaldata.util.Validations;

@Service
public class ProcessDataService {

	@Autowired
	UserService userService;

	private static final Logger LOGGER = Logger.getLogger(ProcessDataService.class.getName());

	private final List<String> languagesAllowed = Arrays.asList(Languages.JAVA.toString(), Languages.PYTHON.toString(), Languages.R.toString());

	public String processState(Long processId) throws ValidateException{
	    validateProcessId(processId);
		LOGGER.info("Consultando el estado de la solicitud " + processId);
		return "La solicitud se está procesando, identificador del proceso " + processId;
	}

	public String resultProcess(Long processId) throws ValidateException{
	    validateProcessId(processId);
		LOGGER.info("Consultando el resultado de la solicitud " + processId);
		return "La solicitud " + processId + " terminó su proceso y su respuesta fue exitosa.";
	}

	public String startProcess(Params params) throws ValidateException {
		validateFields(params);
		validateLanguagesAllowed(params);
		if(params.getLanguage().equalsIgnoreCase(Languages.JAVA.toString())){
			LOGGER.info("El lenguaje a procesar es " + Languages.JAVA);
		}
		if(params.getLanguage().equalsIgnoreCase(Languages.PYTHON.toString())){
			LOGGER.info("El lenguaje a procesar es " + Languages.PYTHON);
		}
		if(params.getLanguage().equalsIgnoreCase(Languages.R.toString())){
			LOGGER.info("El lenguaje a procesar es " + Languages.R);
		}
		createUser(params);
		Long processId = 102030L;
		LOGGER.info("Comenzando el procesamiento de la solicitud " + processId);
		return "Señor " + params.getUserName() + " su solicitud ha comenzado a ser procesada, el identificador generado es " + processId;
	}

    private void validateLanguagesAllowed(Params params)
            throws ValidateException {
	    if(!languagesAllowed.contains(params.getLanguage())){
	        throw new ValidateException("La información diligenciada en el campo <language> no es soportada por la aplicación");
	    }
    }

	private void validateFields(Params params) throws ValidateException {
		if(Validations.field(params.getLanguage())){
			throw new ValidateException("El campo <language> debe ser diligenciado");
		}
		if(Validations.field(params.getFunction())){
			throw new ValidateException("El campo <function> debe ser diligenciado");
		}
	}

	private void validateProcessId(Long processId) throws ValidateException {
        if(Validations.field(processId)){
            throw new ValidateException("El <id> del proceso debe ser identificador válido");
        }
    }

	private void createUser(Params params){
		User user = new User();
		user.setName(params.getUserName());
		user.setSalary(1000);
		user.setAge(25);
		userService.saveUser(user);
	}
}
