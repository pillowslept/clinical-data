package co.edu.itm.clinicaldata.util;

import java.util.List;

public class Validations {

    public static boolean field(String field) {
        return field == null || field.isEmpty();
    }

    public static boolean field(Long field) {
        return field == null || field <= 0;
    }

    public static boolean field(List<?> list) {
        return list == null || list.isEmpty();
    }

}
