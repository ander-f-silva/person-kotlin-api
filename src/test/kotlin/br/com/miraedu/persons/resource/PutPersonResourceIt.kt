package br.com.miraedu.persons.resource

import br.com.miraedu.persons.component.PersonConverter
import br.com.miraedu.persons.domain.ActiveStatus
import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.repository.PersonRepository
import br.com.miraedu.persons.template.PersonDtoTemplateLoader
import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
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
class PutPersonResourceIt : Test() {

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

        describe("Update a registered person ") {

            test("# must update a person") {
                var personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                var id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.ACCEPTED.value())

                val person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isEqualTo(person.name)
                assertThat(request.lastName).isEqualTo(person.lastName)
                assertThat(request.document).isEqualTo(person.document)
                assertThat(request.birthDate).isEqualTo(person.birthDate)
                assertThat(ActiveStatus.Y).isEqualTo(person.active)

                assertThat(request.address!!.publicPlace).isEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isEqualTo(person.address.number)
                assertThat(request.address!!.complement).isEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isEqualTo(person.address.city)
                assertThat(request.address!!.state).isEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isEqualTo(person.phones[0].number)
                assertThat(request.phones!![0].type).isEqualTo(person.phones[0].type.name)

                assertThat(request.phones!![1].areaCode).isEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isEqualTo(person.phones[1].number)
                assertThat(request.phones!![1].type).isEqualTo(person.phones[1].type.name)
            }

            test("# not update person when name is not informed") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_NAME_EMPTY)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Name is required"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)

            }

            test("# not update person when last name is not informed") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_LAST_NAME_EMPTY)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Last Name is required"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# not update person when document is not informed") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_DOCUMENT_EMPTY)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Document is invalid"))

                val person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)

            }

            test("# not update person when birth date is not informed") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                request.birthDate = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Birth Date is required"))

                val person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(person.birthDate).isNotNull()

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# not update person when address is not informed") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                request.address = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Address is required"))

                val person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(person.address).isNotNull

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# not update person when emails are not informed") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                request.emails = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Emails is required"))

                val person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(person.emails).isNotNull

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# not update person when phones are not informed") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)
                request.phones = null

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Phones is required"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(person.phones).isNotNull()
            }

            test("# does not update person when cpf is invalid") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_CPF)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Document is invalid"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# does not update person when email is invalid") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_EMAIL)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .body("errors[0].defaultMessage", equalTo("Email is invalid"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# does not update person when state is invalid") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_STATE)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("State is invalid"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# does not update person when type is invalid") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE)
                val id = repository.save(converter.toPerson(personDto)).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.INVALID_TYPE)

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("Phone Type is invalid"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }

            test("# not update people when they have document in the base") {
                repository.save(converter.toPerson(Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.DOCUMENT_REPEATED)))

                val id = repository.save(converter.toPerson(Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID))).id

                val request = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID_UPDATE, object : Rule() {
                    init {
                        add("document", "86086483183")
                    }
                })

                given()
                        .body(request)
                        .contentType(ContentType.JSON)
                        .`when`()
                        .put("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                        .body("message", equalTo("Document 86086483183 is already saved"))

                var person = repository.findByIdAndActive(id!!, ActiveStatus.Y)

                assertThat(person.id).isNotNull()
                assertThat(request.name).isNotEqualTo(person.name)
                assertThat(request.lastName).isNotEqualTo(person.lastName)
                assertThat(request.document).isNotEqualTo(person.document)
                assertThat(request.birthDate).isNotEqualTo(person.birthDate)

                assertThat(request.address!!.publicPlace).isNotEqualTo(person.address.publicPlace)
                assertThat(request.address!!.number).isNotEqualTo(person.address.number)
                assertThat(request.address!!.complement).isNotEqualTo(person.address.complement)
                assertThat(request.address!!.neighborhood).isNotEqualTo(person.address.neighborhood)
                assertThat(request.address!!.city).isNotEqualTo(person.address.city)
                assertThat(request.address!!.state).isNotEqualTo(person.address.state.name)

                assertThat(request.emails!![0].address).isNotEqualTo(person.emails[0].address)
                assertThat(request.emails!![1].address).isNotEqualTo(person.emails[1].address)

                assertThat(request.phones!![0].areaCode).isNotEqualTo(person.phones[0].areaCode)
                assertThat(request.phones!![0].number).isNotEqualTo(person.phones[0].number)

                assertThat(request.phones!![1].areaCode).isNotEqualTo(person.phones[1].areaCode)
                assertThat(request.phones!![1].number).isNotEqualTo(person.phones[1].number)
            }
        }

        after {
            var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))
            persons.forEach { it -> it.disable() }
            repository.saveAll(persons)
        }
    }
}

