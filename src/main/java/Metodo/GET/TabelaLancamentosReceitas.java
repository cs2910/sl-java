package Metodo.GET;

import java.sql.SQLException;

public class TabelaLancamentosReceitas {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("lancamentosReceitas", "lancamentosReceitas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
