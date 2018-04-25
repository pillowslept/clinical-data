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
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.service.InvestigatorService;

@RunWith(MockitoJUnitRunner.class)
public class InvestigatorControllerTest {

    @Mock
    InvestigatorService investigatorService;

    @InjectMocks
    InvestigatorController investigatorController;

    @Test
    public void inactivateTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setUserId(1L);
        String messageToReturn = "Cuenta inactivada " + params.getUserId();
        Mockito.when(investigatorService.inactivate(Mockito.any(Params.class)))
                .thenReturn(messageToReturn);

        // act
        ResponseEntity<String> message = investigatorController
                .inactivate(params);

        // assert
        Assert.assertEquals(message.getStatusCode(), OK);
        Assert.assertEquals(message.getBody(), messageToReturn);
    }

    @Test
    public void activateTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setUserId(1L);
        String messageToReturn = "Cuenta activada " + params.getUserId();
        Mockito.when(investigatorService.activate(Mockito.any(Params.class)))
                .thenReturn(messageToReturn);

        // act
        ResponseEntity<String> message = investigatorController
                .activate(params);

        // assert
        Assert.assertEquals(message.getStatusCode(), OK);
        Assert.assertEquals(message.getBody(), messageToReturn);
    }

    @Test
    public void createTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setUserName("Juan");
        Investigator investigator = new Investigator();
        Mockito.when(investigatorService.create(Mockito.any(Params.class)))
                .thenReturn(investigator);

        // act
        ResponseEntity<Investigator> message = investigatorController
                .create(params);

        // assert
        Assert.assertEquals(message.getStatusCode(), OK);
        Assert.assertEquals(message.getBody(), investigator);
    }

}
