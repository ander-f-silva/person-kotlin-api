package br.com.miraedu.persons.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
class ErrorProcessException(message: String) : RuntimeException(message)