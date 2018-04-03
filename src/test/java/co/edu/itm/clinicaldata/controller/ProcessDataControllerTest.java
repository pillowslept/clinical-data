package co.edu.itm.clinicaldata.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import co.edu.itm.clinicaldata.service.ProcessDataService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessDataControllerTest {

	@MockBean
	ProcessDataService processDataService;
	
	@Autowired
	ProcessDataController processDataController;

}
