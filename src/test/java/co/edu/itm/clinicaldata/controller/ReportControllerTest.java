package co.edu.itm.clinicaldata.controller;

import static org.springframework.http.HttpStatus.OK;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.service.ReportService;

@RunWith(MockitoJUnitRunner.class)
public class ReportControllerTest {

    private static final String FILE_NAME = "filename.txt";
    private static final byte[] BYTES = "some xml".getBytes();

    @InjectMocks
    ReportController reportController;

    @Mock
    ReportService reportService;

    @Mock
    FileUtilities fileUtilities;

    @Test
    public void byRequestTest() throws ValidateException {
        // arrange
        String identifier = "";
        Mockito.when(reportService.byRequest(Mockito.anyString())).thenReturn(new ByteArrayInputStream(BYTES));
        Mockito.when(fileUtilities.createFileName(Mockito.anyString(), Mockito.anyString())).thenReturn(FILE_NAME);

        // act
        ResponseEntity<InputStreamResource> inputStream = reportController.byRequest(identifier);

        // assert
        Assert.assertNotNull(inputStream);
        Assert.assertEquals(inputStream.getStatusCode(), OK);
    }

    @Test
    public void byInvestigatorTest() throws ValidateException {
        // arrange
        Long investigatorId = 1L;
        Mockito.when(reportService.byInvestigator(Mockito.anyLong())).thenReturn(new ByteArrayInputStream(BYTES));
        Mockito.when(fileUtilities.createFileName(Mockito.anyString(), Mockito.anyString())).thenReturn(FILE_NAME);

        // act
        ResponseEntity<InputStreamResource> inputStream = reportController.byInvestigator(investigatorId);

        // assert
        Assert.assertNotNull(inputStream);
        Assert.assertEquals(inputStream.getStatusCode(), OK);
    }

}
