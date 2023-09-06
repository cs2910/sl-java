package Metodo.GET;

import java.sql.SQLException;

public class TabelaPessoa {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("pessoas", "pessoas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
