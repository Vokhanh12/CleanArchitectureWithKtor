package com.example.services

import com.example.commands.*
import com.example.entities.*

interface IOrderService {
    suspend fun getAllOrders(command : GetAllOrdersCommand) : List<Order>
    suspend fun getOrderById(command : GetOrderByIdCommand) : Order?
    suspend fun createOrder(command : CreateOrderCommand) : Order
    suspend fun updateOrder(command : UpdateOrderCommand) : Order?
    suspend fun deleteOrder(command : DeleteOrderCommand)
}


