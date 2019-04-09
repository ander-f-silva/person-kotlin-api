package br.com.miraedu.persons.component

import br.com.miraedu.persons.domain.ActiveStatus
import br.com.miraedu.persons.exception.ErrorProcessException
import br.com.miraedu.persons.repository.PersonRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class PersonValidator(val personRepository: PersonRepository) {

    fun validateRegisteredDocument(document: String) {
        var hasDocumentRegistration = personRepository.existsByDocumentAndActive(document, ActiveStatus.Y)

        if (hasDocumentRegistration)
            throw throwError(document)
    }

    fun validateRepeatedDocuments(documents: List<String>) {
        val mapDocuments = documents.groupingBy { it }.eachCount().filter { it.value > 1 }

        if (mapDocuments.isNotEmpty())
            throw throwError(mapDocuments.keys.first())

        var persons = personRepository.findByActiveAndDocumentIn(ActiveStatus.Y, documents)

        if (persons.isNotEmpty())
            throw throwError(persons.first().document)
    }

    fun validateRegisteredDocument(id: UUID, document: String) {
        var optPerson = personRepository.findByDocumentAndActive(document, ActiveStatus.Y)

        if (optPerson.isPresent) {
            val person = optPerson.get()

            if (person.id != id && person.document == document)
                throw throwError(document)
        }
    }

    fun throwError(document: String): ErrorProcessException = ErrorProcessException("Document $document is already saved")

}
