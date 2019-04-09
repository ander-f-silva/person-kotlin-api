package br.com.miraedu.persons.repository

import br.com.miraedu.persons.domain.Phone
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PhoneRepository : JpaRepository<Phone, UUID>