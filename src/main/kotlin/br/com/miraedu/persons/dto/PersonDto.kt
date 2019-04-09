package br.com.miraedu.persons.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.hibernate.validator.constraints.br.CPF
import java.time.LocalDate
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JsonIgnoreProperties(value = "id", allowSetters = false, allowGetters = true)
@JsonPropertyOrder(value = ["id", "name", "lastName", "document", "birthDate", "address", "phones", "emails"])
class PersonDto(
        @field:NotBlank(message = "Name is required")
        val name: String,

        @field:NotBlank(message = "Last Name is required")
        val lastName: String,

        @field:CPF(message = "Document is invalid")
        val document: String,

        @field:NotNull(message = "Birth Date is required")
        @field:JsonFormat(pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer::class)
        @JsonDeserialize(using = LocalDateDeserializer::class)
        var birthDate: LocalDate? = null,

        @field:Valid @field:NotNull(message = "Address is required")
        var address: AddressDto? = null,

        @field:Valid @field:NotNull(message = "Phones is required")
        var phones: List<PhoneDto>? = null,

        @field:Valid @field:NotNull(message = "Emails is required")
        var emails: List<EmailDto>? = null
) {
    var id: UUID? = null
}
