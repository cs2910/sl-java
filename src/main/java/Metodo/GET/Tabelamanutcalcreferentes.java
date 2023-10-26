package Metodo.GET;

import java.sql.SQLException;

public class Tabelamanutcalcreferentes {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("manutcalcreferentes", "manutCalcReferentes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
