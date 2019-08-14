package agendaonline.config

import agendaonline.config.Property.AgendaOnlineProperty
import agendaonline.token.CustomTokenEnhancer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import java.util.*

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    var manager: AuthenticationManager? = null

    /*
    * Define as permssoes da aplicação,
    * ex: telefone tem permissao de leitura, ja em um computador tem leitura e escrita*/
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("angular")
                .secret("angular123")//secret do JWT
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(1800)//Tempo de vida do token de acesso
                .refreshTokenValiditySeconds(3600 * 24)//Tempo de vida do refresh token
                .and()
                .withClient("mobile")
                .secret("mobile123")//secret do JWT
                .scopes("read")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(1800)//Tempo de vida do token de acesso
                .refreshTokenValiditySeconds(3600 * 24)//Tempo de vida do refresh token
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        val tokenEnhancerChain = TokenEnhancerChain()
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()))

        endpoints!!
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .reuseRefreshTokens(false)
                .authenticationManager(manager)
    }

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val acessTokenConverter = JwtAccessTokenConverter()
        acessTokenConverter.setSigningKey(AgendaOnlineProperty.getSeguranca().JTWSecret)
        return acessTokenConverter
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    fun tokenEnhancer(): TokenEnhancer {
        return CustomTokenEnhancer()
    }
}