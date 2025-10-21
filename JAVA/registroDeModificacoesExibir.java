package bancodedados;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/registroDeModificacoesExibir")
public class registroDeModificacoesExibir extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public registroDeModificacoesExibir() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Definir a conexão e a consulta
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        System.out.println("salve");
        int userId = (int) session.getAttribute("userId");

        List<registroDeModificacoes> logs = new ArrayList<>();
        
        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = cnx.getConnectionForUser(userId);

            // Consulta para buscar os logs
            String query = "SELECT dados_novos, data_operacao FROM log_alteracoes ORDER BY data_operacao DESC";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            // Preencher a lista com os logs
            while (rs.next()) {
                String dadosNovos = rs.getString("dados_novos");
                Timestamp dataOperacao = rs.getTimestamp("data_operacao");
            }

            // Passar os logs para a JSP
            request.setAttribute("logs", logs);
            request.getRequestDispatcher("estoque.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("estoque.jsp"); // Página de erro
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
