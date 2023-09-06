package Tabelas;

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

public class guiasReceitas {
    public static void main(String[] args) throws SQLException {
        Connection con = new Conexao().conexaoBanco();
        ResultSet lancamento = con.createStatement().executeQuery("select g.id_cloud ,gr.idreceitascreditos,gr.valorlancado,gr.valor_desc,gr.valorbeneficio,gr.valorpago,\n" +
                "gr.percbeneficiocorrecao,gr.percbeneficiojuros,gr.percbeneficiomulta,gr.percbeneficioreceita,gr.percdesconto, g.iguias from guiasreceitas gr\n" +
                "inner join guias g on\n" +
                "\tg.iguias = gr.idguias where gr.protocolo is null");
        while (lancamento.next()) {
            // Acessando os valores das colunas individualmente
            String idGuias = lancamento.getString("id_cloud");
            String idReceitasCreditos = lancamento.getString("idreceitascreditos");
            String valorLancado = lancamento.getString("valorlancado");
            String valorDesconto = lancamento.getString("valor_desc");
            String valorBeneficio = lancamento.getString("valorbeneficio");
            String valorPago = lancamento.getString("valorpago");
            String percBeneficioCorrecao = lancamento.getString("percbeneficiocorrecao");
            String percBeneficioJuros = lancamento.getString("percbeneficiojuros");
            String percBeneficioMulta = lancamento.getString("percbeneficiomulta");
            String percBeneficioReceita = lancamento.getString("percbeneficioreceita");
            String percDesconto = lancamento.getString("percdesconto");

            if (valorDesconto.equals("")) valorDesconto = "0";
            String itemValor = null;

            String urlPost = "https://tributos.betha.cloud/service-layer-tributos/api/guiasReceitas";

            // Dados para enviar na requisição em formato JSON
            String jsonBody = "[\n" +
                    "  {\n" +
                    "    \"idIntegracao\": \"AJUSTEINTEGRACAO\",\n" +
                    "    \"guiasReceitas\": {\n" +
                    "      \"idGuias\": "+idGuias+",\n" +
                    "      \"idReceitasCreditos\": "+idReceitasCreditos+",\n" +
                    "      \"valorLancado\": "+valorLancado+",\n" +
                    "      \"valorDesconto\": "+valorDesconto+",\n" +
                    "      \"valorBeneficio\": "+valorBeneficio+",\n" +
                    "      \"valorPago\":"+valorPago+",\n" +
                    "      \"percBeneficioCorrecao\":"+percBeneficioCorrecao+",\n" +
                    "      \"percBeneficioJuros\":"+percBeneficioJuros+",\n" +
                    "      \"percBeneficioMulta\": "+percBeneficioMulta+",\n" +
                    "      \"percBeneficioReceita\":"+percBeneficioReceita+",\n" +
                    "      \"percDesconto\": "+percDesconto+"\n" +
                    "      }\n" +
                    "  }\n" +
                    "]";

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
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
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
            con.createStatement().executeUpdate("update guiasreceitas set protocolo = '"+itemValor+"' where idguias = "+lancamento.getString("iguias"));

        }
        con.close();
    }
}
