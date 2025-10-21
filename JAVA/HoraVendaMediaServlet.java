package bancodedados;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import jakarta.servlet.http.HttpSession;

public class HoraVendaMediaServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Método que será chamado quando o servlet for acessado
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // Crie uma instância de HoraVendaMedia com a conexão
			System.out.println("Estamos no servlet");

            HoraVendaMedia horaVendaMedia = new HoraVendaMedia();

            // Obtenha o resultado da hora mais frequente
            HoraComMaisVendas resultado = null;
            
            HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

            if (session == null || session.getAttribute("userId") == null) {
                // Redireciona para a página de login se não houver sessão ou userId
                response.sendRedirect("login.jsp");
                return;
            }
            
            int userId = (int) session.getAttribute("userId");
			try {
				resultado = horaVendaMedia.calcularHoraMaisFrequente(userId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Passe o resultado para o JSP
            request.setAttribute("horaMaisFrequente", resultado);
			System.out.println(resultado);

            // Redirecione para o arquivo JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("relatoriovendas.jsp");
            dispatcher.forward(request, response);
        
    }
}
