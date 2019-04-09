package br.com.miraedu.persons.domain

import br.com.miraedu.persons.exception.ErrorProcessException
import io.damo.aspen.Test
import io.damo.aspen.expectException
import org.assertj.core.api.Assertions.assertThat

class PhoneTypeTest : Test({

    test("must find a valid phone type") {
        val phoneType = PhoneType.fromText("RESIDENTIAL")
        assertThat(phoneType).isEqualTo(PhoneType.RESIDENTIAL)
    }

    test("should return the invalid phone type error") {
        expectException(ErrorProcessException::class, "Phone Type is invalid") {
            PhoneType.fromText("WORKING")
        }
    }

})