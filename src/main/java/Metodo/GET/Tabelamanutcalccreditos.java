package Metodo.GET;

import java.sql.SQLException;

public class Tabelamanutcalccreditos {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("manutcalccreditos", "manutCalcCreditos");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
