package bancodedados;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*") // Aplica o filtro a todas as páginas jsp
public class VerificacaoDeSessaoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro, se necessário
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response; // Converte para HttpServletResponse

        // Obtém a URI da requisição
        String path = httpRequest.getRequestURI();

        if (path.endsWith("/index.jsp") || path.endsWith("/login.jsp") || path.endsWith("/cadastroUsuario.jsp")) {
            chain.doFilter(request, response); // Continua o fluxo sem aplicar o filtro
            return; // Encerra a execução do método
        }
        
        // Verifica se a requisição é para um arquivo .jsp
        if (!path.endsWith(".jsp")) {
            chain.doFilter(request, response); // Continua o fluxo sem aplicar a verificação
            return;
        }


        HttpSession session = httpRequest.getSession(false); // Obtém a sessão sem criar uma nova

        // Verifica se a sessão está ativa
        if (session != null && session.getAttribute("userId") != null) {
            System.out.println("Sessão está online para o userId: " + session.getAttribute("userId"));
            chain.doFilter(request, response); // Continua o fluxo para o próximo filtro ou servlet
        } else {
            // Redireciona apenas se o redirecionamento não foi feito antes
            if (httpRequest.getAttribute("redirected") == null) {
                System.out.println("Sessão encerrada. O usuário não está logado.");
                httpRequest.setAttribute("redirected", true); // Define um atributo para indicar redirecionamento
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
            }
        }
    }

    @Override
    public void destroy() {
        // Código para liberar recursos do filtro, se necessário
    }
}


