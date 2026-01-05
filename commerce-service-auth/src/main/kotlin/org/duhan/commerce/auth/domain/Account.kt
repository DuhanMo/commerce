package org.duhan.commerce.auth.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
class Account(
    val email: String?,
    val password: String?,
    val userId: UUID,
    val role: UserRole,
) {
    @Id
    val id: UUID = Generators.timeBasedEpochGenerator().generate()

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.MIN

    @UpdateTimestamp
    val updatedAt: LocalDateTime = LocalDateTime.MIN
}
