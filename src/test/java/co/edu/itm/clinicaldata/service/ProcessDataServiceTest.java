package co.edu.itm.clinicaldata.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;

@RunWith(MockitoJUnitRunner.class)
public class ProcessDataServiceTest {

    private static final String INVESTIGATOR_NAME = "Juan";
    private static final String IDENTIFIER_REQUEST = "1233";
    private static final long INVESTIGATOR_ID = 1L;

    @Mock
    InvestigatorService investigatorService;

    @Mock
    ProcessingRequestService processingRequestService;

    @Mock
    ClusterService clusterService;

    @Mock
    ProcessResourceService processResourceService;

    @Mock
    FileUtilities fileUtilities;

    @InjectMocks
    ProcessDataService processDataService;

    @Test
    public void processStateTest() throws ValidateException {
        // arrange
        String processIdentifier = "";
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setState(ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
        processingRequest.setCreationDate(new Timestamp(0));
        Mockito.when(processingRequestService.validateAndFindByIdentifier(Mockito.anyString())).thenReturn(processingRequest);

        // act
        String message = processDataService.processState(processIdentifier);

        // assert
        Assert.assertNotNull(message);
    }

    @Test
    public void processResultTest() throws ValidateException {
        // arrange
        String processIdentifier = "";
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setBasePath("basepath");
        processingRequest.setState(ProcessState.FINISHED_OK.getState());
        processingRequest.setCreationDate(new Timestamp(0));
        Mockito.when(processingRequestService.validateFinishedProcess(Mockito.anyString())).thenReturn(processingRequest);
        Mockito.when(fileUtilities.readFile(Mockito.anyString())).thenReturn("Contenido leído");

        // act
        String message = processDataService.processResult(processIdentifier);

        // assert
        Assert.assertNotNull(message);
    }

    @Test
    public void startProcessTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setInvestigatorId(INVESTIGATOR_ID);
        Investigator investigator = new Investigator();
        investigator.setName(INVESTIGATOR_NAME);
        investigator.setId(INVESTIGATOR_ID);
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setIdentifier(IDENTIFIER_REQUEST);
        processingRequest.setState(ProcessState.CREATED.getState());
        processingRequest.setInvestigator(investigator);
        Mockito.when(processingRequestService.validateAndFindByIdentifier(Mockito.anyString())).thenReturn(processingRequest);
        Mockito.when(investigatorService.validateAndFind(Mockito.anyLong())).thenReturn(investigator);
        Mockito.when(processResourceService.validateRequiredResources(Mockito.any(), Mockito.any(ProcessingRequest.class))).thenReturn(new ArrayList<>());
        Mockito.when(processingRequestService.updateState(Mockito.any(), Mockito.any())).thenReturn(processingRequest);

        // act
        String message = processDataService.startProcess(params);

        // assert
        Assert.assertNotNull(message);
    }

    @Test(expected=ValidateException.class)
    public void startProcessStateNotValidTest() throws ValidateException {
        // arrange
        Params params = new Params();
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setIdentifier(IDENTIFIER_REQUEST);
        processingRequest.setState(ProcessState.FINISHED_OK.getState());
        Mockito.when(processingRequestService.validateAndFindByIdentifier(Mockito.anyString())).thenReturn(processingRequest);

        // act
        processDataService.startProcess(params);
    }

    @Test(expected=ValidateException.class)
    public void startProcessNotValidInvestigatorTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setInvestigatorId(INVESTIGATOR_ID);
        Investigator investigator = new Investigator();
        investigator.setId(2L);
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setIdentifier(IDENTIFIER_REQUEST);
        processingRequest.setState(ProcessState.CREATED.getState());
        processingRequest.setInvestigator(investigator);
        Investigator investigatorFound = new Investigator();
        investigatorFound.setId(INVESTIGATOR_ID);
        investigatorFound.setName(INVESTIGATOR_NAME);
        Mockito.when(processingRequestService.validateAndFindByIdentifier(Mockito.anyString())).thenReturn(processingRequest);
        Mockito.when(investigatorService.validateAndFind(Mockito.anyLong())).thenReturn(investigatorFound);

        // act
        processDataService.startProcess(params);
    }
}
