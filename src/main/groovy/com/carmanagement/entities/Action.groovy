package com.carmanagement.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "actions")
class Action {

    @Id
    String id

    Date date

    Float distance

    Float cost

    @DBRef
    Vehicle vehicle

    boolean asBoolean() {
        return date && (cost != null) && (distance != null)
    }
}
