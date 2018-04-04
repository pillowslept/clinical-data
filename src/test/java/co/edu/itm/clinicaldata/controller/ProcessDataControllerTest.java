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
import co.edu.itm.clinicaldata.service.ProcessDataService;

@RunWith(MockitoJUnitRunner.class)
public class ProcessDataControllerTest {

	@Mock
	ProcessDataService processDataService;

	@InjectMocks
	ProcessDataController processDataController;

	@Test
	public void processStateTest() {
		//arrange
		Long processId = 1L;
		String messageToReturn = "Señor usuario, su proceso se encuentra en proceso " + processId;
		Mockito.when(processDataService.processState(Mockito.anyLong())).thenReturn(messageToReturn);

		//act
		ResponseEntity<String> message = processDataController.processState(processId);

		//assert
		Assert.assertEquals(message.getStatusCode(), OK);
		Assert.assertEquals(message.getBody(), messageToReturn);
	}

	@Test
	public void resultProcessTest() {
		//arrange
		Long processId = 1L;
		String messageToReturn = "Señor usuario, la solicitud terminó el proceso exitosamente " + processId;
		Mockito.when(processDataService.resultProcess(Mockito.anyLong())).thenReturn(messageToReturn);

		//act
		ResponseEntity<String> message = processDataController.resultProcess(processId);

		//assert
		Assert.assertEquals(message.getStatusCode(), OK);
		Assert.assertEquals(message.getBody(), messageToReturn);
	}

	@Test
	public void startProcessTest() {
		//arrange
		Params params = new Params();
		params.setUserName("Juan");
		String messageToReturn = "Señor usuario, la solicitud terminó el proceso exitosamente ";
		Mockito.when(processDataService.startProcess(Mockito.any(Params.class))).thenReturn(messageToReturn);

		//act
		ResponseEntity<String> message = processDataController.startProcess(params);

		//assert
		Assert.assertEquals(message.getStatusCode(), OK);
		Assert.assertEquals(message.getBody(), messageToReturn);
	}

}
