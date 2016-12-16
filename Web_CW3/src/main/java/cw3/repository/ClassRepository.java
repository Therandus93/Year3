package cw3.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cw3.domain.*;

@Repository
public interface ClassRepository extends CrudRepository<Instance,Integer> {

}

