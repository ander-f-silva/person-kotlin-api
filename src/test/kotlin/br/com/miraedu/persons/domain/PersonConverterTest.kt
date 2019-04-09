package br.com.miraedu.persons.domain

import br.com.miraedu.persons.component.PersonConverter
import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.template.PersonDtoTemplateLoader
import br.com.miraedu.persons.template.PersonTemplateLoader
import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import io.damo.aspen.Test
import org.assertj.core.api.Assertions.assertThat

class PersonConverterTest : Test({


    before {
        FixtureFactoryLoader.loadTemplates(PersonDtoTemplateLoader::class.java.`package`.name)
    }

    test("should component dto in domain") {
        val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

        val converter = PersonConverter()

        val person = converter.toPerson(personDto)

        assertThat(person.name).isEqualTo(personDto.name)
        assertThat(person.lastName).isEqualTo(personDto.lastName)
        assertThat(person.document).isEqualTo(personDto.document)
        assertThat(person.birthDate).isEqualTo(personDto.birthDate)

        var address = personDto.address

        assertThat(person.address.publicPlace).isEqualTo(address!!.publicPlace)
        assertThat(person.address.number).isEqualTo(address!!.number)
        assertThat(person.address.complement).isEqualTo(address!!.complement)
        assertThat(person.address.neighborhood).isEqualTo(address!!.neighborhood)
        assertThat(person.address.city).isEqualTo(address!!.city)
        assertThat(person.address.state.name).isEqualTo(address!!.state)

        var emailExpected = personDto.emails!!.component1()
        var email = person.emails.component1()

        assertThat(email.address).isEqualTo(emailExpected.address)

        emailExpected = personDto.emails!!.component2()
        email = person.emails.component2()

        assertThat(email.address).isEqualTo(emailExpected.address)

        var phoneExpected = personDto.phones!!.component1()
        var phone = person.phones.component1()

        assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
        assertThat(phone.number).isEqualTo(phoneExpected.number)
        assertThat(phone.type.name).isEqualTo(phoneExpected.type)

        phoneExpected = personDto.phones!!.component2()
        phone = person.phones.component2()

        assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
        assertThat(phone.number).isEqualTo(phoneExpected.number)
        assertThat(phone.type.name).isEqualTo(phoneExpected.type)
    }

    test("should component domain in dto") {
        val person = Fixture.from(Person::class.java).gimme<Person>(PersonTemplateLoader.VALID)

        val converter = PersonConverter()

        val personDto = converter.toPersonDto(person)

        assertThat(personDto.id).isEqualTo(personDto.id)
        assertThat(personDto.name).isEqualTo(personDto.name)
        assertThat(personDto.lastName).isEqualTo(personDto.lastName)
        assertThat(personDto.document).isEqualTo(personDto.document)
        assertThat(personDto.birthDate).isEqualTo(personDto.birthDate)

        var address = person.address

        assertThat(personDto.address!!.publicPlace).isEqualTo(address.publicPlace)
        assertThat(personDto.address!!.number).isEqualTo(address.number)
        assertThat(personDto.address!!.complement).isEqualTo(address.complement)
        assertThat(personDto.address!!.neighborhood).isEqualTo(address.neighborhood)
        assertThat(personDto.address!!.city).isEqualTo(address.city)
        assertThat(personDto.address!!.state).isEqualTo(address.state.name)

        var emailExpected = person.emails.component1()
        var email = personDto.emails!!.component1()

        assertThat(email.address).isEqualTo(emailExpected.address)

        emailExpected = person.emails.component2()
        email = personDto.emails!!.component2()

        assertThat(email.address).isEqualTo(emailExpected.address)

        var phoneExpected = person.phones.component1()
        var phone = personDto.phones!!.component1()

        assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
        assertThat(phone.number).isEqualTo(phoneExpected.number)
        assertThat(phone.type).isEqualTo(phoneExpected.type.name)

        phoneExpected = person.phones.component2()
        phone = personDto.phones!!.component2()

        assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
        assertThat(phone.number).isEqualTo(phoneExpected.number)
        assertThat(phone.type).isEqualTo(phoneExpected.type.name)
    }

})