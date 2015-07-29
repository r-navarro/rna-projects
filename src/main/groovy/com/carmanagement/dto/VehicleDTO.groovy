package com.carmanagement.dto

import com.carmanagement.entities.Vehicle


class VehicleDTO {

    Long id

    String registerNumber

    Float price

    String type

    Integer kilometers

    VehicleDTO() {
    }

    VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.id
        this.registerNumber = vehicle.registerNumber
        this.price = vehicle.price
        this.type = vehicle.type
        this.kilometers = vehicle.kilometers
    }

    Vehicle toVehicle() {
        def vehicle = new Vehicle()
        vehicle.id = this.id
        vehicle.registerNumber = this.registerNumber
        vehicle.price = this.price
        vehicle.type = this.type
        vehicle.kilometers = this.kilometers

        return vehicle
    }

    boolean asBoolean() {
        return registerNumber && price && type && kilometers
    }
}
