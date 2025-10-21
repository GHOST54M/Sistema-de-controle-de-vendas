package bancodedados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/editarProduto")
public class editarProduto extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public editarProduto() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obter os parâmetros do formulário e converter para os tipos apropriados
        String id = request.getParameter("id");
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String quantMax = request.getParameter("quantmax");

        int idconv, quantmaxconv;
        double precoconv;

        try {
            idconv = Integer.parseInt(id);
            quantmaxconv = Integer.parseInt(quantMax);
        } catch (NumberFormatException e) {
            response.sendRedirect("editarProduto.jsp?id=" + id + "&erro=" + java.net.URLEncoder.encode("Valores inválidos!", "UTF-8"));
            return;
        }

        String sqlSelect = "SELECT nome, descricao, quantMax FROM produtos WHERE idproduto = ?";
        String sqlUpdate = "UPDATE produtos SET nome = ?, descricao = ?, quantmax = ? WHERE idproduto = ?";
        String sqlLog = "INSERT INTO log_alteracoes (tipo_operacao, tabela_afetada, dados_antigos, dados_novos) VALUES (?, ?, ?, ?)";

        HttpSession session = request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try (Connection connection = cnx.getConnectionForUser(userId);
             PreparedStatement stmtSelect = connection.prepareStatement(sqlSelect);
             PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate);
             PreparedStatement stmtLog = connection.prepareStatement(sqlLog)) {

            // Primeiro, obter os dados antigos do produto
            stmtSelect.setInt(1, idconv);
            ResultSet rs = stmtSelect.executeQuery();
            String nomeAntigo = null;
            String descricaoAntiga = null;
            int quantMaxAntiga = 0;
            if (rs.next()) {
                nomeAntigo = rs.getString("nome");
                descricaoAntiga = rs.getString("descricao");
                quantMaxAntiga = rs.getInt("quantMax");
            }

            // Executar a atualização no banco de dados
            stmtUpdate.setString(1, nome);
            stmtUpdate.setString(2, descricao);
            stmtUpdate.setInt(3, quantmaxconv);
            stmtUpdate.setInt(4, idconv);

            int linhasAfetadas = stmtUpdate.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto atualizado com sucesso!");

                // Registro no log
                String dadosAntigos = String.format("Nome: %s, Descrição: %s, Quantidade Máxima: %d", nomeAntigo, descricaoAntiga, quantMaxAntiga);
                String dadosNovos = String.format("O produto: %s, possui uma nova quantidade máxima de: %d unidades. Nova descrição: %s", nome, quantmaxconv, descricao);

                stmtLog.setString(1, "EDIÇÃO");
                stmtLog.setString(2, "produtos");
                stmtLog.setString(3, dadosAntigos);
                stmtLog.setString(4, dadosNovos);
                stmtLog.executeUpdate();

                response.sendRedirect("editarProduto.jsp?id=" + id + "&sucesso=" + java.net.URLEncoder.encode("O produto foi editado com sucesso! Recarregue a página para visualizar as alterações!", "UTF-8"));
            } else {
                System.out.println("Nenhum produto encontrado com o ID fornecido.");
                response.sendRedirect("editarProduto.jsp?id=" + idconv + "&erro=" + java.net.URLEncoder.encode("Erro ao atualizar o produto.", "UTF-8"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("editarProduto.jsp?id=" + idconv + "&erro=" + java.net.URLEncoder.encode("Erro ao atualizar o produto.", "UTF-8"));
        }
    }
}
