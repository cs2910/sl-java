package Metodo.GET;

import Banco.Conexao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetTabelas {
    public static void executeGet(String tabela, String apibanco) throws SQLException {
        Connection con = new Conexao().conexaoBanco();
        ResultSet pagamento = con. createStatement().executeQuery("select * from "+tabela+" where id_cloud is null and protocolo <> 'null'");
        // Verifica os Protocolos;
        while (pagamento.next()){
            String protocolo = pagamento.getString("protocolo");
            System.out.println(protocolo);
            String itemValorGet = "";
            try {
                String  token = "4f6a2f1a-8cfe-45b4-90e4-8909010bec68";
                // Verifar até ter sucesso
                while (!itemValorGet.equals("SUCESSO") ) {
                    // URL da API que você deseja acessar
                    String apiUrl = "https://tributos.betha.cloud/service-layer-tributos/api/"+apibanco+"/" + protocolo;
                    System.out.println("Api de consulta de lote: " + apiUrl);
                    // Criação da URL
                    URL url = new URL(apiUrl);

                    // Abertura da conexão HTTP
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Inclusão do header de autenticação

                    connection.setRequestProperty("Authorization", "Bearer " + token);

                    // Verifica se a resposta foi bem-sucedida (código 200)
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // Leitura da resposta da requisição
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.isNull("retorno") || jsonObject.getJSONArray("retorno").length() == 0) {
                            itemValorGet = "NAO_PROCESSADO";
                            System.out.println("O campo \"retorno\" não contém valor.");
                        } else {
                            JSONArray retornoArray = jsonObject.getJSONArray("retorno");
                            JSONObject retornoObject = retornoArray.getJSONObject(0);
                            itemValorGet = retornoObject.getString("status");
                            JSONObject idGerado = retornoObject.getJSONObject("idGerado");
                            long idCloud = idGerado.getLong("id");
                            //itemValorGet = "SUCESSO" ;
                            System.out.println(retornoObject.getString("idGerado"));
                           /* String idCloud = retornoObject.getString("idGerado");
                            idCloud = idCloud.substring(6,14);
                            idCloud = idCloud.replace("}","");*/
                            //  System.out.println(valor);
                            System.out.println("ola " + idCloud);

                            if (itemValorGet.equals("SUCESSO")){
                                Connection novo = new Conexao().conexaoBanco();
                                String comando = "update "+tabela+" set id_cloud = "+idCloud+" where protocolo = '"+protocolo+"';";
                                novo.createStatement().executeUpdate(comando);
                                novo.close();
                            }
                            System.out.println("Status: " + itemValorGet);
                        }
                        // Exibe a resposta
                        System.out.println(response.toString());
                    } else {
                        // Exibe o código de erro, caso a resposta não tenha sido bem-sucedida
                        System.out.println("Erro na requisição: " + connection.getResponseCode());
                    }

                    // Fecha a conexão
                    connection.disconnect();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
