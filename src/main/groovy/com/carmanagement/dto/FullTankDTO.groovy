package com.carmanagement.dto

import com.carmanagement.entities.FullTank

class FullTankDTO {

    Long id

    Date date

    Float distance

    Float cost

    Float quantity

    FullTankDTO() {
    }

    FullTankDTO(FullTank fullTank) {
        this.id = fullTank.id
        this.date = fullTank.date
        this.distance = fullTank.distance
        this.cost = fullTank.cost
        this.quantity = fullTank.quantity
    }

    FullTank toFullTank() {
        def fullTank = new FullTank()
        fullTank.id = this.id
        fullTank.date = this.date
        fullTank.distance = this.distance
        fullTank.cost = this.cost
        fullTank.quantity = this.quantity
    }

    boolean asBoolean(){
        return date && cost && distance && quantity
    }
}
