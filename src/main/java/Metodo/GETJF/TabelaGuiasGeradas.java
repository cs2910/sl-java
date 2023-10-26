package Metodo.GETJF;

import Metodo.GET.GetTabelas;

import java.sql.SQLException;

public class TabelaGuiasGeradas {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("guiasgeradasjf", "manutCalcGuiasGeradas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
