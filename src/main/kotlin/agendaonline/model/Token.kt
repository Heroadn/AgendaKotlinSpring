package agendaonline.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

data class Token(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var user_id:Long = 0L,
        var client_id:String = "",
        var user_name:String = "",
        var scope:Array<String> = arrayOf()
)