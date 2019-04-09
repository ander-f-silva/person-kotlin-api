package br.com.miraedu.persons.domain

import br.com.miraedu.persons.exception.ErrorProcessException

enum class States {
    RO, AC, AM, RR, PA, AP, TO, MA, PI, CE, RN, PB, PE, AL, SE, BA, MG, ES, RJ, SP, PR, SC, RS, MS, MT, GO, DF;

    companion object {
        fun fromText(state: String): States {
            if (values().any { it -> it.name == state })
                return valueOf(state)

            throw ErrorProcessException("State is invalid")
        }
    }
}
