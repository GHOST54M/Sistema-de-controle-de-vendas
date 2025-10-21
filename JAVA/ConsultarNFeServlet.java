package serviconfe;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ConsultarNFe")
public class ConsultarNFeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numeroNFe = request.getParameter("nfeNumber");

        if (numeroNFe != null && numeroNFe.length() == 44) {
            // Chama a API para obter os produtos da NFe
            List<Object> produtos = chamadaAPI.chamarAPI(numeroNFe);

            if (produtos != null && !produtos.isEmpty()) {
                // Consulta bem-sucedida, setando os produtos e o atributo de sucesso
                request.setAttribute("produtos", produtos);
                request.setAttribute("consultaSucesso", true);
            } else {
                // Consulta não retornou produtos, setando falha
                request.setAttribute("consultaSucesso", false);
            }

            // Encaminha a requisição para o JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("/cadastroProdutoNotalFiscal.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Número da NFe inválido.");
        }
    }
}
