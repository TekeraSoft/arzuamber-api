package com.tekerasoft.arzuamber.model

import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
data class User @JvmOverloads constructor(
    val firstName: String,
    val lastName: String,
    val email: String,
    var hashedPassword: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<Role>? = mutableSetOf(Role.USER),
    var phoneNumber: String? = null,
    var address: String? = null,

    @ElementCollection
    var favProducts: List<String>? = null,

    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
    val birthDate: LocalDate? = null,
    var lastLogin: LocalDateTime? = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "uuid")
    val id: UUID? = null
): UserDetails {

    override fun getAuthorities()= this.roles

    override fun getPassword() = this.hashedPassword

    override fun getUsername() = this.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
