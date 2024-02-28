package com.youdev.SpringGrapQl.dao;


import com.youdev.SpringGrapQl.entity.Etudiant;
import org.springframework.data.repository.CrudRepository;

public interface EtudiantRepository extends CrudRepository<Etudiant,Integer>{

	Etudiant findByEmail(String email);

}
