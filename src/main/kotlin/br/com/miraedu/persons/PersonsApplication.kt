package br.com.miraedu.persons

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class PersonsApplication

fun main(args: Array<String>) {
    runApplication<PersonsApplication>(*args)
}

