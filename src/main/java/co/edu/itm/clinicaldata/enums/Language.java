package co.edu.itm.clinicaldata.model;

public enum Language {
	
    JAVA("java", "java"),
    PYTHON("python", "py"),
    R("r", "r");

    private String fileExtension;
    private String name;

    Language(String name, String fileExtension) {
        this.name = name;
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getName() {
        return name;
    }

}
