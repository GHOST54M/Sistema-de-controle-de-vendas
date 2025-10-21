package serviconfe;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class chamadaAPI {
    private static final String API_TOKEN = "06aef429-a981-3ec5-a1f8-71d38d86481e"; // Token fixo ou de ambiente

    public static List<Object> chamarAPI(String nfeNumber) {
        // URL da API com o número da NFe recebido
        String url = "https://gateway.apiserpro.serpro.gov.br/consulta-nfe-df-trial/api/v1/nfe/" + nfeNumber;

        // Cria um cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Cria a requisição GET com o cabeçalho Authorization para o token Bearer
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json") // Define o cabeçalho para receber JSON
                .header("Authorization", "Bearer " + API_TOKEN) // Adiciona o token Bearer
                .build();

        try {
            // Envia a requisição e armazena a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verifica o status da resposta
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();

                // Processa os dados e retorna os produtos
                return processarDados.processarResposta(jsonResponse);

            } else {
                System.out.println("Erro na resposta da API: Código " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
