package agendaonline.model;

import javax.persistence.metamodel.SingularAttribute;

public class Token_ {
    public static volatile SingularAttribute<Token, Long> user_id;
    public static volatile SingularAttribute<Token, String> client_id;
    public static volatile SingularAttribute<Token, String> user_name;
    public static volatile SingularAttribute<Token, String> scope;
}
