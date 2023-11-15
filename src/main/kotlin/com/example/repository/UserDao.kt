package com.example.repository

import com.example.model.User
import com.example.model.Users
import com.example.plugins.ExposedUser
import com.example.plugins.UserService
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserDao {


}