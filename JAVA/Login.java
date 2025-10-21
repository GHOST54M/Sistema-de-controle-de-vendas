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

@WebServlet("/Login")

public class Login extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        try (Connection conn = cnx.getConnection()) {
            String query = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("idusuario"); // Obter o ID do usuário

                // Criar uma sessão e armazenar o ID do usuário
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);

                response.sendRedirect("relatoriovendas.jsp");
            } else {
                response.sendRedirect("login.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=1");
        }
    }
}
