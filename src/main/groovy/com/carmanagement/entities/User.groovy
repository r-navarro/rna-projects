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

    @OneToMany
    List<Vehicle> vehicles

}
