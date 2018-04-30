package co.edu.itm.clinicaldata.util;

import java.util.UUID;

public class RandomUtilities {

    public static String randomIdentifier() {
        return UUID.randomUUID().toString();
    }

}
