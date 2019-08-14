package agendaonline.security

import agendaonline.repository.UsuarioRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.ArrayList



@Service
class AppUserDetailsService(var usuarioRepository: UsuarioRepository) : UserDetailsService {

    /*Carrega o Usuario do banco de dados*/
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val listGrantAuthority = ArrayList<GrantedAuthority>()
        val usuarioOptional = usuarioRepository.findByEmail(email)
        val usuario = usuarioOptional.orElseThrow({ UsernameNotFoundException("Usuario e/ou senha incorretas") })
        return UsuarioSistema(usuario, listGrantAuthority)
    }


    /*Define as permissoes que o usuario tem nos recursos
    private fun getPermissoes(usuario: Usuario): Collection<GrantedAuthority> {
        val authorities = HashSet<SimpleGrantedAuthority>()
        usuario.permissoes.forEach { p -> authorities.add(SimpleGrantedAuthority(p.descricao.toUpperCase())) }
        return authorities
    }*/
}