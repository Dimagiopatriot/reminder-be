package com.reminder.infrastructure.mysql

class NoSuchElementInDbException: Exception("No such element in database")

class CustomSQLException(override val message: String?): Exception()