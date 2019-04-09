package br.com.miraedu.persons.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PhoneDto(
        @field:NotNull(message = "Area Code is required")
        val areaCode: Int,

        @field:NotNull(message = "Number is required")
        val number: Int,

        @field:NotBlank(message = "Type is required")
        val type: String
)

