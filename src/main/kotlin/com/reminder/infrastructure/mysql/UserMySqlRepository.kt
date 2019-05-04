package com.reminder.infrastructure.mysql

import com.reminder.core.model.user.User
import com.reminder.core.model.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Repository
class UserMySqlRepository : UserRepository {

    @Autowired
    private lateinit var userDaoRepository: UserDaoRepository

    override fun getUser(email: String, password: String): User {
        val userDao = userDaoRepository.findOne(
            Example.of(
                UserDao(
                    email = email,
                    password = password
                )
            )
        )
        return if (userDao.isPresent) userDao
            .map {
                User(it.userName!!, it.email!!, it.password!!)
            }.get() else throw NoSuchElementInDbException()
    }

    override fun createUser(user: User): User {
        val userDao = userDaoRepository.save(
            UserDao(
                userName = user.userName,
                email = user.email,
                password = user.password
            )
        )
        return User(userDao.userName!!, userDao.email!!, userDao.password!!)
    }
}

interface UserDaoRepository : JpaRepository<UserDao, Long>

@Table(name = "user_table")
@Entity
data class UserDao(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val userName: String? = null,
    val email: String? = null,
    val password: String? = null
)