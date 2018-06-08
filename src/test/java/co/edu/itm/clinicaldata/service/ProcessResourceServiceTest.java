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

import co.edu.itm.clinicaldata.component.FileUtilities;
import co.edu.itm.clinicaldata.dto.Resource;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.ProcessResource;
import co.edu.itm.clinicaldata.model.ProcessingRequest;
import co.edu.itm.clinicaldata.repository.ProcessResourceRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProcessResourceServiceTest {

    @Mock
    ProcessResourceRepository processResourceRepository;

    @Mock
    FileUtilities fileUtilities;

    @InjectMocks
    ProcessResourceService processResourceService;

    @Test
    public void createTest() throws ValidateException {
        // arrange
        Resource resource = new Resource();
        String resourceName = "resource.jar";
        resource.setName(resourceName);
        ProcessingRequest processingRequest = new ProcessingRequest();

        // act
        ProcessResource processResource = processResourceService.create(
                resource, processingRequest);

        // assert
        Assert.assertNotNull(processResource);
        Assert.assertEquals(processResource.getName(), resourceName);
    }

    @Test(expected=ValidateException.class)
    public void createWithExceptionTest() throws ValidateException {
        // arrange
        Resource resource = new Resource();
        ProcessingRequest processingRequest = new ProcessingRequest();

        // act
        processResourceService.create(resource, processingRequest);
    }

    @Test
    public void findByProcessingRequestIdTest() throws ValidateException {
        // arrange
        Long processingRequestId = 1L;
        Mockito.when(processResourceRepository.findByProcessingRequestId(Mockito.anyLong())).thenReturn(new ArrayList<>());

        // act
        List<ProcessResource> list = processResourceService.findByProcessingRequestId(processingRequestId);

        // assert
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), 0);
    }

    @Test
    public void validateRequiredResourcesTest() throws ValidateException {
        // arrange
        List<Resource> resources = new ArrayList<>();
        Resource resource = new Resource();
        resource.setName("resource.jar");
        resources.add(resource);
        ProcessingRequest processingRequest = new ProcessingRequest();
        Mockito.when(fileUtilities.existsFile(Mockito.anyString())).thenReturn(Boolean.TRUE);

        // act
        List<ProcessResource> list = processResourceService.validateRequiredResources(resources, processingRequest);

        // assert
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void validateRequiredResourcesEmptyResourcesTest() throws ValidateException {
        // arrange
        List<Resource> resources = new ArrayList<>();
        ProcessingRequest processingRequest = new ProcessingRequest();
        Mockito.when(fileUtilities.existsFile(Mockito.anyString())).thenReturn(Boolean.TRUE);

        // act
        List<ProcessResource> list = processResourceService.validateRequiredResources(resources, processingRequest);

        // assert
        Assert.assertNotNull(list);
        Assert.assertEquals(list.size(), 0);
    }

    @Test(expected=ValidateException.class)
    public void validateRequiredResourcesWithotNameResourceTest() throws ValidateException {
        // arrange
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource());
        ProcessingRequest processingRequest = new ProcessingRequest();

        // act
        processResourceService.validateRequiredResources(resources, processingRequest);
    }

    @Test(expected=ValidateException.class)
    public void validateRequiredResourcesNotConfiguredTest() throws ValidateException {
        // arrange
        List<Resource> resources = new ArrayList<>();
        Resource resource = new Resource();
        resource.setName("resource.jar");
        resources.add(resource);
        ProcessingRequest processingRequest = new ProcessingRequest();
        Mockito.when(fileUtilities.existsFile(Mockito.anyString())).thenReturn(Boolean.FALSE);

        // act
        processResourceService.validateRequiredResources(resources, processingRequest);
    }

}
