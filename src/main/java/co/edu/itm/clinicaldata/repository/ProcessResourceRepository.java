package co.edu.itm.clinicaldata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.itm.clinicaldata.model.ProcessResource;

@Repository
public interface ProcessResourceRepository extends JpaRepository<ProcessResource, Long> {

    List<ProcessResource> findByProcessingRequestId(Long processingRequestId);

}
