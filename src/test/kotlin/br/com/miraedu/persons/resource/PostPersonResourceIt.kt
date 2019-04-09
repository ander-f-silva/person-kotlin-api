package br.com.miraedu.persons.resource

import br.com.miraedu.persons.component.PersonConverter
import br.com.miraedu.persons.domain.ActiveStatus
import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.dto.PersonListDto
import br.com.miraedu.persons.repository.PersonRepository
import br.com.miraedu.persons.template.PersonDtoTemplateLoader
import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import io.damo.aspen.Test
import io.damo.aspen.spring.SpringTestTreeRunner
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@RunWith(SpringTestTreeRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostPersonResourceIt : Test() {

    @Autowired
    lateinit var repository: PersonRepository

    @Autowired
    lateinit var converter: PersonConverter

    @LocalServerPort
    val port: Int = 8090

    init {
        before {
            FixtureFactoryLoader.loadTemplates(PersonDtoTemplateLoader::class.java.`package`.name)

            RestAssured.baseURI = "http://localhost"
            RestAssured.port = port
        }

        describe("Register single person ") {

            test("# must register a single person") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())

                val persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(1)

                val person = persons.component1()

                assertThat(person.id).isNotNull()
                assertThat(request.name).isEqualTo(person.name)
                assertThat(request.lastName).isEqualTo(person.lastName)
                assertThat(request.document).isEqualTo(person.document)
                assertThat(request.birthDate).isEqualTo(person.birthDate)
                assertThat(ActiveStatus.Y).isEqualTo(person.active)

                var address = person.address

                assertThat(request.address!!.publicPlace).isEqualTo(address.publicPlace)
                assertThat(request.address!!.number).isEqualTo(address.number)
                assertThat(request.address!!.complement).isEqualTo(address.complement)
                assertThat(request.address!!.neighborhood).isEqualTo(address.neighborhood)
                assertThat(request.address!!.city).isEqualTo(address.city)
                assertThat(request.address!!.state).isEqualTo(address.state.name)

                var emailExpected = person.emails.component1()
                var email = request.emails!!.component1()

                assertThat(email.address).isEqualTo(emailExpected.address)

                emailExpected = person.emails.component2()
                email = request.emails!!.component2()

                assertThat(email.address).isEqualTo(emailExpected.address)

                var phoneExpected = person.phones.component1()
                var phone = request.phones!!.component1()

                assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
                assertThat(phone.number).isEqualTo(phoneExpected.number)
                assertThat(phone.type).isEqualTo(phoneExpected.type.name)

                phoneExpected = person.phones.component2()
                phone = request.phones!!.component2()

                assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
                assertThat(phone.number).isEqualTo(phoneExpected.number)
                assertThat(phone.type).isEqualTo(phoneExpected.type.name)
            }

            test("# not register person when name is not informed") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_NAME_EMPTY)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Name is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register person when last name is not informed") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_LAST_NAME_EMPTY)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Last Name is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register person when document is not informed") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_DOCUMENT_EMPTY)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Document is invalid"))


                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register person when birth date is not informed") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                request.birthDate = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Birth Date is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register person when address is not informed") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                request.address = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Address is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register person when emails are not informed") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                request.emails = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Emails is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register person when phones are not informed") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                request.phones = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Phones is required"))


                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register person when cpf is invalid") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_CPF)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Document is invalid"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register person when email is invalid") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_EMAIL)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Email is invalid"))


                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register person when state is invalid") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_STATE)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("State is invalid"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register person when type is invalid") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_TYPE)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("Phone Type is invalid"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register single people when they have document in the base") {
                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.DOCUMENT_REPEATED)

                val person = converter.toPerson(request)
                repository.save(person)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/persons")
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("Document 86086483183 is already saved"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(1)
            }
        }

        describe("Register persons in batch mode ") {

            test("# must register many persons") {
                var request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID))


                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.OK.value())

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(2)

                var person = persons.component1()
                var element = request.persons.component1()

                assertThat(person.id).isNotNull()
                assertThat(element.name).isEqualTo(person.name)
                assertThat(element.lastName).isEqualTo(person.lastName)
                assertThat(element.document).isEqualTo(person.document)
                assertThat(element.birthDate).isEqualTo(person.birthDate)
                assertThat(ActiveStatus.Y).isEqualTo(person.active)

                var address = person.address

                assertThat(element.address!!.publicPlace).isEqualTo(address.publicPlace)
                assertThat(element.address!!.number).isEqualTo(address.number)
                assertThat(element.address!!.complement).isEqualTo(address.complement)
                assertThat(element.address!!.neighborhood).isEqualTo(address.neighborhood)
                assertThat(element.address!!.city).isEqualTo(address.city)
                assertThat(element.address!!.state).isEqualTo(address.state.name)

                var emailExpected = person.emails.component1()
                var email = element.emails!!.component1()

                assertThat(email.address).isEqualTo(emailExpected.address)

                emailExpected = person.emails.component2()
                email = element.emails!!.component2()

                assertThat(email.address).isEqualTo(emailExpected.address)

                var phoneExpected = person.phones.component1()
                var phone = element.phones!!.component1()

                assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
                assertThat(phone.number).isEqualTo(phoneExpected.number)
                assertThat(phone.type).isEqualTo(phoneExpected.type.name)

                phoneExpected = person.phones.component2()
                phone = element.phones!!.component2()

                assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
                assertThat(phone.number).isEqualTo(phoneExpected.number)
                assertThat(phone.type).isEqualTo(phoneExpected.type.name)

                person = persons.component2()
                element = request.persons.component2()

                assertThat(person.id).isNotNull()
                assertThat(element.name).isEqualTo(person.name)
                assertThat(element.lastName).isEqualTo(person.lastName)
                assertThat(element.document).isEqualTo(person.document)
                assertThat(element.birthDate).isEqualTo(person.birthDate)
                assertThat(ActiveStatus.Y).isEqualTo(person.active)

                address = person.address

                assertThat(element.address!!.publicPlace).isEqualTo(address.publicPlace)
                assertThat(element.address!!.number).isEqualTo(address.number)
                assertThat(element.address!!.complement).isEqualTo(address.complement)
                assertThat(element.address!!.neighborhood).isEqualTo(address.neighborhood)
                assertThat(element.address!!.city).isEqualTo(address.city)
                assertThat(element.address!!.state).isEqualTo(address.state.name)

                emailExpected = person.emails.component1()
                email = element.emails!!.component1()

                assertThat(email.address).isEqualTo(emailExpected.address)

                emailExpected = person.emails.component2()
                email = element.emails!!.component2()

                assertThat(email.address).isEqualTo(emailExpected.address)

                phoneExpected = person.phones.component1()
                phone = element.phones!!.component1()

                assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
                assertThat(phone.number).isEqualTo(phoneExpected.number)
                assertThat(phone.type).isEqualTo(phoneExpected.type.name)

                phoneExpected = person.phones.component2()
                phone = element.phones!!.component2()

                assertThat(phone.areaCode).isEqualTo(phoneExpected.areaCode)
                assertThat(phone.number).isEqualTo(phoneExpected.number)
                assertThat(phone.type).isEqualTo(phoneExpected.type.name)
            }

            test("# not register many persons when one name is not informed") {
                var request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID, PersonDtoTemplateLoader.INVALID_NAME_EMPTY))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Name is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register many persons when one last name is not informed") {
                var request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID, PersonDtoTemplateLoader.INVALID_LAST_NAME_EMPTY))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Last Name is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register many persons when document is not informed") {
                var request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID, PersonDtoTemplateLoader.INVALID_DOCUMENT_EMPTY))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Document is invalid"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register many persons when birth date is not informed") {
                val personDtoValid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                val personDtoInvalid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                personDtoInvalid.birthDate = null

                var request = PersonListDto(listOf(personDtoValid, personDtoInvalid))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Birth Date is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register many persons when address is not informed") {
                val personDtoValid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                val personDtoInvalid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                personDtoInvalid.address = null

                var request = PersonListDto(listOf(personDtoValid, personDtoInvalid))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Address is required"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register many persons when emails are not informed") {
                val personDtoValid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                val personDtoInvalid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                personDtoInvalid.emails = null

                var request = PersonListDto(listOf(personDtoValid, personDtoInvalid))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Emails is required"))


                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register many persons when phones are not informed") {
                val personDtoValid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                val personDtoInvalid = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                personDtoInvalid.phones = null

                var request = PersonListDto(listOf(personDtoValid, personDtoInvalid))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Phones is required"))


                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register many persons when cpf is invalid") {
                var request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID, PersonDtoTemplateLoader.INVALID_CPF))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Document is invalid"))


                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register many persons when email is invalid") {
                val request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID, PersonDtoTemplateLoader.INVALID_EMAIL))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Email is invalid"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register many persons when state is invalid") {
                val request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID, PersonDtoTemplateLoader.INVALID_STATE))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("State is invalid"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register many persons when type is invalid") {
                val request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID, PersonDtoTemplateLoader.INVALID_TYPE))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("Phone Type is invalid"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# does not register many persons when has cpf repeated in list") {
                val request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.DOCUMENT_REPEATED))

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("Document 86086483183 is already saved"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# not register many people when they have document in the base") {
                val request = PersonListDto(Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID))

                val person = converter.toPerson(request.persons.component1())
                repository.save(person)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .post("/api/batch-persons")
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("Document ${person.document} is already saved"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(1)
            }
        }

        after {
            var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))
            persons.forEach { it -> it.disable() }
            repository.saveAll(persons)
        }
    }
}

