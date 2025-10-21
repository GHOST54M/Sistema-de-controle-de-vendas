package bancodedados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class reestocarProduto {

    public String adicionarQuantidade(int idProduto, int quantidadeAdicionar, Double preco, Double custo, Integer idUsuario) {
        String consultaProduto = "SELECT quant, quantmax, nome FROM produtos WHERE idproduto = ?";
        String logOperacao = "INSERT INTO log_alteracoes (tipo_operacao, tabela_afetada, dados_novos) VALUES (?, ?, ?)";
        Connection conexao = null;

        try {
            conexao = cnx.getConnectionForUser(idUsuario);

            try (PreparedStatement stmt = conexao.prepareStatement(consultaProduto)) {
                stmt.setInt(1, idProduto);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int quantAtual = rs.getInt("quant");
                    int quantMax = rs.getInt("quantmax");
                    String nomeProduto = rs.getString("nome");

                    // Verifica se a quantidade a ser adicionada não ultrapassa o limite
                    if (quantAtual + quantidadeAdicionar <= quantMax) {
                        // Atualiza a quantidade do produto
                        String atualizarProduto = "UPDATE produtos SET quant = quant + ?, preco = ?, precofabrica = ? WHERE idproduto = ?";
                        try (PreparedStatement updateStmt = conexao.prepareStatement(atualizarProduto)) {
                            updateStmt.setInt(1, quantidadeAdicionar);
                            updateStmt.setDouble(2, preco);
                            updateStmt.setDouble(3, custo);
                            updateStmt.setInt(4, idProduto);

                            int rowsAffected = updateStmt.executeUpdate();
                            if (rowsAffected > 0) {
                                // Registro no log de alterações
                                try (PreparedStatement logStmt = conexao.prepareStatement(logOperacao)) {
                                    String dadosNovos = String.format("O produto: %s, foi reestocado em: %d, unidades com o preço de R$ %.2f ao custo de:R$ %.2f por unidade",
                                            nomeProduto, quantidadeAdicionar, preco, custo);

                                    logStmt.setString(1, "REESTOCAR");
                                    logStmt.setString(2, "produtos");
                                    logStmt.setString(3, dadosNovos);

                                    logStmt.executeUpdate();
                                }
                                return "Quantidade adicionada com sucesso e operação registrada.";
                            } else {
                                return "Erro ao atualizar o produto.";
                            }
                        }
                    } else {
                        return "Quantidade excede o limite máximo do produto.";
                    }
                } else {
                    return "Produto não encontrado.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao acessar o banco de dados: " + e.getMessage();
        } finally {
            try {
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
