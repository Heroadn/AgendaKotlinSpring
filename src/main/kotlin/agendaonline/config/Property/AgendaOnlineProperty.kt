package agendaonline.config.Property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("agenda")
class AgendaOnlineProperty {

    companion object {
        private val seguranca = Seguranca()
        private var origemPermitida = "http://localhost:8080"

        fun getSeguranca(): Seguranca {
            return seguranca
        }

        fun getOrigemPermitida(): String {
            return origemPermitida
        }

        fun setOrigemPermitida(origemPermitida: String) {
            this.origemPermitida = origemPermitida
        }
    }

    class   Seguranca {
        var isEnableHttps: Boolean = false

        var JTWSecret: String = "L&@=T&8vg3*G32&%U#SD6*&"
    }
}