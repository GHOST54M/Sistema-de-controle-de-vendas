package bancodedados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/editarUsuario")

public class editarUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public editarUsuario() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obter os parâmetros do formulário
        PreparedStatement stmt = null;
        
        String id = request.getParameter("id");
        int idconv = Integer.parseInt(id);
        
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");        
        String celular = request.getParameter("Celular");       
        String Datanascimento = request.getParameter("Datanascimento");
        
        String Sql = "UPDATE usuario SET nomeusuario = ?, email = ?, celular = ?, Datanascimento = ? WHERE idusuario = ?";
        
        try { 
            // Conectando ao banco
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection1 = cnx.getConnection(); // Certifique-se de que `cnx.getConnection()` retorna uma conexão válida
             
            stmt = connection1.prepareStatement(Sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, celular);
            stmt.setString(4, Datanascimento);
            stmt.setInt(5, idconv);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Usuário editado!");
            } else {
                System.out.println("Não foi possível editar seus dados.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
        response.sendRedirect("perfil.jsp");
    }
    

}
