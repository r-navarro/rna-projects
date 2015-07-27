package com.carmanagement.entities

import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Maintenance extends Action {

    @Column(name = "predicted_date")
    Date predictedDate

    @Column
    String description


}
