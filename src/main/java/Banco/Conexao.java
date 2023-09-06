package Banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    String url = "jdbc:postgresql://localhost:5432/serverlayer";
    String usuario = "postgres";
    String senha = "142536";

    public Connection conexaoBanco(){
        try {
            Connection conn = DriverManager.getConnection(url, usuario, senha);
            System.out.printf("Banco Conectado");
            return conn;
        } catch (SQLException e) {
            // Erro caso haja problemas para se conectar ao banco de dados
            e.printStackTrace();
            return null;
        }
    }
}
