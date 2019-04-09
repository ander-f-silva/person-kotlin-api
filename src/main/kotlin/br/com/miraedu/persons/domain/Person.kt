package br.com.miraedu.persons.domain

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode.SUBSELECT
import org.hibernate.annotations.GenericGenerator
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.MERGE
import javax.persistence.CascadeType.PERSIST
import javax.persistence.FetchType.EAGER

@Entity
@EntityListeners(AuditingEntityListener::class)
@Audited(withModifiedFlag = true)
@Table(name = "person")
data class Person(
        val name: String,

        @Column(name = "last_name")
        val lastName: String,

        val document: String,

        @Column(name = "birth_date")
        val birthDate: LocalDate,

        @OneToOne(cascade = [PERSIST, MERGE], optional = false, mappedBy = "person")
        val address: Address,

        @OneToMany(cascade = [PERSIST, MERGE], mappedBy = "person", fetch = EAGER)
        @Fetch(SUBSELECT)
        val phones: List<Phone>,

        @OneToMany(cascade = [PERSIST, MERGE], mappedBy = "person", fetch = EAGER)
        @Fetch(SUBSELECT)
        val emails: List<Email>
) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID? = null

    @Enumerated(EnumType.STRING)
    var active: ActiveStatus

    @CreatedDate
    @Column(name = "created_date")
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "updated_date")
    var updatedDate: LocalDateTime? = null

    init {
        address.person = this
        phones.forEach { it.person = this }
        emails.forEach { it.person = this }
        active = ActiveStatus.Y
    }

    fun disable() {
        active = ActiveStatus.N
    }
}