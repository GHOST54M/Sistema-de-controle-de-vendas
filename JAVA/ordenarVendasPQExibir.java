package bancodedados;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/produtosMaisVendidos")
public class ordenarVendasPQExibir extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Método doGet que captura os parâmetros do formulário e chama o método de filtro
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Captura dos parâmetros do formulário
        String diaStr = request.getParameter("dia");
        String mesStr = request.getParameter("mes");
        String anoStr = request.getParameter("ano");

        // Conversão dos parâmetros de String para Integer (verificando se são nulos ou vazios)
        Integer dia = (diaStr != null && !diaStr.isEmpty()) ? Integer.parseInt(diaStr) : null;
        Integer mes = (mesStr != null && !mesStr.isEmpty()) ? Integer.parseInt(mesStr) : null;
        Integer ano = (anoStr != null && !anoStr.isEmpty()) ? Integer.parseInt(anoStr) : null;

        // Criação da instância do DAO que acessa o banco de dados
        ordenarVendasProdQuant vendaDAO = new ordenarVendasProdQuant();
        
        if (dia == null && mes == null && ano == null) {
            // Passa null para os parâmetros, o que fará a consulta usar a data atual
            dia = null;
            mes = null;
            ano = null;
        }
        HttpSession session = request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        // Chama o método para listar os produtos mais vendidos, passando os parâmetros de data
        List<ItemVenda> produtosMaisVendidos = vendaDAO.listarProdutosMaisVendidos(dia, mes, ano, userId);

        // Atribui a lista de produtos mais vendidos ao request
        request.setAttribute("produtosMaisVendidos", produtosMaisVendidos);

        // Exibe a lista na página JSP
        request.getRequestDispatcher("relatoriovendas.jsp").forward(request, response);
    }
}
