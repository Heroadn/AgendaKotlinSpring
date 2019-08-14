package agendaonline.resource.Upload

import org.apache.catalina.util.ParameterMap
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class UploadPreProcessor : Filter {

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        var req = request as HttpServletRequest

        if ("/usuario/".equals(req.requestURI, ignoreCase = true) && "POST".equals(request.getMethod())) {
            var file: MultipartFile = (request as MultipartHttpServletRequest).getFile("foto");
            //println(file.size)
        }

        if ("/cliente/".equals(req.requestURI, ignoreCase = true) && "POST".equals(request.getMethod())) {
            var file: MultipartFile = (request as MultipartHttpServletRequest).getFile("foto");
            //println(file.size)
            //        	if(!file.isEmpty()) {
            //        		//Salvando a imagem
            //    			BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            //    			ImageIO.write(src,"png",new File("images/"+usuario.getNome()+usuario.getId()+".png"));
            //
            //    			//Salvando o usuario
            //    	        usuario.setImagem(usuario.getNome()+usuario.getId()+".png");
            //    	        usuario.getInventario().setPontos(0);
            //    	        usuario.getInventario().setDecks(new ArrayList<Deck>());
        }

        chain.doFilter(req, response)
    }

    override fun destroy() {
    }
}