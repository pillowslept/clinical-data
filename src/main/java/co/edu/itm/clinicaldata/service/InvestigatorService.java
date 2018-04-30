package co.edu.itm.clinicaldata.service;

import java.util.List;

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

    @Autowired
    private InvestigatorRepository investigatorRepository;

    public Investigator findById(Long id) {
        return investigatorRepository.findOne(id);
    }

    public Investigator findByName(String name) {
        return investigatorRepository.findByName(name);
    }

    public Investigator create(Params params) throws ValidateException {
        validateCreate(params);
        Investigator investigator = new Investigator();
        investigator.setName(params.getInvestigatorName());
        investigator.setState(InvestigatorState.ACTIVE.getState());
        investigatorRepository.save(investigator);
        return investigator;
    }

    private void validateCreate(Params params) throws ValidateException {
        if(Validations.field(params.getInvestigatorName())){
            throw new ValidateException("El campo <investigatorName> no es válido");
        }
    }

    public String inactivate(Params params) throws ValidateException {
        validateFields(params);
        Investigator investigator = validateAndfind(params.getInvestigatorId());
        investigator.setState(InvestigatorState.INACTIVE.getState());
        investigatorRepository.save(investigator);
        return String.format("El investigador con identificador <%d> fue inactivado con éxito", params.getInvestigatorId());
    }

    public String activate(Params params) throws ValidateException {
        validateFields(params);
        Investigator investigator = validateAndfind(params.getInvestigatorId());
        investigator.setState(InvestigatorState.ACTIVE.getState());
        investigatorRepository.save(investigator);
        return String.format("El investigador con identificador <%d> fue activado con éxito",
                params.getInvestigatorId());
    }

    public Investigator validateAndfind(Long investigatorId) throws ValidateException {
        Investigator investigator = findById(investigatorId);
        if(investigator == null){
            throw new ValidateException(String.format("El investigador con identificador <%d> no existe en la base de datos",
                    investigatorId));
        }
        return investigator;
    }

    private void validateFields(Params params) throws ValidateException {
        if(Validations.field(params.getInvestigatorId())){
            throw new ValidateException("El campo <investigatorId> no es válido");
        }
    }

    public void update(Investigator investigator) {
        investigatorRepository.save(investigator);
    }

    public void deleteUserById(Long id) {
        investigatorRepository.delete(id);
    }

    public void deleteAllUsers() {
        investigatorRepository.deleteAll();
    }

    public List<Investigator> findAll() {
        return investigatorRepository.findAll();
    }

    public boolean exist(Investigator investigator) {
        return findByName(investigator.getName()) != null;
    }

}
