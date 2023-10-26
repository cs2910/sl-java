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

public class manutCalcCreditosRec {
    public static void main(String[] args) throws SQLException {
        Connection con = new Conexao().conexaoBanco();
        ResultSet divida = con.createStatement().executeQuery("select codigo,idlancamento,idcreditos, '{\n" +
                "    \"idIntegracao\": \"INTEGRACAO1\",\n" +
                "    \"manutCalcCreditosRec\": {\n" +
                "      \"idCreditosTributariosRec\": '||idcreditos ||',\n" +
                "      \"idManutencoesCalculos\": '||idmanutencao ||',\n" +
                "      \"idManutCalcReferentes\": '||idreferentes ||',\n" +
                "      \"selecionada\": \"SIM\",\n" +
                "      \"deferida\": \"NAO\",\n" +
                "      \"classificacaoRevisaoCalculo\": \"RETIFICACAO\",\n" +
                "      \"valorLancado\": '||valor ||',\n" +
                "      \"valorCorrecao\": 0,\n" +
                "      \"valorJuros\": 0,\n" +
                "      \"valorMulta\": 0,\n" +
                "      \"valorBeneficioLancado\": 0,\n" +
                "      \"valorBeneficioCorrecao\": 0,\n" +
                "      \"valorBeneficioJuros\": 0,\n" +
                "      \"valorBeneficioMulta\": 0,\n" +
                "      \"valorBeneficioLancadoReq\": 0,\n" +
                "      \"valorBeneficioCorrecaoReq\": 0,\n" +
                "      \"valorBeneficioJurosReq\": 0,\n" +
                "      \"valorBeneficioMultaReq\": 0,\n" +
                "      \"percLancadoReq\": 0,\n" +
                "      \"percLancado\": 1,\n" +
                "      \"percCorrecao\": 0,\n" +
                "      \"percCorrecaoReq\": 0,\n" +
                "      \"percJuros\": 0,\n" +
                "      \"percJurosReq\": 0,\n" +
                "      \"percMulta\": 0,\n" +
                "      \"percMultaReq\": 0,\n" +
                "      \"percReqAlterado\": 0,\n" +
                "      \"percAlterado\": 0,\n" +
                "      \"anosVigencia\": \"'||ano||'\"\n" +
                "    }\n" +
                "  }' as nome\n" +
                "  from manutcalccreditosrec m where codigo > 4001 and protocolo is null ");
        while (divida.next()){
            String itemValor = null;
            String jsonBody = divida.getString("nome");
            String div = divida.getString("idlancamento");
            String codcredito = divida.getString("codigo");
            String idcred = divida.getString("idcreditos");
            String urlPost = "https://tributos.betha.cloud/service-layer-tributos/api/manutCalcCreditosRec/";



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
            con.createStatement().executeUpdate("update manutcalccreditosrec set protocolo = '"+itemValor+"' " +
                    "where idlancamento = '"+div +"' and codigo = "+codcredito+" and idcreditos = '"+idcred+"'" );
        }
    }

}
