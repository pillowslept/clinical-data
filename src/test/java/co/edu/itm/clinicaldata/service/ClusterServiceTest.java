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

import co.edu.itm.clinicaldata.component.Commands;
import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.enums.Language;
import co.edu.itm.clinicaldata.enums.ProcessState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;

@RunWith(MockitoJUnitRunner.class)
public class ClusterServiceTest {

    @Mock
    ProcessingRequestService processingRequestService;

    @Mock
    Commands commands;

    @Mock
    FileUtilities fileUtilities;

    @InjectMocks
    ClusterService clusterService;

    @Test
    public void sendProcessToClusterLanguageRTest() throws ValidateException {
        // arrange
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setLanguage(Language.R.getName());
        List<ProcessResource> listProcessResource = new ArrayList<>();
        normalMocks();

        // act
        clusterService.sendProcessToCluster(processingRequest, listProcessResource);

        // assert
        Assert.assertNotNull(processingRequest);
        Assert.assertEquals(processingRequest.getState(), ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
    }

    private void normalMocks() {
        Mockito.when(fileUtilities.templateLanguageFolder(Mockito.anyString())).thenReturn("template/folder");
        Mockito.when(fileUtilities.readFile(Mockito.anyString())).thenReturn("Readed content");
        Mockito.when(fileUtilities.isLinux()).thenReturn(Boolean.FALSE);
    }

    @Test
    public void sendProcessToClusterLanguagePythonTest() throws ValidateException {
        // arrange
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setLanguage(Language.PYTHON.getName());
        List<ProcessResource> listProcessResource = new ArrayList<>();
        normalMocks();

        // act
        clusterService.sendProcessToCluster(processingRequest, listProcessResource);

        // assert
        Assert.assertNotNull(processingRequest);
        Assert.assertEquals(processingRequest.getState(), ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
    }

    @Test
    public void sendProcessToClusterLanguagePythonFailTest() throws ValidateException {
        // arrange
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setLanguage(Language.PYTHON.getName());
        List<ProcessResource> listProcessResource = new ArrayList<>();
        normalMocks();
        Mockito.doThrow(ValidateException.class).when(fileUtilities).createFile(Mockito.any(), Mockito.anyString());

        // act
        clusterService.sendProcessToCluster(processingRequest, listProcessResource);

        // assert
        Assert.assertNotNull(processingRequest);
        Assert.assertEquals(processingRequest.getState(), ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
    }

    @Test
    public void sendProcessToClusterLanguageNotValidTest() throws ValidateException {
        // arrange
        ProcessingRequest processingRequest = new ProcessingRequest();
        processingRequest.setLanguage("pdf");
        List<ProcessResource> listProcessResource = new ArrayList<>();

        // act
        clusterService.sendProcessToCluster(processingRequest, listProcessResource);

        // assert
        Assert.assertNotNull(processingRequest);
        Assert.assertEquals(processingRequest.getState(), ProcessState.FINISHED_WITHOUT_ACTIONS.getState());
    }

}
