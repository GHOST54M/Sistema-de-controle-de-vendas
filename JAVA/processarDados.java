package serviconfe;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import bancodedados.Produto;

public class processarDados {

    public static List<Object> processarResposta(String jsonResponse) {
        List<Object> produtosList = new ArrayList<>();

        try {
            // Parse da resposta JSON
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Verificar se o caminho para os produtos é válido
            if (jsonObject.has("nfeProc")) {
                JSONObject nfeProc = jsonObject.getJSONObject("nfeProc");
                if (nfeProc.has("NFe")) {
                    JSONObject nfe = nfeProc.getJSONObject("NFe");
                    if (nfe.has("infNFe")) {
                        JSONObject infNFe = nfe.getJSONObject("infNFe");

                        // Verificar se "det" é um JSONArray
                        if (infNFe.has("det")) {
                            JSONArray produtos = infNFe.getJSONArray("det");

                            // Verificar se o array "det" não está vazio
                            if (produtos.length() == 0) {
                                produtosList.add("Nenhum produto encontrado no array 'det'.");
                            } else {
                                // Loop para extrair os dados de cada produto
                                for (int i = 0; i < produtos.length(); i++) {
                                    JSONObject produtoJson = produtos.getJSONObject(i).getJSONObject("prod");

                                    // Criar o objeto Produto com base no JSON
                                    Produto produto = criarProduto(produtoJson);
                                    if (produto != null) {
                                        produtosList.add(produto);
                                    } else {
                                        produtosList.add("Erro ao processar o produto no índice " + i + ".");
                                    }
                                }
                            }
                        } else {
                            produtosList.add("A chave 'det' não foi encontrada dentro de 'infNFe'.");
                        }
                    } else {
                        produtosList.add("Chave 'infNFe' não encontrada dentro de 'NFe'.");
                    }
                } else {
                    produtosList.add("Chave 'NFe' não encontrada.");
                }
            } else {
                produtosList.add("Chave 'nfeProc' não encontrada.");
            }
        } catch (Exception e) {
            produtosList.add("Erro ao processar JSON: " + e.getMessage());
        }

        return produtosList;
    }

    // Método auxiliar para criar um objeto Produto a partir de um JSONObject
    private static Produto criarProduto(JSONObject produtoJson) {
        try {
            if (produtoJson.has("cProd") && produtoJson.has("xProd") && produtoJson.has("vProd")) {
                String nome = produtoJson.getString("xProd");
                double precoFabrica = produtoJson.getDouble("vProd");

                // Criar e retornar o objeto Produto
                return new Produto(0, nome, "Sem descrição", 1, precoFabrica, 1, 10);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Logar erros no processamento do produto
        }
        return null; // Retornar null se ocorrer um problema
    }
}
