package br.com.miraedu.persons.template

import br.com.miraedu.persons.dto.AddressDto
import br.com.miraedu.persons.dto.EmailDto
import br.com.miraedu.persons.dto.PersonDto
import br.com.miraedu.persons.dto.PhoneDto
import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.Rule
import br.com.six2six.fixturefactory.loader.TemplateLoader
import java.time.LocalDate

class PersonDtoTemplateLoader : TemplateLoader {

    companion object {
        const val VALID = "valid"
        const val VALID_UPDATE = "valid_udpate"
        const val INVALID_NAME_EMPTY = "invalid_name_empty"
        const val INVALID_LAST_NAME_EMPTY = "invalid_last_name_empty"
        const val INVALID_DOCUMENT_EMPTY = "invalid_document_empty"
        const val INVALID_CPF = "invalid_cpf"
        const val INVALID_EMAIL = "invalid_email"
        const val INVALID_STATE = "invalid_state"
        const val INVALID_TYPE = "invalid_type"
        const val DOCUMENT_REPEATED = "document_repeated"
    }

    override fun load() {
        Fixture.of(PersonDto::class.java).addTemplate(VALID, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", lastName())
                add("document", uniqueRandom("65667802899", "47800209482", "77677105211", "44926429519"))
                add("birthDate", LocalDate.of(1976, 9, 12))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person02@gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELL_PHONE"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(VALID_UPDATE, object : Rule() {
            init {
                add("name", "Gerson")
                add("lastName", " Borges")
                add("document", uniqueRandom("11307553125", "76812301060", "26496227179", "35297471818"))
                add("birthDate", LocalDate.of(1975, 10, 31))
                add("address", AddressDto("Rua Edson Arantes do Nascimento", "300", "ap 2 Bloco Y", "Mococa", "Belo Horizonte", "MG"))
                add("emails", listOf(EmailDto("person02@gmail.com"), EmailDto("person03@gmail.com")))
                add("phones", listOf(PhoneDto(13, 984730022, "CELL_PHONE"), PhoneDto(13, 56661800, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(INVALID_NAME_EMPTY, object : Rule() {
            init {
                add("name", random("", " "))
                add("lastName", lastName())
                add("document", uniqueRandom("14200768763", "77240514455", "43611111970", "66186397190"))
                add("birthDate", LocalDate.of(1976, 8, 1))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person02@gmail.com")))
                add("phones", listOf(PhoneDto(11, 56661898, "RESIDENTIAL"), PhoneDto(11, 983830022, "CELL_PHONE")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(INVALID_LAST_NAME_EMPTY, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", random("", " "))
                add("document", uniqueRandom("05325723278", "44291908873", "37504764540", "53265757870"))
                add("birthDate", LocalDate.of(1980, 9, 14))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person02@gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELL_PHONE"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(INVALID_DOCUMENT_EMPTY, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", lastName())
                add("document", uniqueRandom("", " "))
                add("birthDate", LocalDate.of(1976, 6, 6))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person02@gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELL_PHONE"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(INVALID_CPF, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", lastName())
                add("document", uniqueRandom("16252525252", "43434343434", "55645343232", "29573927372"))
                add("birthDate", LocalDate.of(1966, 9, 13))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person02@gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELL_PHONE"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(INVALID_EMAIL, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", lastName())
                add("document", uniqueRandom("98345377270", "24121805712", "29071224872", "86144725520"))
                add("birthDate", LocalDate.of(2000, 10, 12))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01gmail.com"), EmailDto("person02gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELL_PHONE"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(INVALID_STATE, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", lastName())
                add("document", uniqueRandom("38615265739", "14753214818", "22286374678", "40743755324"))
                add("birthDate", LocalDate.of(1988, 9, 6))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SPS"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person@02gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELL_PHONE"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(INVALID_TYPE, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", lastName())
                add("document", uniqueRandom("78153551450", "62836372052", "84830434830", "27038386700"))
                add("birthDate", LocalDate.of(1976, 5, 13))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person@02gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELLPHONES"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })

        Fixture.of(PersonDto::class.java).addTemplate(DOCUMENT_REPEATED, object : Rule() {
            init {
                add("name", firstName())
                add("lastName", lastName())
                add("document", "86086483183")
                add("birthDate", LocalDate.of(2003, 5, 9))
                add("address", AddressDto("Rua Estados Unidos", "30", "", "Cidade Jardim", "São Paulo", "SP"))
                add("emails", listOf(EmailDto("person01@gmail.com"), EmailDto("person@02gmail.com")))
                add("phones", listOf(PhoneDto(11, 983830022, "CELL_PHONE"), PhoneDto(11, 56661898, "RESIDENTIAL")))
            }
        })
    }
}


