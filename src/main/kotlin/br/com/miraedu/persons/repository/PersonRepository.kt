package br.com.miraedu.persons.repository

import br.com.miraedu.persons.domain.ActiveStatus
import br.com.miraedu.persons.domain.Person
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface PersonRepository : JpaRepository<Person, UUID>, JpaSpecificationExecutor<Person> {
    fun findByActive(active: ActiveStatus, pageable: Pageable): List<Person>

    fun existsByDocumentAndActive(document: String, active: ActiveStatus): Boolean

    fun existsByIdAndActive(id: UUID, active: ActiveStatus): Boolean

    fun findByActiveAndDocumentIn(active: ActiveStatus, documents: List<String>): List<Person>

    fun findByIdAndActive(id: UUID, active: ActiveStatus): Person

    fun findByDocumentAndActive(document: String, active: ActiveStatus): Optional<Person>
}