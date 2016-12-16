package cw3.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cw3.domain.*;
import cw3.repository.ClassRepository;

@Service
public class ClassService {

	@Autowired
	private ClassRepository classRepository;
	
	public Object findAllInstances(){
		return classRepository.findAll();
	}
	
	
	public Instance findById(Integer i){
		return classRepository.findOne(i);
	}
	
	public void deleteById(Integer id){
		classRepository.delete(id);;
	}
	
	
	public void save(Instance a){
		classRepository.save(a);
	}
}
