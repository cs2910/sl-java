package Metodo.GET;

import java.sql.SQLException;

public class TabelaImoveisC {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("imoveiscoresponsavel", "imoveis");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
