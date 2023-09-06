package Metodo.GET;

import java.sql.SQLException;
public class TabelaGuias {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("guias", "guias");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
