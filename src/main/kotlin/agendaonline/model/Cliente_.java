package agendaonline.model;

import javax.persistence.metamodel.SingularAttribute;

public class Cliente_ {
        public static volatile SingularAttribute<Cliente, Long> id;
        public static volatile SingularAttribute<Cliente, String> nome;
        public static volatile SingularAttribute<Cliente, String> email;
        public static volatile SingularAttribute<Cliente, String> senha;
        public static volatile SingularAttribute<Cliente, String> celular;
        public static volatile SingularAttribute<Cliente, String> foto;
        public static volatile SingularAttribute<Cliente, Usuario_> usuario;
}