package agendaonline.exceptionHandler.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class FileStorageException(message: String) : RuntimeException(message) {
}