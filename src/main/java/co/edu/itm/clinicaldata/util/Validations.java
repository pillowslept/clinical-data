package co.edu.itm.clinicaldata.util;

public class Validations {

	public static boolean field(String field){
		return field == null || field.isEmpty();
	}

	public static boolean field(Long field){
        return field == null || field <= 0;
    }
}
