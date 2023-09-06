package Metodo.GET;

import java.sql.SQLException;
public class TabelaGuiasReceitas {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("guiasreceitas", "guiasReceitas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
