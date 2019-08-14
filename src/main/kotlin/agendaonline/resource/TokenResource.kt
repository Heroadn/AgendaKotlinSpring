package agendaonline.resource

import agendaonline.config.Property.AgendaOnlineProperty
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/tokens")

/**
 * Classe responsavel por gerenciar o recurso 'Token'
 *
 * @author  Benjamin de Castro Azevedo Ponciano
 * @version 1.0
 */
class TokenResource {

    @DeleteMapping("/revoke")
    fun revoke(request: HttpServletRequest, response: HttpServletResponse) {
        val refreshToken = Cookie("refreshToken", null)
        refreshToken.setHttpOnly(true)
        refreshToken.setSecure(AgendaOnlineProperty!!.getSeguranca().isEnableHttps)
        refreshToken.setPath(request.getContextPath() + "/oauth/token")
        refreshToken.setMaxAge(0)

        response.addCookie(refreshToken)
        response.status = HttpStatus.NO_CONTENT.value()
    }
}
