package co.edu.itm.clinicaldata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "co.edu.itm.clinicaldata" })
public class ClinicalData {

    public static void main(String[] args) {
        SpringApplication.run(ClinicalData.class, args);
    }
}