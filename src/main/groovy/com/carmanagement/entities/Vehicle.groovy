package com.carmanagement.entities

import javax.persistence.*

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
	@JoinColumn(nullable = false)
    User user


    def String toString() {
	return registerNumber
    }
}
