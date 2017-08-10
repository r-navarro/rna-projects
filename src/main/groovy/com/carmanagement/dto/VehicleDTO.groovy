package com.carmanagement.dto

import com.carmanagement.entities.Vehicle


class VehicleDTO {

    String id

    String registrationNumber

    Float price

    String type

    Integer kilometers

    VehicleDTO() {
    }

    VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.id
        this.registrationNumber = vehicle.registrationNumber
        this.price = vehicle.price
        this.type = vehicle.type
        this.kilometers = vehicle.kilometers
    }

    Vehicle toVehicle() {
        def vehicle = new Vehicle()
        vehicle.id = this.id
        vehicle.registrationNumber = this.registrationNumber
        vehicle.price = this.price
        vehicle.type = this.type
        vehicle.kilometers = this.kilometers

        return vehicle
    }

    boolean asBoolean() {
        return registrationNumber && price && type && kilometers
    }
}
