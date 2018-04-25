package co.edu.itm.clinicaldata.enums;

public enum InvestigatorState {

    ACTIVE("ACTIVO"),
    INACTIVE("INACTIVO");

    private String state;

    InvestigatorState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
