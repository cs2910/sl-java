package Metodo.GET;

import java.sql.SQLException;

public class TabelaLancamento {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("lancamentosjf", "lancamentos");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
