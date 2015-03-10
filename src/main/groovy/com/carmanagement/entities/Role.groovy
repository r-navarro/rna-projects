package com.carmanagement.entities

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class Role {

	@Id
	@GeneratedValue
	Long id
	
	@Column
	String authority

}
