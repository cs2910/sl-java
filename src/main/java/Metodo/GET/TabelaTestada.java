package Metodo.GET;

import java.sql.SQLException;

public class TabelaTestada {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("imoveltestada", "imoveisTestadas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
