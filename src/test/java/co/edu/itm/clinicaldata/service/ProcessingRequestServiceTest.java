package co.edu.itm.clinicaldata.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.repository.ProcessingRequestRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProcessingRequestServiceTest {

    private static final String PROCESS_IDENTIFIER = "1";

    @Mock
    ProcessingRequestRepository processingRequestRepository;

    @InjectMocks
    ProcessingRequestService processingRequestService;

    @Test
    public void findByIdentifierTest() throws ValidateException {
        // arrange
        String identifier = "abcde";
        Mockito.when(processingRequestRepository.findByIdentifier(Mockito.anyString())).thenReturn(new ProcessingRequest());

        // act
        ProcessingRequest processingRequest = processingRequestService.findByIdentifier(identifier);

        // assert
        Assert.assertNotNull(processingRequest);
    }

    @Test
    public void findByInvestigatorIdTest() throws ValidateException {
        // arrange
        Long investigatorId = 1L;
        Mockito.when(processingRequestRepository.findByInvestigatorId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        // act
        List<ProcessingRequest> list = processingRequestService.findByInvestigatorId(investigatorId);

        // assert
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void validateAndFindByIdentifierTest() throws ValidateException {
        // arrange
        String processIdentifier = PROCESS_IDENTIFIER;
        Mockito.when(processingRequestRepository.findByIdentifier(Mockito.anyString())).thenReturn(new ProcessingRequest());

        // act
        ProcessingRequest processingRequest = processingRequestService.validateAndFindByIdentifier(processIdentifier);

        // assert
        Assert.assertNotNull(processingRequest);
    }

    @Test(expected = ValidateException.class)
    public void validateAndFindByIdentifierProcessNullTest() throws ValidateException {
        // arrange
        String processIdentifier = null;

        // act
        processingRequestService.validateAndFindByIdentifier(processIdentifier);
    }

    @Test(expected=ValidateException.class)
    public void validateAndFindByIdentifierWithoutResultTest() throws ValidateException {
        // arrange
        String processIdentifier = PROCESS_IDENTIFIER;
        Mockito.when(processingRequestRepository.findByIdentifier(Mockito.anyString())).thenReturn(null);

     // act
        processingRequestService.validateAndFindByIdentifier(processIdentifier);
    }

    @Test
    public void updateStateTest() throws ValidateException {
        // arrange
        Mockito.when(processingRequestRepository.findByIdentifier(Mockito.anyString())).thenReturn(new ProcessingRequest());

        // act
        ProcessingRequest processingRequest = processingRequestService.updateState(new ProcessingRequest(), ProcessState.CREATED);

        // assert
        Assert.assertNotNull(processingRequest);
    }

    @Test
    public void createTest() throws ValidateException {
        // arrange
        String identifier = "";
        String language = "";
        byte[] bytes = "".getBytes();
        String fileName = "";
        String basePath = "";
        Investigator investigator = new Investigator();

        // act
        ProcessingRequest processingRequest = processingRequestService.create(identifier, language, bytes, fileName, basePath, investigator);

        // assert
        Assert.assertNotNull(processingRequest);
    }

    @Test
    public void validateFinishedProcessTest() throws ValidateException {
        // arrange
        String processIdentifier = PROCESS_IDENTIFIER;
        ProcessingRequest processingRequestReturn = new ProcessingRequest();
        processingRequestReturn.setState(ProcessState.FINISHED_OK.getState());
        Mockito.when(processingRequestRepository.findByIdentifier(Mockito.anyString())).thenReturn(processingRequestReturn);

        // act
        ProcessingRequest processingRequest = processingRequestService.validateFinishedProcess(processIdentifier);

        // assert
        Assert.assertNotNull(processingRequest);
    }

    @Test(expected = ValidateException.class)
    public void validateFinishedProcessCreatedTest() throws ValidateException {
        // arrange
        String processIdentifier = PROCESS_IDENTIFIER;
        ProcessingRequest processingRequestReturn = new ProcessingRequest();
        processingRequestReturn.setState(ProcessState.CREATED.getState());
        Mockito.when(processingRequestRepository.findByIdentifier(Mockito.anyString())).thenReturn(processingRequestReturn);

        // act
        processingRequestService.validateFinishedProcess(processIdentifier);
    }

    @Test(expected = ValidateException.class)
    public void validateFinishedProcessProcessingTest() throws ValidateException {
        // arrange
        String processIdentifier = PROCESS_IDENTIFIER;
        ProcessingRequest processingRequestReturn = new ProcessingRequest();
        processingRequestReturn.setState(ProcessState.PROCESSING.getState());
        Mockito.when(processingRequestRepository.findByIdentifier(Mockito.anyString())).thenReturn(processingRequestReturn);

        // act
        processingRequestService.validateFinishedProcess(processIdentifier);
    }
}
