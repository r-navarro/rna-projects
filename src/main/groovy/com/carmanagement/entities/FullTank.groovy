package com.carmanagement.entities

class FullTank extends Action {

    Float quantity

    boolean asBoolean() {
        return super.asBoolean() && quantity
    }
}
