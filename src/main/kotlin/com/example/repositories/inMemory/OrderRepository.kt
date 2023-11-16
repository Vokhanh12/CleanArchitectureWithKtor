package com.example.repositories.inMemory

import com.example.entities.Order
import com.example.repositories.interfaces.IOrderRepository

class OrderRepository : BaseRepository<Order>() , IOrderRepository