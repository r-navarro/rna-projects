package com.carmanagement.entities

import javax.persistence.*

@Entity
class Action {

    @Id
    @GeneratedValue
    Long id

    @Column(name = "date_action")
    Date date

    @Column
    Float distance

    @Column
    Float cost

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    Vehicle vehicle

    boolean asBoolean() {
        return date && cost && distance
    }
}
