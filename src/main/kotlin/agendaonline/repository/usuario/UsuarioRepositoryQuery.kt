package agendaonline.repository.usuario

import agendaonline.model.Usuario
import agendaonline.repository.filter.UsuarioFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UsuarioRepositoryQuery {

    fun filtrar(usuarioFilter: UsuarioFilter, pageable: Pageable): Page<Usuario>
}