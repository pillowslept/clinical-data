package co.edu.itm.clinicaldata.model;

public enum ProcessState {

    CREATED("CREADA"),
    PROCESSING("EN PROGRESO"),
    FINISHED_OK("FINALIZADA"),
    FINISHED_WITH_ERRORS("FINALIZADA CON ERRORES");

    private String state;

    ProcessState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
