package agendaonline.cors

import agendaonline.config.Property.AgendaOnlineProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        response.setHeader("Access-Control-Allow-Origin",AgendaOnlineProperty.getOrigemPermitida())
        response.setHeader("Access-Control-Allow-Credentials", "true")

        if ("OPTIONS" == request.method && AgendaOnlineProperty.getOrigemPermitida().equals(request.getHeader("Origen"))) {
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, POST, PUT, OPTIONS")
            response.setHeader("Access-Control-Allow-Methods", "Authorization, Content-Type, Accept")
            response.setHeader("Access-Control-Max-Age", "3600")
            response.status = HttpServletResponse.SC_OK
        } else {
            chain.doFilter(req, res)
        }
    }

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {

    }

    override fun destroy() {

    }
}