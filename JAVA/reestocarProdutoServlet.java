package bancodedados;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/reestocarProduto")

public class reestocarProdutoServlet extends HttpServlet{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idProduto = Integer.parseInt(request.getParameter("id"));
        int quantidadeAdicionar = Integer.parseInt(request.getParameter("quantidadeAdicionar"));
        Double custo = Double.parseDouble(request.getParameter("custo"));
        Double preco = Double.parseDouble(request.getParameter("preco"));
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        reestocarProduto manager = new reestocarProduto();
        String mensagem = manager.adicionarQuantidade(idProduto, quantidadeAdicionar, preco, custo, userId);

        // Passa a mensagem para o JSP
        request.setAttribute("mensagemErro", mensagem);

        // Redireciona para o JSP
        request.getRequestDispatcher("/reestocarProduto.jsp").forward(request, response);
    }
}
