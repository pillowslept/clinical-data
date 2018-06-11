package co.edu.itm.clinicaldata.service;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {

    @Mock
    ProcessingRequestService processingRequestService;
    
    @Mock
    InvestigatorService investigatorService;
    
    @Mock
    ProcessResourceService processResourceService;

    @InjectMocks
    ReportService reportService;

    @Test
    public void byRequestTest() throws ValidateException {
        // arrange
        String processIdentifier = "";
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setId(1L);
        processingRequest.setResult("Es un resultado ok");
        processingRequest.setCreationDate(new Timestamp(0));
        processingRequest.setInvestigator(new Investigator());
        Mockito.when(processingRequestService.validateAndFindByIdentifier(Mockito.anyString())).thenReturn(processingRequest);
        List<ProcessResource> listProcessResource = new ArrayList<>();
        ProcessResource processResource = new ProcessResource();
        processResource.setName("asd.jar");
        processResource.setVersion("2.0");
        listProcessResource.add(processResource);
        Mockito.when(processResourceService.findByProcessingRequestId(Mockito.anyLong())).thenReturn(listProcessResource);

        // act
        ByteArrayInputStream byteArrayInputStream = reportService.byRequest(processIdentifier);

        // assert
        Assert.assertNotNull(byteArrayInputStream);
    }

    @Test
    public void byInvestigatorTest() throws ValidateException {
        // arrange
        Long investigatorId = 1L;
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setId(1L);
        processingRequest.setCreationDate(new Timestamp(0));
        Mockito.when(processingRequestService.validateAndFindByIdentifier(Mockito.anyString())).thenReturn(processingRequest);
        Investigator investigator = new Investigator();
        investigator.setName("Juan");
        Mockito.when(investigatorService.validateAndFind(Mockito.anyLong())).thenReturn(investigator);

        // act
        ByteArrayInputStream byteArrayInputStream = reportService.byInvestigator(investigatorId);

        // assert
        Assert.assertNotNull(byteArrayInputStream);
    }
}
