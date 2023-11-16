package com.example.entities

abstract class Entity(val id: String)

class Order(id: String) : Entity(id) {
    var name: String = ""
}