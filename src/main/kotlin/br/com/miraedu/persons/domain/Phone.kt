package br.com.miraedu.persons.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "phone")
@EntityListeners(AuditingEntityListener::class)
@Audited(withModifiedFlag = true)
data class Phone(
        @Column(name = "area_code")
        val areaCode: Int,

        val number: Int,

        @Enumerated(EnumType.STRING)
        val type: PhoneType
) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    lateinit var id: UUID

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    lateinit var person: Person
}

