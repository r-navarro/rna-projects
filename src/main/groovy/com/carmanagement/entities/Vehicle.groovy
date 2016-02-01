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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vehicle")
    List<Action> actions

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false)
    User user

    void setUser(User user) {
        this.user = user
        user.vehicles << this
    }

    def String toString() {
        return registerNumber
    }

    def leftShift(action) {
        if (action instanceof Action) {
            actions << action
            if (action instanceof FullTank) {
                kilometers += action.distance
            }
            action.vehicle = this
        }
    }

    def rightShift(action) {
        if (action instanceof Action) {
            def removed = actions.remove action
            if (removed && action instanceof FullTank) {
                kilometers -= action.distance
            }
        }
    }
}
