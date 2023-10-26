package Metodo.GET;

import java.sql.SQLException;

public class TabelaManutCalculo {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("manutencoescalculos", "manutencoesCalculos");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
