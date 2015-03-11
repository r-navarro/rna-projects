package com.carmanagement.entities

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
class Action {

	@Id
	@GeneratedValue
	Long id
	
	@Column(name="date_action")
	Date date
	
	@Column
	Float mileage
	
	@Column
	Float cost
	
	@ManyToOne
	Vehicle vehicle

}
