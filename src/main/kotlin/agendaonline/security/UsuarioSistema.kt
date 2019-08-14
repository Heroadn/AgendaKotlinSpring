package agendaonline.security

import agendaonline.model.Usuario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class UsuarioSistema(var usuario: Usuario, authorities: Collection<GrantedAuthority>) :
        User(usuario.email, usuario.senha, authorities) {}