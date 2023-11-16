package com.example.controllers

import com.example.commands.*
import com.example.dtos.extensions.toDto
import com.example.dtos.requests.CreateOrderDto
import com.example.dtos.requests.UpdateOrderDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.repositories.inMemory.OrderRepository
import com.example.services.OrderService

// must be handled with dependency injection
var repository = OrderRepository()
var service = OrderService(repository)

fun Route.addOrderRoutes () {

    route("api/v1/orders") {
        get {
            val command = GetAllOrdersCommand()
            val orders = service.getAllOrders(command)
            val ordersDto = orders.map { it.toDto() }
            call.respond(ordersDto)
        }

        get("{id}") {
            val id = call.parameters["id"]
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest)
            }

            val command = GetOrderByIdCommand(id!!)
            val order = service.getOrderById(command)
            if(order == null) {
                call.respond(HttpStatusCode.NotFound)
            }

            val orderDto = order!!.toDto()
            call.respond(orderDto)
        }

        post {
            val dto = call.receive<CreateOrderDto>()
            val command = CreateOrderCommand(dto.name)
            val order = service.createOrder(command)
            val orderDto = order.toDto()

            call.respond(HttpStatusCode.Created, orderDto)
        }

        put("{id}") {
            val id = call.parameters["id"]
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest)
            }

            val dto = call.receive<UpdateOrderDto>()
            val command = UpdateOrderCommand(id!!, dto.name)
            var order = service.updateOrder(command)

            if(order == null) {
                call.respond(HttpStatusCode.NotFound)
            }
            val orderDto = order!!.toDto()

            call.respond(orderDto)
        }

        delete ("{id}") {
            val id = call.parameters["id"]
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest)
            }

            val command = GetOrderByIdCommand(id!!)
            val order = service.getOrderById(command)
            if(order == null) {
                call.respond(HttpStatusCode.NotFound)
            }

            val deleteCommand = DeleteOrderCommand(id)
            service.deleteOrder(deleteCommand)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}