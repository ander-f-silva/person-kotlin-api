package br.com.miraedu.persons.dto

import javax.validation.constraints.Email

class EmailDto(
        @field:Email(message = "Email is invalid")
        val address: String
)