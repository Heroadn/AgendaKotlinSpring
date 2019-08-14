package agendaonline.listener

import agendaonline.event.RecursoCriadoEvento
import org.springframework.context.ApplicationListener
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletResponse
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI


class RecursoCriadoListener : ApplicationListener<RecursoCriadoEvento> {

    override fun onApplicationEvent(recursoCriado: RecursoCriadoEvento) {
        val response:HttpServletResponse = recursoCriado.response
        val id:Long = recursoCriado.id

        adicionarHeader(response, id)
    }

    private fun adicionarHeader(response: HttpServletResponse, id: Long) {
        val uri:URI = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(id).toUri()
        response.setHeader("Location", uri.toASCIIString())
        ResponseEntity.created(uri)
    }

}