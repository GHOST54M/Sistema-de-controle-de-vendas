package bancodedados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cadastroProduto")
public class cadastroProduto extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public cadastroProduto() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obter os parâmetros do formulário
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String quantMax = request.getParameter("quantmax");
        int quantmaxconv = Integer.parseInt(quantMax);

        String insertSql = "INSERT INTO produtos (nome, preco, precofabrica, descricao, quant, quantMax) VALUES (?, ?, ?, ?, ?, ?)";
        String logSql = "INSERT INTO log_alteracoes (tipo_operacao, tabela_afetada, dados_antigos, dados_novos) VALUES (?, ?, ?, ?)";

        HttpSession session = request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try (Connection connection = cnx.getConnectionForUser(userId);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql);
             PreparedStatement logStatement = connection.prepareStatement(logSql)) {

            // Inserção no banco
            insertStatement.setString(1, nome);
            insertStatement.setDouble(2, 0.0);
            insertStatement.setDouble(3, 0.0);
            insertStatement.setString(4, descricao);
            insertStatement.setInt(5, 0);
            insertStatement.setInt(6, quantmaxconv);

            int rowsInserted = insertStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Produto cadastrado com sucesso!");

                // Registro no log
                String dadosNovos = String.format("O produto: %s, foi cadastrado com a quantidade máxima de armazenamento de: %d unidades!",
                                                  nome, quantmaxconv);

                logStatement.setString(1, "INSERÇÃO");
                logStatement.setString(2, "produtos");
                logStatement.setString(3, null); //
                logStatement.setString(4, dadosNovos);

                logStatement.executeUpdate();
                response.sendRedirect("cadastroProduto.jsp?sucesso=" + java.net.URLEncoder.encode("O produto foi cadastrado com sucesso!", "UTF-8"));
            } else {
                System.out.println("Não foi possível cadastrar o produto.");
                response.sendRedirect("cadastroProduto.jsp?erro=" + java.net.URLEncoder.encode("Não foi possível cadastrar o produto!", "UTF-8"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir dados: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("cadastroProduto.jsp");
        }
    }
}
