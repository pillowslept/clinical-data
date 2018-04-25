package co.edu.itm.clinicaldata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.itm.clinicaldata.dto.Params;
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

    public Investigator create(Params params) {
        Investigator investigator = new Investigator();
        investigator.setName(params.getUserName());
        investigator.setState("A");
        investigatorRepository.save(investigator);
        return investigator;
    }

    public String inactive(Params params) throws ValidateException {
        if(Validations.field(params.getUserId())){
            throw new ValidateException("El campo <userId> no es válido");
        }
        Investigator investigator = findById(params.getUserId());
        if(investigator == null){
            throw new ValidateException(String.format("El investigador con identificador <%d> no existe en la base de datos", params.getUserId()));
        }
        investigator.setState("I");
        investigatorRepository.save(investigator);
        return String.format("El investigador con identificador <%d> fue inactivado con éxito", params.getUserId());
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
