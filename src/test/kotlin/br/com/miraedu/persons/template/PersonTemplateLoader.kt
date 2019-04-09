package br.com.miraedu.persons.template

import br.com.miraedu.persons.domain.*
import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import java.time.LocalDate
import java.util.*

class PersonTemplateLoader : TemplateLoader {

    companion object {
        const val VALID = "valid"
    }

    override fun load() {
        Fixture.of(Person::class.java).addTemplate(VALID, object : Rule() {
            init {
                add("id", UUID.randomUUID())
                add("name", firstName())
                add("lastName", lastName())
                add("document", random("65667802899", "47800209482", "77677105211", "44926429519"))
                add("birthDate", LocalDate.of(1976, 9, 12))
                add("address", Address("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", States.AC))
                add("emails", listOf(Email("person01@gmail.com"), Email("person02@gmail.com")))
                add("phones", listOf(Phone(11, 983830022, PhoneType.CELL_PHONE), Phone(11, 56661898, PhoneType.RESIDENTIAL)))
            }
        })
    }
}


