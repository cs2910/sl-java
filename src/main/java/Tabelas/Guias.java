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

public class Guias {
    public static void main(String[] args) throws SQLException {
        Connection con = new Conexao().conexaoBanco();
        ResultSet lancamento = con.createStatement().executeQuery("select l.id_cloud,idindexadores,idpessoas,idpessoaatual,idcreditotributario,idreceitadiversa,\n" +
                "idreceitadiversalanctos,idimovel,ideconomico,idtransfimoveis,idcontribuicaomelhorias,\n" +
                "g.ano,datavcto,g.dhlancamento,nroparcela,g.situacao,valorlancado,valorpago,valordesconto,\n" +
                "unica,g.tiporeferente,lancamentoenglobado,complementar,origem,iguias  from guias g \n" +
                "inner join lancamentos l on\n" +
                "l.codigoreferente = g.idlancamentos " +
                " where g.protocolo is null");
        while (lancamento.next()) {
            // Acessando os valores das colunas individualmente
            String i_lanctos_cloud = lancamento.getString("id_cloud");
            String i_moedas_cloud = lancamento.getString("idindexadores");
            String id_pessoas = lancamento.getString("idpessoas");
            String id_pessoas_atual = lancamento.getString("idpessoaatual");
            String id_credito = lancamento.getString("idcreditotributario");
            String id_receitas_diversas = lancamento.getString("idreceitadiversa");
            String id_receitas_diversas_lanctos = lancamento.getString("idreceitadiversalanctos");
            String id_imoveis = lancamento.getString("idimovel");
            String id_economicos = lancamento.getString("ideconomico");
            String id_transferencia_imoveis = lancamento.getString("idtransfimoveis");
            String id_contribuicoes =  lancamento.getString("idcontribuicaomelhorias");
            String ano = lancamento.getString("ano");
            String data_vcto = lancamento.getString("datavcto");
            String data_lancto = lancamento.getString("dhlancamento");
            String parcela = lancamento.getString("nroparcela");
            String situacaocloud = lancamento.getString("situacao");
            String valor_gerado = lancamento.getString("valorlancado");
            String valorpago = lancamento.getString("valorpago");
            String valor_desconto = lancamento.getString("valordesconto");
            String unica = lancamento.getString("unica");
            String tiporeferente = lancamento.getString("tiporeferente");
            String lancto_englobado = lancamento.getString("lancamentoenglobado");
            String complementar = lancamento.getString("complementar");
            String origem = lancamento.getString("origem");
            String i_debitos = lancamento.getString("iguias");
            String itemValor = null;

            String data_credito = "";
            if (id_receitas_diversas.equals("")) id_receitas_diversas = null;
            if (id_receitas_diversas_lanctos.equals("")) id_receitas_diversas_lanctos = null;
            if (id_imoveis.equals("")) id_imoveis = null;
            if (id_economicos.equals("")) id_economicos = null;
            if (id_transferencia_imoveis.equals("")) id_transferencia_imoveis = null;
            if (id_contribuicoes.equals("")) id_contribuicoes = null;
            if (valor_gerado.equals(" ") ) valor_gerado = String.valueOf(1);
            if (valor_desconto.equals("")) valor_desconto = "0";
            if (situacaocloud.equals("PAGA")) data_credito = data_vcto;

            String urlPost = "https://tributos.betha.cloud/service-layer-tributos/api/guias";

            // Dados para enviar na requisição em formato JSON
            String jsonBody = "[\n" +
                    " {\n" +
                    "  \"idIntegracao\": \"AJUSTEINTEGRACAO\",\n" +
                    " \"guias\": {\n" +
                    "\"idLancamentos\":"+i_lanctos_cloud+",\n" +
                    "\"idIndexadores\":"+i_moedas_cloud+",\n" +
                    "\"idPessoas\":"+id_pessoas+",\n" +
                    "\"idPessoaAtual\":"+id_pessoas_atual+",\n" +
                    "\"idCreditoTributario\":"+id_credito+",\n" +
                    "\"idReceitaDiversa\":"+id_receitas_diversas+",\n" +
                    "\"idReceitaDiversaLanctos\":"+id_receitas_diversas_lanctos+",\n" +
                    "\"idImovel\":"+id_imoveis+",\n" +
                    "\"idEconomico\":"+id_economicos+",\n" +
                    "\"idTransfImoveis\":"+id_transferencia_imoveis+",\n" +
                    "\"idContribuicaoMelhorias\":"+id_contribuicoes+",\n" +
                    "\"ano\":"+ano+",\n" +
                    "\"dataVcto\": \""+data_vcto+"\",\n" +
                    "\"dhLancamento\": \""+data_lancto+"\",\n" +
                    "\"nroParcela\":"+parcela+",\n" +
                    "\"situacao\": \""+situacaocloud+"\",\n" +
                    "\"valorLancado\":"+valor_gerado+",\n" +
                    "\"valorPago\":"+valorpago+",\n" +
                    "\"valorDesconto\":"+valor_desconto+",\n" +
                    "\"unica\": \""+unica+"\",\n" +
                    "\"tipoReferente\": \""+tiporeferente+"\",\n" +
                    "\"lancamentoEnglobado\": \""+lancto_englobado+"\",\n" +
                    "\"complementar\": \""+complementar+"\",\n" +
                    "\"origem\": \""+origem+"\",\n" +
                    "\"iGuias\":"+i_debitos+"\n" +
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
            con.createStatement().executeUpdate("update guias set protocolo = '"+itemValor+"' where iguias = "+i_debitos);

        }
        con.close();
    }

}
