package Metodo.GET;

import java.sql.SQLException;

public class TabelaItbiCompra {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("transfimoveisitenscompra", "transfImoveisItensCompra");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
