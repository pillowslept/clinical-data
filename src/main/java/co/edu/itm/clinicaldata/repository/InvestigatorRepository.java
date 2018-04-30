package co.edu.itm.clinicaldata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.itm.clinicaldata.model.Investigator;

@Repository
public interface InvestigatorRepository extends JpaRepository<Investigator, Long> {

}
