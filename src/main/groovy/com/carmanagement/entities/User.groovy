package com.carmanagement.entities

import javax.persistence.*

@Entity
class User {

    @Id
    @GeneratedValue
    Long id

    @Column(unique = true)
    String name

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    List<Vehicle> vehicles = []

    String toString() {
        return name
    }

}
