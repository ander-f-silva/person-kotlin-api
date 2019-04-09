package br.com.miraedu.persons.domain

import br.com.miraedu.persons.exception.ErrorProcessException
import io.damo.aspen.Test
import io.damo.aspen.expectException
import org.assertj.core.api.Assertions.assertThat

class StatesTest : Test({

    test("must find a valid state") {
        val state = States.fromText("SP")
        assertThat(state).isEqualTo(States.SP)
    }

    test("should return the invalid state error") {
        expectException(ErrorProcessException::class, "State is invalid") {
            States.fromText("SSP")
        }
    }

})