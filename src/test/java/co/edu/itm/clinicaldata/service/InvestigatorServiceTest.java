package co.edu.itm.clinicaldata.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.repository.InvestigatorRepository;

@RunWith(MockitoJUnitRunner.class)
public class InvestigatorServiceTest {

    @Mock
    InvestigatorRepository investigatorRepository;

    @InjectMocks
    InvestigatorService investigatorService;

    @Test
    public void findByIdTest() throws ValidateException {
        // arrange
        Long id = 1L;
        Mockito.when(investigatorRepository.findOne(Mockito.anyLong())).thenReturn(new Investigator());

        // act
        Investigator investigator = investigatorService.findById(id);

        // assert
        Assert.assertNotNull(investigator);
    }

    @Test
    public void createTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setInvestigatorName("Juan");
        params.setInvestigatorEmail("Juan@gmail.com");

        // act
        String message = investigatorService.create(params);

        // assert
        Assert.assertNotNull(message);
    }

    @Test(expected = ValidateException.class)
    public void createInvalidNameTest() throws ValidateException {
        // arrange
        Params params = new Params();

        // act
        investigatorService.create(params);
    }

    @Test(expected = ValidateException.class)
    public void createInvalidEmailTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setInvestigatorName("Juan");

        // act
        investigatorService.create(params);
    }

    @Test
    public void validateAndFindTest() throws ValidateException {
        // arrange
        Long investigatorId = 1L;
        Mockito.when(investigatorRepository.findOne(Mockito.anyLong())).thenReturn(new Investigator());

        // act
        Investigator investigator = investigatorService.validateAndfind(investigatorId);

        // assert
        Assert.assertNotNull(investigator);
    }

    @Test(expected = ValidateException.class)
    public void validateAndFindInvestigatorIdNullTest() throws ValidateException {
        // arrange
        Long investigatorId = null;

        // act
        investigatorService.validateAndfind(investigatorId);
    }

    @Test(expected = ValidateException.class)
    public void validateAndFindNotFoundTest() throws ValidateException {
        // arrange
        Long investigatorId = 1L;
        Mockito.when(investigatorRepository.findOne(Mockito.anyLong())).thenReturn(null);

        // act
        investigatorService.validateAndfind(investigatorId);
    }

    @Test
    public void updateTest() throws ValidateException {
        // arrange
        Investigator investigator = new Investigator();

        // act
        investigatorService.update(investigator);

        // assert
        Assert.assertNotNull(investigator);
    }

    @Test
    public void inactivateTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setInvestigatorId(1L);
        Mockito.when(investigatorRepository.findOne(Mockito.anyLong())).thenReturn(new Investigator());

        // act
        String message = investigatorService.inactivate(params);

        // assert
        Assert.assertNotNull(message);
    }

    @Test
    public void activateTest() throws ValidateException {
        // arrange
        Params params = new Params();
        params.setInvestigatorId(1L);
        Mockito.when(investigatorRepository.findOne(Mockito.anyLong())).thenReturn(new Investigator());

        // act
        String message = investigatorService.activate(params);

        // assert
        Assert.assertNotNull(message);
    }
}
