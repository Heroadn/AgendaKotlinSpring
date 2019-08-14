package agendaonline.model

import com.fasterxml.jackson.annotation.*
import org.jetbrains.annotations.NotNull
import javax.persistence.*

@Entity
@JsonRootName("cliente")
data class Cliente(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long = 0L,
        @NotNull var nome:String = "",
        @NotNull var email:String = "",
        @JsonIgnore
        @NotNull var senha: String = "",
        @NotNull var celular:String = "",
        var foto:String = "",
        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id")
        var usuario: Usuario = Usuario()
){
    override fun toString(): String = "Cliente($nome,$email,$usuario)"
}