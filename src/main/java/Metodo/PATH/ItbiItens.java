package Metodo.PATH;

import Banco.Conexao;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItbiItens {
    public static void main(String[] args) throws SQLException {
        Connection con = new Conexao().conexaoBanco();
        ResultSet divida = con.createStatement().executeQuery("select * from transfimoveisitens where protocolo is null");
        while (divida.next()){
            String idcloud = divida.getString("idcloud");
            String financiado = "NAO";
            String itemValor = null;
            System.out.println(idcloud);
            String urlPost = "https://tributos.betha.cloud/service-layer-tributos/api/transfImoveisItens/";


            String jsonBody = "[\n" +
                    "  {\n" +
                    "    \"idIntegracao\": \"AJUSTEINTEGRACAO\",\n" +
                    "    \"transfImoveisItens\": {\n" +
                    "        \"idGerado\":{\n" +
                    "            \"id\": "+idcloud+"\n" +
                    "         },\n" +
                    "         \"financiado\": \""+financiado+"\"\n" +
                    "  }\n" +
                    "  }\n" +
                    "]";

            System.out.println(jsonBody);
            // ConectarBanco.executePost(urlPost,jsonBody,caminho,tabela);
            String  token = "4f6a2f1a-8cfe-45b4-90e4-8909010bec68";
            System.out.println("Json: " + jsonBody);
            // Criar cliente HttpClient
            HttpClient client = HttpClient.newHttpClient();

            // Criar requisição POST com o corpo em JSON
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlPost))
                    .header("Authorization", "Bearer " + token )
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
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
            con.createStatement().executeUpdate("update transfimoveisitens set protocolo = '"+itemValor+"' where idcloud = "+idcloud);
        }
    }
}
