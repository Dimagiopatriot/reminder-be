package com.reminder.infrastructure.mysql

import com.reminder.core.model.user.User
import com.reminder.core.model.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException


@Service(value = "userService")
@Repository
class UserMySqlRepository : UserRepository, UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userDaoRepository.findOne(
            Example.of(
                UserDao(
                    userName = username
                )
            )
        )
        return if (user.isPresent) {
            org.springframework.security.core.userdetails.User(
                user.get().userName,
                user.get().password,
                getAuthority()
            )
        } else {
            throw UsernameNotFoundException("Invalid username or password.")
        }
    }

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

    private fun getAuthority(): List<SimpleGrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }
}

@Repository
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