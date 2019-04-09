package br.com.miraedu.persons.resource

import br.com.miraedu.persons.component.PersonConverter
import br.com.miraedu.persons.domain.ActiveStatus
import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.repository.PersonRepository
import br.com.miraedu.persons.template.PersonDtoTemplateLoader
import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import io.damo.aspen.Test
import io.damo.aspen.spring.SpringTestTreeRunner
import io.restassured.RestAssured
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
import java.time.format.DateTimeFormatter
import java.util.*

@ActiveProfiles("test")
@RunWith(SpringTestTreeRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetPersonResourceIt : Test() {

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

        describe("Find all persons active true ") {

            test("# should returns error for empty base") {

                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .param("page", "0")
                        .param("size", "2")
                        .`when`()
                        .get("/api/persons")
                        .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("message", equalTo("Persons not found"))

                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                assertThat(persons).isNotNull
                assertThat(persons.size).isEqualTo(0)
            }

            test("# should find all persons indexed") {
                val persons = Fixture.from(PersonDto::class.java).gimme<PersonDto>(2, PersonDtoTemplateLoader.VALID)

                repository.saveAll(persons.map { it -> converter.toPerson(it) })

                val personOne = persons.component1()
                val personTwo = persons.component2()

                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .param("page", "0")
                        .param("size", "2")
                        .`when`()
                        .get("/api/persons")
                        .then()
                        .statusCode(HttpStatus.OK.value())

                        .body("persons[0].name", equalTo(personOne.name))
                        .body("persons[0].lastName", equalTo(personOne.lastName))
                        .body("persons[0].document", equalTo(personOne.document))
                        .body("persons[0].birthDate", equalTo(personOne.birthDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                        .body("persons[0].address.publicPlace", equalTo(personOne.address!!.publicPlace))
                        .body("persons[0].address.number", equalTo(personOne.address!!.number))
                        .body("persons[0].address.complement", equalTo(personOne.address!!.complement))
                        .body("persons[0].address.neighborhood", equalTo(personOne.address!!.neighborhood))
                        .body("persons[0].address.city", equalTo(personOne.address!!.city))
                        .body("persons[0].address.state", equalTo(personOne.address!!.state))
                        .body("persons[0].phones[0].areaCode", equalTo(personOne.phones!![0].areaCode))
                        .body("persons[0].phones[0].number", equalTo(personOne.phones!![0].number))
                        .body("persons[0].phones[0].type", equalTo(personOne.phones!![0].type))
                        .body("persons[0].phones[1].areaCode", equalTo(personOne.phones!![1].areaCode))
                        .body("persons[0].phones[1].number", equalTo(personOne.phones!![1].number))
                        .body("persons[0].phones[1].type", equalTo(personOne.phones!![1].type))
                        .body("persons[0].emails[0].address", equalTo(personOne.emails!![0].address))
                        .body("persons[0].emails[1].address", equalTo(personOne.emails!![1].address))

                        .body("persons[1].name", equalTo(personTwo.name))
                        .body("persons[1].lastName", equalTo(personTwo.lastName))
                        .body("persons[1].document", equalTo(personTwo.document))
                        .body("persons[1].birthDate", equalTo(personTwo.birthDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                        .body("persons[1].address.publicPlace", equalTo(personTwo.address!!.publicPlace))
                        .body("persons[1].address.number", equalTo(personTwo.address!!.number))
                        .body("persons[1].address.complement", equalTo(personTwo.address!!.complement))
                        .body("persons[1].address.neighborhood", equalTo(personOne.address!!.neighborhood))
                        .body("persons[1].address.city", equalTo(personTwo.address!!.city))
                        .body("persons[1].address.state", equalTo(personTwo.address!!.state))
                        .body("persons[1].phones[0].areaCode", equalTo(personTwo.phones!![0].areaCode))
                        .body("persons[1].phones[0].number", equalTo(personTwo.phones!![0].number))
                        .body("persons[1].phones[0].type", equalTo(personTwo.phones!![0].type))
                        .body("persons[1].phones[1].areaCode", equalTo(personTwo.phones!![1].areaCode))
                        .body("persons[1].phones[1].number", equalTo(personTwo.phones!![1].number))
                        .body("persons[1].phones[1].type", equalTo(personTwo.phones!![1].type))
                        .body("persons[1].emails[0].address", equalTo(personOne.emails!![0].address))
                        .body("persons[1].emails[1].address", equalTo(personTwo.emails!![1].address))
            }

            describe("Find single person") {
                test("# should returns error for empty base") {
                    val id = UUID.randomUUID().toString()

                    RestAssured.given()
                            .contentType(ContentType.JSON)
                            .`when`()
                            .get("/api/persons/{id}", id)
                            .then()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .body("message", equalTo("Person $id not found"))

                    var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                    assertThat(persons).isNotNull
                    assertThat(persons.size).isEqualTo(0)
                }

                test("# should find one persons register") {
                    val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                    val person = repository.save(converter.toPerson(personDto))

                    RestAssured.given()
                            .contentType(ContentType.JSON)
                            .`when`()
                            .get("/api/persons/{id}", person.id)
                            .then()
                            .statusCode(HttpStatus.OK.value())

                            .body("id", equalTo(person.id.toString()))
                            .body("name", equalTo(person.name))
                            .body("lastName", equalTo(person.lastName))
                            .body("document", equalTo(person.document))
                            .body("birthDate", equalTo(person.birthDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                            .body("address.publicPlace", equalTo(person.address!!.publicPlace))
                            .body("address.number", equalTo(person.address!!.number))
                            .body("address.complement", equalTo(person.address!!.complement))
                            .body("address.neighborhood", equalTo(person.address!!.neighborhood))
                            .body("address.city", equalTo(person.address!!.city))
                            .body("address.state", equalTo(person.address!!.state.name))
                            .body("phones[0].areaCode", equalTo(person.phones!![0].areaCode))
                            .body("phones[0].number", equalTo(person.phones!![0].number))
                            .body("phones[0].type", equalTo(person.phones!![0].type.name))
                            .body("phones[1].areaCode", equalTo(person.phones!![1].areaCode))
                            .body("phones[1].number", equalTo(person.phones!![1].number))
                            .body("phones[1].type", equalTo(person.phones!![1].type.name))
                            .body("emails[0].address", equalTo(person.emails!![0].address))
                            .body("emails[1].address", equalTo(person.emails!![1].address))
                }
            }

            describe("Find person by name, last name and document ") {

                test("# should return not found when not meet data with parameter information") {
                    val id = UUID.randomUUID().toString()

                    RestAssured.given()
                            .contentType(ContentType.JSON)
                            .`when`()
                            .param("name", "Anderson")
                            .param("lastName", "Silva")
                            .param("document", "78774879405")
                            .get("/api/persons/search")
                            .then()
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .body("message", equalTo("Persons not found"))

                    var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))

                    assertThat(persons).isNotNull
                    assertThat(persons.size).isEqualTo(0)
                }

                test("# should return ok with parameter information") {
                    val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                    val person = repository.save(converter.toPerson(personDto))

                    RestAssured.given()
                            .contentType(ContentType.JSON)
                            .`when`()
                            .param("name", person.name)
                            .param("lastName", person.lastName)
                            .param("document", person.document)
                            .get("/api/persons/search")
                            .then()
                            .statusCode(HttpStatus.OK.value())

                            .body("persons[0].name", equalTo(person.name))
                            .body("persons[0].lastName", equalTo(person.lastName))
                            .body("persons[0].document", equalTo(person.document))
                            .body("persons[0].birthDate", equalTo(person.birthDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                            .body("persons[0].address.publicPlace", equalTo(person.address!!.publicPlace))
                            .body("persons[0].address.number", equalTo(person.address!!.number))
                            .body("persons[0].address.complement", equalTo(person.address!!.complement))
                            .body("persons[0].address.neighborhood", equalTo(person.address!!.neighborhood))
                            .body("persons[0].address.city", equalTo(person.address!!.city))
                            .body("persons[0].address.state", equalTo(person.address!!.state.name))
                            .body("persons[0].phones[0].areaCode", equalTo(person.phones!![0].areaCode))
                            .body("persons[0].phones[0].number", equalTo(person.phones!![0].number))
                            .body("persons[0].phones[0].type", equalTo(person.phones!![0].type.name))
                            .body("persons[0].phones[1].areaCode", equalTo(person.phones!![1].areaCode))
                            .body("persons[0].phones[1].number", equalTo(person.phones!![1].number))
                            .body("persons[0].phones[1].type", equalTo(person.phones!![1].type.name))
                            .body("persons[0].emails[0].address", equalTo(person.emails!![0].address))
                            .body("persons[0].emails[1].address", equalTo(person.emails!![1].address))
                }
            }

            after {
                var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))
                persons.forEach { it -> it.disable() }
                repository.saveAll(persons)
            }
        }
    }
}