package bancodedados;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@WebServlet("/rentabilidadeGeral")
public class calculoRentabilidadeGeralExibir extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    // Método para processar a requisição e calcular a rentabilidade geral
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer ano = parseInteger(request.getParameter("ano"));
        Integer mes = parseInteger(request.getParameter("mes"));
        Integer dia = parseInteger(request.getParameter("dia"));

        calculoRentabilidadeGeral calculoRentabilidade = new calculoRentabilidadeGeral();
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        List<Rentabilidade> rentabilidadeGeral = calculoRentabilidade.calcularRentabilidadeGeral(ano, mes, dia,userId);
        
        request.setAttribute("rentabilidadeGeral", rentabilidadeGeral);
        System.out.println(rentabilidadeGeral);

        // Encaminhar a requisição para o JSP para exibição
        RequestDispatcher dispatcher = request.getRequestDispatcher("relatoriovendas.jsp");
        dispatcher.forward(request, response);
    }

    // Método auxiliar para tentar converter uma string para Integer
    private Integer parseInteger(String value) {
        try {
 
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;  
        }
    }
}
