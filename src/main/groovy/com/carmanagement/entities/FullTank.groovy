package com.carmanagement.entities

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class FullTank extends Action {

    @Column
    Float quantity

    boolean asBoolean() {
        return super.asBoolean() && quantity
    }
}
