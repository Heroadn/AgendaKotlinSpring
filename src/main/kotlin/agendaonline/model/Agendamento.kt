package agendaonline.model

import com.fasterxml.jackson.annotation.*
import java.time.LocalDate
import java.time.Month
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@JsonRootName("agendamento")
data class Agendamento (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        @NotNull var data_registro: LocalDate? = null,
        @NotNull var data_agendamento: LocalDate? = null,
        @Enumerated(EnumType.STRING)
        @NotNull var status: StatusAgendamento? = null,
        @NotNull @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "usuario_id")
        var usuario: Usuario = Usuario(),
        @NotNull @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cliente_id")
        var cliente: Cliente = Cliente()
)