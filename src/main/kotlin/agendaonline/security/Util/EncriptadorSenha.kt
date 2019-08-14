package agendaonline.security.Util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class EncriptadorSenha{
    companion object {
        fun encriptar(senha:String) {
            val encoder = BCryptPasswordEncoder()
            println(encoder.encode("admin"))
        }
    }
}