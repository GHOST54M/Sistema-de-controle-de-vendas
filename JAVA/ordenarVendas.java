package bancodedados;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ordenarVendas")
public class ordenarVendas extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ordenarVendas() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Recebe os parâmetros do formulário
        String ordem = request.getParameter("ordem");
        String diaStr = request.getParameter("dia");
        String mesStr = request.getParameter("mes");
        String anoStr = request.getParameter("ano");

        // Converte os parâmetros para os tipos apropriados
        Integer dia = (diaStr != null && !diaStr.isEmpty()) ? Integer.parseInt(diaStr) : null;
        Integer mes = (mesStr != null && !mesStr.isEmpty()) ? Integer.parseInt(mesStr) : null;
        Integer ano = (anoStr != null && !anoStr.isEmpty()) ? Integer.parseInt(anoStr) : null;

        // Verifica se a ordem de ordenação é válida, senão define como 'desc'
        if (ordem == null || (!ordem.equalsIgnoreCase("asc") && !ordem.equalsIgnoreCase("desc"))) {
            ordem = "desc";
        }

        // Obtém o UserId da sessão ativa
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            // Caso não encontre o UserId na sessão, redireciona para a página de login ou uma página de erro
            response.sendRedirect("login.jsp");
            return;
        }

        // Cria a instância de consultarVendas
        consultarVendas vendasLista = new consultarVendas();

        // Converte o dia (se fornecido) para LocalDate
        LocalDate diaLocal = null;
        if (dia != null) {
            // Considera o mês e ano atuais se não forem fornecidos
            if (mes == null) mes = LocalDate.now().getMonthValue();
            if (ano == null) ano = LocalDate.now().getYear();
            diaLocal = LocalDate.of(ano, mes, dia);
        }

        // Chama o método listarVendas com os parâmetros apropriados, incluindo userId
        List<Venda> vendas = vendasLista.listarVendas(ordem, diaLocal, mes, ano, userId);

        // Passa as vendas para o JSP
        request.setAttribute("vendas", vendas);

        // Redireciona para o JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("relatoriovendas.jsp");
        dispatcher.forward(request, response);
    }
}
