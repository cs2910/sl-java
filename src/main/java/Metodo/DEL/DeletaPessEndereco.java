package Metodo.DEL;

import Banco.Conexao;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.net.URI;

import java.net.http.HttpRequest;



public class DeletaPessEndereco {
    public static void main(String[] args) throws SQLException {
        Connection con = new Conexao().conexaoBanco();
        ResultSet divida = con.createStatement().executeQuery("select idCloud, '{\"idIntegracao\": \"INTEGRACAO1\",\"pessoasEnderecos\": {\"idGerado\": {\"id\": '||idcloud ||'}}}'\n" +
                "as idJson from pessoasenderecos where protocolo is null  and idcontribuinte  = 58433340");
        while (divida.next()){
            String idcloud = divida.getString("idcloud");
            String jsonBody = divida.getString("idJson");
            String itemValor = null;
            System.out.println(idcloud);
            String urlPost = "https://tributos.betha.cloud/service-layer-tributos/api/pessoasEnderecos/";

            System.out.println(jsonBody);
            // ConectarBanco.executePost(urlPost,jsonBody,caminho,tabela);
            String  token = "4f6a2f1a-8cfe-45b4-90e4-8909010bec68";
            System.out.println("Json: " + jsonBody);
            // Criar cliente HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Criar requisição delete com o corpo em JSON
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlPost))
                    .header("Authorization", "Bearer 4f6a2f1a-8cfe-45b4-90e4-8909010bec68")
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            try {
                // Enviar requisição
                HttpResponse<String> responsePost = client.send(request, HttpResponse.BodyHandlers.ofString());


                // Processar resposta
                int statusCode = responsePost.statusCode();
                HttpHeaders headers = responsePost.headers();
                String responseBody = responsePost.body();

                System.out.println("Código de status: " + statusCode);
                //   System.out.println("Headers: " + headers);
                //   System.out.println("Corpo da resposta: " + responseBody);

                JSONObject jsonResponse = new JSONObject(responseBody);
                itemValor = jsonResponse.getString("idLote");
                System.out.println("Valor do item: " + itemValor);


            } catch (Exception e) {
                e.printStackTrace();
            }
            con.createStatement().executeUpdate("update pessoasenderecos set protocolo = '"+itemValor+"' where idcloud = "+idcloud);
        }
    }


}
