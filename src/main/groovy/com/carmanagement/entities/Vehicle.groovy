package com.carmanagement.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "vehicles")
class Vehicle {

    @Id
    String id


    String registerNumber


    Float price


    String type


    Integer kilometers

    @DBRef
    User user

    String toString() {
        return registerNumber
    }
}
