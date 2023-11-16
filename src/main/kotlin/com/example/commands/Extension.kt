package com.example.commands

import com.example.entities.Order
import java.util.*

fun CreateOrderCommand.toEntity() : Order {
    val uniqueID = UUID.randomUUID().toString()
    val order = Order(uniqueID)
    order.name = this.name
    return order
}

fun UpdateOrderCommand.updateEntity(order: Order) : Order {
    order.name = this.name
    return order
}