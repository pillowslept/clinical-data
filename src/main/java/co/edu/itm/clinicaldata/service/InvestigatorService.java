package co.edu.itm.clinicaldata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.dto.Params;
import co.edu.itm.clinicaldata.enums.InvestigatorState;
import co.edu.itm.clinicaldata.exception.ValidateException;
import co.edu.itm.clinicaldata.model.Investigator;
import co.edu.itm.clinicaldata.repository.InvestigatorRepository;
import co.edu.itm.clinicaldata.util.Validations;

@Service
@Transactional
public class InvestigatorService {

    private static final String INVESTIGATOR_CREATED = "El investigador con nombre <%s> ha sido creado con éxito, el identificador generado es <%d>";
    private static final String INVESTIGATOR_NOT_FOUND = "El investigador con identificador <%d> no existe en la base de datos";
    private static final String INVESTIGATOR_ACTIVATED = "El investigador con identificador <%d> fue activado con éxito";
    private static final String INVESTIGATOR_INACTIVATED = "El investigador con identificador <%d> fue inactivado con éxito";
    private static final String INVESTIGATOR_EMAIL_NOT_VALID = "El campo <investigatorEmail> no es válido";
    private static final String INVESTIGATOR_NAME_NOT_VALID = "El campo <investigatorName> no es válido";
    private static final String INVESTIGATOR_ID_NOT_VALID = "El campo <investigatorId> no es válido";

    @Autowired
    private InvestigatorRepository investigatorRepository;

    public Investigator findById(Long id) {
        return investigatorRepository.findOne(id);
    }

    public String create(Params params) throws ValidateException {
        validateCreate(params);
        Investigator investigator = new Investigator();
        investigator.setName(params.getInvestigatorName());
        investigator.setEmail(params.getInvestigatorEmail());
        investigator.setState(InvestigatorState.ACTIVE.getState());
        investigatorRepository.save(investigator);
        return String.format(INVESTIGATOR_CREATED, investigator.getName(), investigator.getId());
    }

    private void validateCreate(Params params) throws ValidateException {
        if(Validations.field(params.getInvestigatorName())){
            throw new ValidateException(INVESTIGATOR_NAME_NOT_VALID);
        }
        if(Validations.field(params.getInvestigatorEmail())){
            throw new ValidateException(INVESTIGATOR_EMAIL_NOT_VALID);
        }
    }

    public String inactivate(Params params) throws ValidateException {
        Investigator investigator = validateAndfind(params.getInvestigatorId());
        investigator.setState(InvestigatorState.INACTIVE.getState());
        update(investigator);
        return String.format(INVESTIGATOR_INACTIVATED, params.getInvestigatorId());
    }

    public String activate(Params params) throws ValidateException {
        Investigator investigator = validateAndfind(params.getInvestigatorId());
        investigator.setState(InvestigatorState.ACTIVE.getState());
        update(investigator);
        return String.format(INVESTIGATOR_ACTIVATED, params.getInvestigatorId());
    }

    public Investigator validateAndfind(Long investigatorId) throws ValidateException {
        validateInvestigatorId(investigatorId);
        Investigator investigator = findById(investigatorId);
        if(investigator == null){
            throw new ValidateException(String.format(INVESTIGATOR_NOT_FOUND, investigatorId));
        }
        return investigator;
    }

    private void validateInvestigatorId(Long investigatorId) throws ValidateException {
        if(Validations.field(investigatorId)){
            throw new ValidateException(INVESTIGATOR_ID_NOT_VALID);
        }
    }

    public void update(Investigator investigator) {
        investigatorRepository.save(investigator);
    }

}
