package br.com.miraedu.persons.component

import br.com.miraedu.persons.domain.*
import br.com.miraedu.persons.dto.AddressDto
import br.com.miraedu.persons.dto.EmailDto
import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.dto.PhoneDto
import org.springframework.stereotype.Component

@Component
class PersonConverter {

    fun toPerson(personDto: PersonDto): Person {
        val name = personDto.name
        val lastName = personDto.lastName
        val document = personDto.document
        val birthDate = personDto.birthDate

        val address = Address(
                personDto.address!!.publicPlace,
                personDto.address!!.number,
                personDto.address!!.complement,
                personDto.address!!.neighborhood,
                personDto.address!!.city,
                States.fromText(personDto.address!!.state)
        )

        val emails = personDto.emails!!.map { Email(it.address) }
        val phones = personDto.phones!!.map { Phone(it.areaCode, it.number, PhoneType.fromText(it.type)) }

        return Person(name, lastName, document, birthDate!!, address, phones, emails)
    }

    fun toPersonDto(person: Person): PersonDto {
        val name = person.name
        val lastName = person.lastName
        val document = person.document
        val birthDate = person.birthDate

        val address = AddressDto(
                person.address.publicPlace,
                person.address.number,
                person.address.complement,
                person.address.neighborhood,
                person.address.city,
                person.address.state.name
        )

        val emails = person.emails.map { EmailDto(it.address) }
        val phones = person.phones.map { PhoneDto(it.areaCode, it.number, it.type.name) }

        var personDto = PersonDto(name, lastName, document, birthDate, address, phones, emails)

        personDto.id = person.id!!

        return personDto
    }
}