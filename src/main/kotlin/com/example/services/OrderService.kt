package com.example.services

import com.example.commands.*
import com.example.entities.Order
import com.example.repositories.interfaces.*
class OrderService(private val orderRepository: IOrderRepository) : IOrderService {


    override suspend fun getAllOrders(command: GetAllOrdersCommand): List<Order> {
        return orderRepository.getAll()
    }

    override suspend fun getOrderById(command: GetOrderByIdCommand): Order? {
        return orderRepository.findById(command.orderId)
    }

    override suspend fun createOrder(command: CreateOrderCommand): Order {
        val order = command.toEntity()
        orderRepository.insert(order)
        return order
    }

    override suspend fun updateOrder(command: UpdateOrderCommand): Order? {
        val order = orderRepository.findById(command.orderId) ?: return null
        command.updateEntity(order)
        orderRepository.replace(order)
        return order
    }

    override suspend fun deleteOrder(command: DeleteOrderCommand) {
        orderRepository.delete(command.orderId)
    }
}