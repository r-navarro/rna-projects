package com.carmanagement.entities

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
class User {

	@Id
	@GeneratedValue
	Long id
	
	@Column
    String username
	
	@Column
    String password
	
	@Column
    boolean enabled = true
	
	@Column
    boolean accountExpired
	
	@Column
    boolean accountLocked
	
	@Column
    boolean passwordExpired
	
	@OneToMany
    List<Vehicle>  vehicles

}
