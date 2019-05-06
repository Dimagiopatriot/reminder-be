package com.reminder.infrastructure.mysql

import com.reminder.core.model.user.User
import com.reminder.core.model.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.util.Base64Utils
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Service(value = "userService")
@Repository
class UserMySqlRepository : UserRepository, UserDetailsService {

    override fun getUserByUserName(userName: String): User {
        val user = userDaoRepository.findOne(Example.of(UserDao(userName = userName))).get()
        return User(
            user.id!!,
            user.userName!!,
            user.email!!,
            Base64Utils.decode(user.password!!).toString(Charsets.UTF_8)
        )
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userDaoRepository.findOne(Example.of(UserDao(userName = username)))

        return if (user.isPresent) {
            org.springframework.security.core.userdetails.User(
                user.get().userName,
                BCryptPasswordEncoder().encode(Base64Utils.decode(user.get().password!!).toString(Charsets.UTF_8)),
                getAuthority()
            )
        } else {
            throw UsernameNotFoundException("Invalid username or password.")
        }
    }

    @Autowired
    private lateinit var userDaoRepository: UserDaoRepository

    @Throws(NoSuchElementInDbException::class)
    override fun getUser(email: String, password: String): User {
        val userDao = userDaoRepository.findOne(
            Example.of(
                UserDao(
                    email = email,
                    password = Base64Utils.encode(password.toByteArray())
                )
            )
        )
        return if (userDao.isPresent) userDao
            .map {
                User(it.id!!, it.userName!!, it.email!!, Base64Utils.decode(it.password!!).toString(Charsets.UTF_8))
            }
            .get() else throw NoSuchElementInDbException()
    }

    @Throws(CustomSQLException::class)
    override fun createUser(userName: String, email: String, password: String): User {
        try {
            val userDao = userDaoRepository.save(
                UserDao(
                    userName = userName,
                    email = email,
                    password = Base64Utils.encode(password.toByteArray())
                )
            )
            return User(
                userDao.id!!,
                userDao.userName!!,
                userDao.email!!,
                Base64Utils.decode(userDao.password!!).toString(Charsets.UTF_8)
            )

        } catch (e: DataAccessException) {
            throw CustomSQLException(e.cause?.cause?.message)
        }
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
    val password: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserDao

        if (id != other.id) return false
        if (userName != other.userName) return false
        if (email != other.email) return false
        if (password != null) {
            if (other.password == null) return false
            if (!password.contentEquals(other.password)) return false
        } else if (other.password != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (userName?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (password?.contentHashCode() ?: 0)
        return result
    }
}