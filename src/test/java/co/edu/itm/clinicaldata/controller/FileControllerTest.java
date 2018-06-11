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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.service.FileService;

@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {

    private static final String DATA = "data";
    private static final byte[] BYTES = "some xml".getBytes();
    private static final String TEXT_PLAIN = "text/plain";

    @InjectMocks
    FileController fileController;

    @Mock
    FileService fileService;

    @Test
    public void uploadTest() throws ValidateException {
        // arrange
        MultipartFile file = new MockMultipartFile(DATA, "filename.java", TEXT_PLAIN, BYTES);
        Long investigatorId = 1L;
        String returnMessage = "Archivo cargado con éxito";
        Mockito.when(fileService.upload(Mockito.any(), Mockito.anyLong())).thenReturn(returnMessage);

        // act
        ResponseEntity<String> message = fileController.upload(file, investigatorId);

        // assert
        Assert.assertNotNull(message);
        Assert.assertEquals(message.getBody(), returnMessage);
        Assert.assertEquals(OK, message.getStatusCode());
    }

}
