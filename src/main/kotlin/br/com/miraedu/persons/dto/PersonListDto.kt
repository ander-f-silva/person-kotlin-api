package br.com.miraedu.persons.dto

import javax.validation.Valid

class PersonListDto(@field:Valid val persons: List<PersonDto>)
