package com.example.repositories.inMemory

import com.example.entities.Order
import com.example.repositories.interfaces.IOrderRepository
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Orders : IntIdTable() {
    val name = varchar("name", 255)
}

class OrderRepository : BaseRepository<Order>() , IOrderRepository {

    override suspend fun getAll(): List<Order> {
        return transaction {
            Orders.selectAll().map { rowToOrder(it) }
        }
    }

    override suspend fun findById(id: String): Order? {
        return transaction {
            Orders.select { Orders.id eq id.toInt() }.singleOrNull()?.let { rowToOrder(it) }
        }
    }

    private fun rowToOrder(row: ResultRow): Order {
        return Order(row[Orders.id].value.toString()).apply {
            name = row[Orders.name]
        }
    }
}