package br.com.miraedu.persons.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "email")
@EntityListeners(AuditingEntityListener::class)
@Audited(withModifiedFlag = true)
data class Email(val address: String) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    lateinit var id: UUID

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    lateinit var person: Person
}