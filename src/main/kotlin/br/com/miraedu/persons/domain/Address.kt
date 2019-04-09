package br.com.miraedu.persons.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Audited(withModifiedFlag = true)
@Table(name = "address")
data class Address(
        @Column(name = "public_place")
        val publicPlace: String,

        val number: String,

        val complement: String,

        val neighborhood: String,

        val city: String,

        @Enumerated(EnumType.STRING)
        val state: States
) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID? = null

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    var person: Person? = null
}