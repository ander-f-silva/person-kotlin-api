package br.com.miraedu.persons.repository

import br.com.miraedu.persons.domain.Email
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EmailRepository : JpaRepository<Email, UUID>