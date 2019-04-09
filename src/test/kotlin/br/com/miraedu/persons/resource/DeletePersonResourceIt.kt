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
import java.util.UUID

@ActiveProfiles("test")
@RunWith(SpringTestTreeRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeletePersonResourceIt : Test() {

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

        describe("Find single person") {
            test("# should returns error for empty base") {
                val id = UUID.randomUUID()

                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .`when`()
                        .delete("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("message", equalTo("Person $id not found"))

                var person = repository.findById(id!!)

                assertThat(person).isEmpty
            }

            test("# should find one persons register") {
                val personDto = Fixture.from(PersonDto::class.java).gimme<PersonDto>(PersonDtoTemplateLoader.VALID)

                val id = repository.save(converter.toPerson(personDto)).id

                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .`when`()
                        .delete("/api/persons/{id}", id)
                        .then()
                        .statusCode(HttpStatus.ACCEPTED.value())

                var person = repository.findById(id!!)

                assertThat(person).isNotNull
                assertThat(person.get().active).isEqualTo(ActiveStatus.N)
            }
        }

        after {
            var persons = repository.findByActive(ActiveStatus.Y, PageRequest.of(0, 10))
            persons.forEach { it -> it.disable() }
            repository.saveAll(persons)
        }
    }
}