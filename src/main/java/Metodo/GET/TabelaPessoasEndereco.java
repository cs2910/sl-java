package Metodo.GET;

import java.sql.SQLException;

public class TabelaPessoasEndereco {
    public static void main(String[] args) {
        // Tabela no postgres e API
        try {
            GetTabelas.executeGet("pessoasenderecos", "pessoasEnderecos");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
