package com.carmanagement.entities

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
class UserRole {

	@Id
	@GeneratedValue
	Long id
	
	@OneToOne
	User user
	
	@OneToOne
	Role role

}
