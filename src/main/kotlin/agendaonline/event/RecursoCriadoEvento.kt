package agendaonline.event

import org.springframework.context.ApplicationEvent
import javax.servlet.http.HttpServletResponse

class RecursoCriadoEvento(source: Any, response: HttpServletResponse, id: Long) : ApplicationEvent(source) {

    var response: HttpServletResponse = response
    var id: Long = id


}