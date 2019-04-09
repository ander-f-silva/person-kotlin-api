package br.com.miraedu.persons.domain

import br.com.miraedu.persons.exception.ErrorProcessException

enum class PhoneType {
    CELL_PHONE, RESIDENTIAL;

    companion object {
        fun fromText(phoneType: String): PhoneType {
            if (PhoneType.values().any { it -> it.name == phoneType })
                return PhoneType.valueOf(phoneType)

            throw ErrorProcessException("Phone Type is invalid")
        }
    }
}
