package com.example.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(val id: Int, val name: String, val age: Int) {
    companion object {
        fun fromRow(row: ResultRow): User {
            return User(
                id = row[Users.id].value,
                name = row[Users.name],
                age = row[Users.age]
            )
        }
    }
}

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val age = integer("age")
    // Add other fields as needed
}
