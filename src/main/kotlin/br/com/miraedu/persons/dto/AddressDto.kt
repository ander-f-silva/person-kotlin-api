package br.com.miraedu.persons.dto

import javax.validation.constraints.NotBlank

class AddressDto(
        @field:NotBlank(message = "Public Place is required")
        val publicPlace: String,

        @field:NotBlank(message = "Number is required")
        val number: String,

        val complement: String,

        @field:NotBlank(message = "Neighborhood is required")
        val neighborhood: String,

        @field:NotBlank(message = "City is required")
        val city: String,

        @field:NotBlank(message = "State is required")
        val state: String
)