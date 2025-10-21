package bancodedados;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
@WebServlet("/cadastroProdutoNfe")
public class cadastroProdutoNfe extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Produto> produtos = new ArrayList<>();
        
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        try {
            // Iterar sobre os parâmetros enviados
            int index = 0;
            while (request.getParameter("produtos[" + index + "].nome") != null) {
                String nome = request.getParameter("produtos[" + index + "].nome");
                String descricao = request.getParameter("produtos[" + index + "].descricao");
                double preco = Double.parseDouble(request.getParameter("produtos[" + index + "].preco"));
                double precoFabrica = Double.parseDouble(request.getParameter("produtos[" + index + "].precofabrica"));
                int quant = Integer.parseInt(request.getParameter("produtos[" + index + "].quant"));
                int quantMax = Integer.parseInt(request.getParameter("produtos[" + index + "].quantMax"));

                Produto produto = new Produto(0, nome, descricao, preco, precoFabrica, quant, quantMax);
                produtos.add(produto);

                index++;
            }

            // Inserir os produtos no banco de dados e logar no log_alteracoes
            try (Connection conn = cnx.getConnectionForUser(userId)) {
                String sql = "INSERT INTO produtos (nome, descricao, preco, precofabrica, quant, quantmax) VALUES (?, ?, ?, ?, ?, ?)";
                String logSql = "INSERT INTO log_alteracoes (tipo_operacao, tabela_afetada, dados_antigos, dados_novos) VALUES (?, ?, ?, ?)";
                
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                     
                    for (Produto produto : produtos) {
                        stmt.setString(1, produto.getNome());
                        stmt.setString(2, produto.getDescricao());
                        stmt.setDouble(3, produto.getPreco());
                        stmt.setDouble(4, produto.getPrecofabrica());
                        stmt.setInt(5, produto.getQuant());
                        stmt.setInt(6, produto.getQuantMax());
                        stmt.addBatch(); 

                        String dadosNovos = String.format("O produto: %s, foi cadastrado com a quantidade máxima de armazenamento de: %d unidades! Com o preço de: R$ %.2f por unidade!",
                                produto.getNome(), produto.getQuantMax(), produto.getPrecofabrica());
                        
                    
                        logStmt.setString(1, "INSERÇÃO");
                        logStmt.setString(2, "produtos");
                        logStmt.setString(3, null);  
                        logStmt.setString(4, dadosNovos);

                        logStmt.addBatch(); 
                    }
                    stmt.executeBatch();
                    logStmt.executeBatch();
                }
            }

            // Sucesso: adicionar mensagem de sucesso à requisição
            request.setAttribute("mensagem", "Produtos cadastrados com sucesso!");
            request.getRequestDispatcher("cadastroProdutoNotalFiscal.jsp").forward(request, response);

        } catch (Exception e) {
            // Em caso de erro: adicionar mensagem de erro à requisição
            e.printStackTrace();
            request.setAttribute("mensagem", "Erro ao cadastrar os produtos: " + e.getMessage());
            request.getRequestDispatcher("cadastroProdutoNotalFiscal.jsp").forward(request, response);
        }
    }
}
