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

public class Lancamento {
    public static void main(String[] args) throws SQLException {
            Connection con = new Conexao().conexaoBanco();
            ResultSet lancamento = con.createStatement().executeQuery("select * from lancamentos where protocolo is null");
            while (lancamento.next()) {
                // Acessando os valores das colunas individualmente
                String id_refer = lancamento.getString("codigoreferente");
                String idPessoa = lancamento.getString("idpessoa");
                String idCreditosTributarios = lancamento.getString("idcreditostributarios");
                String ano = lancamento.getString("ano");
                String idEconomicos = lancamento.getString("ideconomicos");
                String situacao = lancamento.getString("situacao");
                String dhLancamento = lancamento.getString("dhlancamento");
                String tipoReferente = lancamento.getString("tiporeferente");

                String complementar = "SIM";
                String lancamentoEnglobado = "NAO";
                String itemValor = null;

                String urlPost = "https://tributos.betha.cloud/service-layer-tributos/api/lancamentos";

                // Dados para enviar na requisição em formato JSON
                String jsonBody = "[\n" +
                        " {\n" +
                        "  \"idIntegracao\": \"AJUSTELANC\",\n" +
                        " \"lancamentos\": {\n" +
                        "\"idPessoa\":"+idPessoa+",\n" +
                        "\"idCreditosTributarios\":"+idCreditosTributarios+",\n" +
                        "\"ano\":"+ano+",\n" +
                        "\"complementar\":\""+complementar+"\",\n" +
                        "\"lancamentoEnglobado\":\""+lancamentoEnglobado+"\",\n" +
                        "\"idEconomicos\":"+idEconomicos+",\n" +
                        "\"situacao\":\""+situacao+"\",\n" +
                        "\"idPessoaAtual\":"+idPessoa+",\n" +
                        "\"dhLancamento\": \""+dhLancamento+"\",\n" +
                        "\"codigoReferente\":"+id_refer+",\n" +
                        "\"idReferente\":"+idEconomicos+",\n" +
                        "\"tipoReferente\": \""+tipoReferente+"\"\n" +
                        " }\n" +
                        " }\n" +
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
                con.createStatement().executeUpdate("update lancamentos set protocolo = '"+itemValor+"' where codigoreferente = "+lancamento.getString("codigoreferente"));

            }
            con.close();
    }
}
