package br.com.miraedu.persons.service

import br.com.miraedu.persons.component.PersonConverter
import br.com.miraedu.persons.component.PersonValidator
import br.com.miraedu.persons.domain.ActiveStatus
import br.com.miraedu.persons.domain.Person
import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.dto.PersonListDto
import br.com.miraedu.persons.exception.NotFoundException
import br.com.miraedu.persons.repository.EmailRepository
import br.com.miraedu.persons.repository.PersonRepository
import br.com.miraedu.persons.repository.PhoneRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.collections.ArrayList


@Service
class PersonService {

    @Autowired
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var personValidator: PersonValidator

    @Autowired
    lateinit var personConverter: PersonConverter

    @Autowired
    lateinit var phoneRepository: PhoneRepository

    @Autowired
    lateinit var emailRepository: EmailRepository

    fun save(personDto: PersonDto): UUID? {
        personValidator.validateRegisteredDocument(personDto.document)
        return personRepository.save(personConverter.toPerson(personDto)).id
    }

    fun save(id: UUID, personDto: PersonDto) {
        notExistPerson(id)

        personValidator.validateRegisteredDocument(id, personDto.document)

        var person = personConverter.toPerson(personDto)
        var personCurrent = personRepository.findByIdAndActive(id, ActiveStatus.Y)

        removePhoneAndEmailByPerson(personCurrent)
        mergePersons(personCurrent, person)

        personRepository.save(person)
    }

    fun saveAll(personsDto: PersonListDto): PersonListDto {
        personValidator.validateRepeatedDocuments(personsDto.persons.map { it -> it.document })
        val persons = personRepository.saveAll(personsDto.persons.map { it -> personConverter.toPerson(it) })

        return generatePersonList(persons)
    }

    fun findById(id: UUID): PersonDto {
        notExistPerson(id)
        return personConverter.toPersonDto(personRepository.findByIdAndActive(id, ActiveStatus.Y))
    }

    fun findAll(page: Int, size: Int): PersonListDto {
        val persons = personRepository.findByActive(ActiveStatus.Y, PageRequest.of(page, size))

        if (persons.isEmpty())
            throw NotFoundException("Persons not found")

        return generatePersonList(persons)
    }

    fun findByNameAndLastNameOrDocument(name: String, lastName: String, document: String): PersonListDto {
        val persons = personRepository.findAll {

            root, query, cb ->

            val predicates = ArrayList<Predicate>()

            predicates.add(cb.equal(root.get<String>("name"), name))
            predicates.add(cb.equal(root.get<String>("lastName"), lastName))

            if (document != null)
                predicates.add(cb.equal(root.get<String>("document"), document))


            val predicate = cb.and(*predicates.toTypedArray())

            predicate
        }

        if (persons.isEmpty())
            throw NotFoundException("Persons not found")


        return PersonListDto(persons.map { it -> personConverter.toPersonDto(it) })
    }

    fun delete(id: UUID) {
        notExistPerson(id)
        var person = personRepository.findByIdAndActive(id, ActiveStatus.Y)

        person.disable()

        personRepository.save(person)
    }

    private fun notExistPerson(id: UUID) {
        val hasPerson = !personRepository.existsByIdAndActive(id, ActiveStatus.Y)

        if (hasPerson)
            throw NotFoundException("Person $id not found")
    }

    private fun removePhoneAndEmailByPerson(personCurrent: Person) {
        phoneRepository.deleteInBatch(personCurrent.phones)
        emailRepository.deleteInBatch(personCurrent.emails)
    }

    private fun mergePersons(personCurrent: Person, person: Person) {
        ModelMapper().map(personCurrent, person)
        ModelMapper().map(personCurrent.address, person.address)
    }

    private fun generatePersonList(persons: List<Person>): PersonListDto = PersonListDto(persons.map { it -> personConverter.toPersonDto(it) })
}
