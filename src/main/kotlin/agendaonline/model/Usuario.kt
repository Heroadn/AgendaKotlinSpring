package agendaonline.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonRootName
import org.hibernate.validator.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import javax.persistence.*

@Entity
@JsonRootName("usuario")
data class Usuario(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id:Long = 0L,
        @NotNull var nome:String = "",
        @NotNull var email:String = "",
        @JsonIgnore
        @NotNull @NotBlank var senha: String = "",
        @NotNull var celular:String = "",
        var foto:String = "",
        @JsonIgnore
        @NotNull var ativo:Boolean = false,
        @JsonIgnore
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
        var cliente: MutableList<Cliente> =  mutableListOf()
){
    override fun toString(): String = "Usuario_($nome,$email)"
}