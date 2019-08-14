package agendaonline.repository

import agendaonline.model.Usuario
import agendaonline.repository.usuario.UsuarioRepositoryQuery
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UsuarioRepository : JpaRepository<Usuario,Long>, UsuarioRepositoryQuery{
    fun findByEmail(email:String) : Optional<Usuario>
}