package co.edu.itm.clinicaldata.controller;

import static org.springframework.http.HttpStatus.OK;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.service.ProcessDataService;

@RunWith(MockitoJUnitRunner.class)
public class ProcessDataControllerTest {

	@Mock
	ProcessDataService processDataService;

	@InjectMocks
	ProcessDataController processDataController;

	@Test
	public void processStateTest() throws ValidateException {
		//arrange
		String processId = "1";
		String messageToReturn = "Señor usuario, su proceso se encuentra en proceso " + processId;
		Mockito.when(processDataService.processState(Mockito.anyString())).thenReturn(messageToReturn);

		//act
		ResponseEntity<String> message = processDataController.processState(processId);

		//assert
		Assert.assertEquals(message.getStatusCode(), OK);
		Assert.assertEquals(message.getBody(), messageToReturn);
	}

	@Test
	public void resultProcessTest() throws ValidateException {
		//arrange
		String processId = "1";
		String messageToReturn = "Señor usuario, la solicitud terminó el proceso exitosamente " + processId;
		Mockito.when(processDataService.processResult(Mockito.anyString())).thenReturn(messageToReturn);

		//act
		ResponseEntity<String> message = processDataController.processResult(processId);

		//assert
		Assert.assertEquals(message.getStatusCode(), OK);
		Assert.assertEquals(message.getBody(), messageToReturn);
	}

	@Test
	public void startProcessTest() throws ValidateException {
		//arrange
		Params params = new Params();
		params.setInvestigatorName("Juan");
		String messageToReturn = "Señor usuario, la solicitud terminó el proceso exitosamente ";
		Mockito.when(processDataService.startProcess(Mockito.any(Params.class))).thenReturn(messageToReturn);

		//act
		ResponseEntity<String> message = processDataController.startProcess(params);

		//assert
		Assert.assertEquals(message.getStatusCode(), OK);
		Assert.assertEquals(message.getBody(), messageToReturn);
	}

}
