package co.edu.itm.clinicaldata.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.component.RandomUtilities;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.model.ProcessingRequest;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {

    private static final String DATA = "data";
    private static final byte[] BYTES = "some xml".getBytes();
    private static final String TEXT_PLAIN = "text/plain";

    @Mock
    ProcessingRequestService processingRequestService;

    @Mock
    InvestigatorService investigatorService;

    @Mock
    FileUtilities fileUtilities;

    @Mock
    RandomUtilities randomUtilities;

    @InjectMocks
    FileService fileService;

    @Test
    public void uploadJavaTest() throws ValidateException {
        // arrange
        MultipartFile file = new MockMultipartFile(DATA, "filename.java", TEXT_PLAIN, BYTES);
        Long investigatorId = 1L;
        commonMocks();

        // act
        String message = fileService.upload(file, investigatorId);

        // assert
        Assert.assertNotNull(message);
    }

    @Test(expected=ValidateException.class)
    public void uploadNotValidExtensionTest() throws ValidateException {
        // arrange
        MultipartFile file = new MockMultipartFile(DATA, "filename.txt", TEXT_PLAIN, BYTES);
        Long investigatorId = 1L;

        // act
        fileService.upload(file, investigatorId);
    }

    private void commonMocks() throws ValidateException{
        Mockito.when(investigatorService.validateAndFind(Mockito.anyLong())).thenReturn(new Investigator());
        Mockito.when(randomUtilities.generateIdentifier()).thenReturn("abcabc");
        Mockito.when(fileUtilities.buildBasePath(Mockito.anyString(), Mockito.anyString())).thenReturn("path");
        Mockito.when(processingRequestService.create(Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class),
                Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(new ProcessingRequest());
    }

    @Test
    public void uploadPythonTest() throws ValidateException {
        // arrange
        MultipartFile file = new MockMultipartFile(DATA, "filename.py", TEXT_PLAIN, BYTES);
        Long investigatorId = 1L;
        commonMocks();

        // act
        String message = fileService.upload(file, investigatorId);

        // assert
        Assert.assertNotNull(message);
    }

    @Test
    public void uploadRTest() throws ValidateException {
        // arrange
        MultipartFile file = new MockMultipartFile(DATA, "filename.r", TEXT_PLAIN, BYTES);
        Long investigatorId = 1L;
        commonMocks();

        // act
        String message = fileService.upload(file, investigatorId);

        // assert
        Assert.assertNotNull(message);
    }
}
