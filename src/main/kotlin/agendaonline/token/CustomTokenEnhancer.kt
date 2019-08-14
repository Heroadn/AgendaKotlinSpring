package agendaonline.token

import agendaonline.security.UsuarioSistema
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import java.util.HashMap

/*Incrementa o JWT*/
class CustomTokenEnhancer : TokenEnhancer {


    override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken {
        val usuarioSistema = authentication.principal as UsuarioSistema

        /*Adicionando informaçoes ao JWT*/
        val addInfo = HashMap<String, Any>()
        addInfo["user_id"] = usuarioSistema.usuario.id

        (accessToken as DefaultOAuth2AccessToken).additionalInformation = addInfo
        return accessToken
    }
}