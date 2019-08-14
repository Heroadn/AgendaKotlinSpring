package agendaonline.exceptionHandler.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.NOT_FOUND)
class UsuarioInexistenteOuInativo(message: String?) : RuntimeException(message) {
}