package agendaonline.model;

import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;

public class Agendamento_ {

    public static volatile SingularAttribute<Agendamento, Long> codigo;
    public static volatile SingularAttribute<Agendamento, LocalDate> data_registro;
    public static volatile SingularAttribute<Agendamento, LocalDate> data_agendamento;
    public static volatile SingularAttribute<Agendamento, StatusAgendamento> StatusAgendamento;
    public static volatile SingularAttribute<Agendamento, Usuario_> usuario;
    public static volatile SingularAttribute<Agendamento, Cliente> cliente;

}
