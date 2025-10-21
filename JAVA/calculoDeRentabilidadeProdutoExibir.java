package bancodedados;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/produtosMaisRentaveis")
public class calculoDeRentabilidadeProdutoExibir extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	// Obtém os parâmetros de dia, mês e ano do formulário
        String diaStr = request.getParameter("dia");
        String mesStr = request.getParameter("mes");
        String anoStr = request.getParameter("ano");

        // Conversão dos parâmetros de String para Integer 
        Integer dia = (diaStr != null && !diaStr.isEmpty()) ? Integer.parseInt(diaStr) : null;
        Integer mes = (mesStr != null && !mesStr.isEmpty()) ? Integer.parseInt(mesStr) : null;
        Integer ano = (anoStr != null && !anoStr.isEmpty()) ? Integer.parseInt(anoStr) : null;
        
        System.out.println(dia);
        System.out.println(mes);
        System.out.println(ano);
        
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        calculoDeRentabilidadeProduto rentaveis = new calculoDeRentabilidadeProduto();
        List<ItemVenda> produtosRentaveis = rentaveis.listarProdutosMaisRentaveis(dia, mes, ano, userId);

        // Adiciona a lista de produtos rentáveis ao request para passar para o JSP
        request.setAttribute("produtosMaisRentaveis", produtosRentaveis);

        // Encaminha a requisição para o JSP para exibição dos dados
        RequestDispatcher dispatcher = request.getRequestDispatcher("relatoriovendas.jsp");
        dispatcher.forward(request, response);
    }
}
