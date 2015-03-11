package com.carmanagement.entities

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
class Vehicle {

	@Id
	@GeneratedValue
	Long id
	
	@Column
    String registerNumber
	
	@Column
    Float price
	
	@Column
    String type
	
	@Column
    Integer kilometers
	
	@OneToMany
    List<Action> actions
	
	@ManyToOne
    User user


    def String toString() {
	return registerNumber
    }
}
