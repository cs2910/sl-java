package Metodo.PATHAJF;

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

public class guiasjf {
    public static void main(String[] args) throws SQLException {
        Connection con = new Conexao().conexaoBanco();
        ResultSet divida = con.createStatement().executeQuery("select idguias, '{\"idIntegracao\": \"INTEGRACAO1\",\n" +
                "  \"guias\": {\n" +
                "    \"idGerado\": {\n" +
                "      \"id\": '||idguias ||'\n" +
                "    },\n" +
                "    \"situacao\": \"SUSPENSA\"\n" +
                "  }}' as nome from guiasjf where codigo > 4000");
        while (divida.next()){
            String itemValor = null;
            String jsonBody = divida.getString("nome");
            String div = divida.getString("idguias");
            String urlPost = "https://tributos.betha.cloud/service-layer-tributos/api/guias/";



            System.out.println(jsonBody);
            // ConectarBanco.executePost(urlPost,jsonBody,caminho,tabela);
            String  token = "c99b5c6c-16be-4d0c-9fed-8af564b13805";
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
            con.createStatement().executeUpdate("update guiasjf set protocolo = '"+itemValor+"' where idguias = "+div);
        }
    }

}
