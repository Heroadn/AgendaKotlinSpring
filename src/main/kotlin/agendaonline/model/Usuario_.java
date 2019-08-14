package agendaonline.model;

import javax.persistence.metamodel.SingularAttribute;

public class Usuario_ {
    public static volatile SingularAttribute<Usuario, Long> id;
    public static volatile SingularAttribute<Usuario, String> nome;
    public static volatile SingularAttribute<Usuario, String> email;
    public static volatile SingularAttribute<Usuario, String> senha;
    public static volatile SingularAttribute<Usuario, String> celular;
    public static volatile SingularAttribute<Usuario, String> foto;
    public static volatile SingularAttribute<Usuario, Boolean> ativo;
    public static volatile SingularAttribute<Usuario, Cliente> cliente;
}
