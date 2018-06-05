package co.edu.itm.clinicaldata.component;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class RandomUtilities {

    public String generateIdentifier() {
        return UUID.randomUUID().toString();
    }

}
