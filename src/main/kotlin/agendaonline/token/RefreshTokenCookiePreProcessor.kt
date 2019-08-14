package agendaonline.token

import org.apache.catalina.util.ParameterMap
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RefreshTokenCookiePreProcessor : Filter {

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        var req = request as HttpServletRequest

        if ("/oauth/token".equals(req.requestURI, ignoreCase = true)
                && "refresh_token" == req.getParameter("grant_type")
                && req.cookies != null) {
            for (cookie in req.cookies) {
                if (cookie.name == "refreshToken") {
                    val refreshToken = cookie.value
                    req = MyServletRequestWrapper(req, refreshToken)
                }
            }
        }

        chain.doFilter(req, response)
    }

    override fun destroy() {

    }

    class MyServletRequestWrapper(request: HttpServletRequest, private val refreshToken: String) : HttpServletRequestWrapper(request) {

        override fun getParameterMap(): Map<String, Array<String>> {
            val map = ParameterMap(request.parameterMap)
            map["refresh_token"] = arrayOf(refreshToken)
            map.isLocked = true
            return map
        }

    }
}