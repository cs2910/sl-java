package Metodo.GET;

import java.sql.SQLException;

public class Tabelaccreditosrec {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("manutcalccreditosrec", "manutCalcCreditosRec");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
