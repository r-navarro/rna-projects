package com.carmanagement.dto

import com.carmanagement.entities.Maintenance

class MaintenanceDTO {

    Long id

    Date date

    Float distance

    Float cost

    Date predictedDate

    String description

    MaintenanceDTO() {
    }

    MaintenanceDTO(Maintenance maintenance) {
        id = maintenance.id
        cost = maintenance.cost
        description = maintenance.description
        predictedDate = maintenance.predictedDate
        date = maintenance.date
        distance = maintenance.distance
    }

    Maintenance toMaintenance() {
        return new Maintenance(id: id, distance: distance, date: date, cost: cost, predictedDate: predictedDate, description: description)
    }

    boolean asBoolean(){
        return date && (cost != null)
    }
}
