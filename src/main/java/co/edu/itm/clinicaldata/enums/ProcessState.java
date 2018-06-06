package co.edu.itm.clinicaldata.enums;

public enum ProcessState {

    CREATED("CREADA"),
    PROCESSING("EN PROGRESO"),
    FINISHED_OK("FINALIZADA"),
    FINISHED_WITHOUT_ACTIONS("FINALIZADA SIN ACCIONES"),
    FINISHED_WITH_ERRORS("FINALIZADA CON ERRORES");

    private String state;

    ProcessState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
