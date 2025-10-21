package bancodedados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/excluirProduto")
public class excluirProduto extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public excluirProduto() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PreparedStatement stmt = null;
        
        String id = request.getParameter("id");
        int idconv = Integer.parseInt(id);
    
        String Sql = "DELETE FROM produtos WHERE idproduto = ?";   
        
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        try { 
            // Conectando ao banco
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection1 = cnx.getConnectionForUser(userId);

            // Obter o nome do produto antes de excluir
            String nomeProduto = getNomeProduto(connection1, idconv);
            
            if (nomeProduto == null) {
                response.sendRedirect("excluirProduto.jsp?erro=" + java.net.URLEncoder.encode("Produto não encontrado. Tente novamente.", "UTF-8"));
                return;
            }
            
            stmt = connection1.prepareStatement(Sql);
            stmt.setInt(1, idconv);
            
            // Executa a exclusão
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Produto excluído com sucesso!");

                // Registro da operação de remoção no log
                registrarOperacao(connection1, "REMOÇÃO", "O produto: " + nomeProduto + " foi excluído!" + " (ID do produto: " + idconv + ")");
                
                response.sendRedirect("excluirProduto.jsp?sucesso=" + java.net.URLEncoder.encode("O produto foi excluído com sucesso!", "UTF-8"));
            } else {
                System.out.println("Nenhum produto encontrado com o ID fornecido.");
                response.sendRedirect("excluirProduto.jsp?erro=" + java.net.URLEncoder.encode("Erro ao excluir o produto. Tente novamente.", "UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    // Método para obter o nome do produto pelo ID
    private String getNomeProduto(Connection conn, int idProduto) {
        String nomeProduto = null;
        String query = "SELECT nome FROM produtos WHERE idproduto = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idProduto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nomeProduto = rs.getString("nome");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nomeProduto;
    }

    // Método para registrar a operação de remoção no log
    private void registrarOperacao(Connection conn, String tipoOperacao, String dadosNovos) {
        String Sql = "INSERT INTO log_alteracoes (tipo_operacao, tabela_afetada, dados_novos, data_operacao) VALUES (?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = conn.prepareStatement(Sql)) {
            stmt.setString(1, tipoOperacao);
            stmt.setString(2, "produtos");
            stmt.setString(3, dadosNovos);

            stmt.executeUpdate();
            System.out.println("Operação registrada com sucesso no log.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao registrar a operação no log.");
        }
    }
}
