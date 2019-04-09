package br.com.miraedu.persons

import io.damo.aspen.Test
import io.damo.aspen.spring.SpringTestTreeRunner
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@RunWith(SpringTestTreeRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonsApplicationTests : Test({
    test("test loader") {}
})




