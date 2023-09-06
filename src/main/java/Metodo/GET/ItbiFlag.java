package Metodo.GET;

import java.sql.SQLException;

public class ItbiFlag {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("transfimoveisitens", "transfImoveisItens");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
