package br.com.miraedu.persons.resource

import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.dto.PersonListDto
import br.com.miraedu.persons.service.PersonService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(produces = [APPLICATION_JSON_UTF8_VALUE])
class PersonResource(val service: PersonService) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(PersonResource::class.java)
    }

    @PostMapping(value = "/persons", consumes = [APPLICATION_JSON_UTF8_VALUE])
    fun save(@Valid @RequestBody personDto: PersonDto): ResponseEntity<Void> {
        logger.info("Create person. Format json {}", jacksonObjectMapper().writeValueAsString(personDto))

        val uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(service.save(personDto))
                .toUri()
        return ResponseEntity.created(uri).build()
    }

    @PostMapping(value = "/batch-persons", consumes = [APPLICATION_JSON_UTF8_VALUE])
    fun saveAll(@Valid @RequestBody persons: PersonListDto): ResponseEntity<PersonListDto> {
        logger.info("Create persons in batch. Format json {}", jacksonObjectMapper().writeValueAsString(persons))
        return ResponseEntity.ok(service.saveAll(persons))
    }

    @PutMapping(value = "/persons/{id}", consumes = [APPLICATION_JSON_UTF8_VALUE])
    fun save(@PathVariable("id") id: UUID, @Valid @RequestBody person: PersonDto): ResponseEntity<Unit> {
        logger.info("Update person $id. Format json {}", jacksonObjectMapper().writeValueAsString(person))
        service.save(id, person)
        return ResponseEntity.accepted().build()
    }

    @DeleteMapping(value = "/persons/{id}", consumes = [APPLICATION_JSON_UTF8_VALUE])
    fun delete(@PathVariable("id") id: UUID): ResponseEntity<Unit> {
        logger.info("Remove person $id.")
        service.delete(id)
        return ResponseEntity.accepted().build()
    }

    @GetMapping(value = "/persons")
    fun findAll(@RequestParam("page", defaultValue = "0") page: Int, @RequestParam("size", defaultValue = "10") size: Int): ResponseEntity<PersonListDto> = ResponseEntity.ok(service.findAll(page, size))

    @GetMapping(value = "/persons/{id}")
    fun findById(@PathVariable("id") id: UUID): ResponseEntity<PersonDto> = ResponseEntity.ok(service.findById(id))

    @GetMapping(value = "/persons/search")
    fun findByNameAndLastNameOrDocument(@RequestParam("name") name: String, @RequestParam("lastName") lastName: String, @RequestParam("document", required = false) document: String): ResponseEntity<PersonListDto> = ResponseEntity.ok(service.findByNameAndLastNameOrDocument(name, lastName, document))
}
