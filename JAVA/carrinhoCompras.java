package bancodedados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/carrinhoCompras")
public class carrinhoCompras extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public carrinhoCompras() {
        super();
 
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }
        
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
        try (Connection conn = cnx.getConnectionForUser(userId)) {
            switch (action) {
                case "add":
                    adicionarAoCarrinho(request, response, conn);
                    return; // Não redirecionar aqui, a lógica do redirecionamento está no método
                case "remove":
                    removerDoCarrinho(request, conn);
                    response.sendRedirect("novavenda.jsp");
                    return; // Retorna após o redirecionamento
                case "checkout":
                    concluirVenda(request, response, conn);

                    return; // Retorna após o redirecionamento
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
    }

    private void adicionarAoCarrinho(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, ServletException, IOException {
        int produtoId = Integer.parseInt(request.getParameter("id"));
        int quantidade = Integer.parseInt(request.getParameter("quantidade"));

        // Verificar a quantidade disponível no estoque
        String sqlVerificacao = "SELECT quant FROM produtos WHERE idproduto = ?";
        try (PreparedStatement stmtVerificacao = conn.prepareStatement(sqlVerificacao)) {
            stmtVerificacao.setInt(1, produtoId);
            try (ResultSet rs = stmtVerificacao.executeQuery()) {
                if (rs.next()) {
                    int quantidadeDisponivel = rs.getInt("quant");

                    // Verificar se o produto já está no carrinho
                    String sqlVerificarCarrinho = "SELECT quantidade FROM carrinho WHERE produto_id = ?";
                    int quantidadeTotal = quantidade;
                    boolean produtoExisteNoCarrinho = false;

                    try (PreparedStatement stmtCarrinho = conn.prepareStatement(sqlVerificarCarrinho)) {
                        stmtCarrinho.setInt(1, produtoId);
                        try (ResultSet rsCarrinho = stmtCarrinho.executeQuery()) {
                            if (rsCarrinho.next()) {
                                // Produto já existe no carrinho
                                int quantidadeExistente = rsCarrinho.getInt("quantidade");
                                quantidadeTotal += quantidadeExistente;
                                produtoExisteNoCarrinho = true;
                            }
                        }
                    }

                    // Verifica se a quantidade total desejada é superior à disponível no estoque
                    if (quantidadeTotal > quantidadeDisponivel) {
                        response.sendRedirect("novavenda.jsp?mensagem=101");
                        return; // Interrompe a execução do método
                    } else {
                        if (produtoExisteNoCarrinho) {
                            // Atualiza a quantidade se o produto já estava no carrinho
                            String sqlAtualizarCarrinho = "UPDATE carrinho SET quantidade = ? WHERE produto_id = ?";
                            try (PreparedStatement stmtAtualizar = conn.prepareStatement(sqlAtualizarCarrinho)) {
                                stmtAtualizar.setInt(1, quantidadeTotal);
                                stmtAtualizar.setInt(2, produtoId);
                                stmtAtualizar.executeUpdate();
                            }
                        } else {
                            // Insere o produto no carrinho caso não esteja presente
                            String sqlInsercao = "INSERT INTO carrinho (produto_id, quantidade) VALUES (?, ?)";
                            try (PreparedStatement stmtInserir = conn.prepareStatement(sqlInsercao)) {
                                stmtInserir.setInt(1, produtoId);
                                stmtInserir.setInt(2, quantidade);
                                stmtInserir.executeUpdate();
                            }
                        }
                        response.sendRedirect("novavenda.jsp");
                    }
                }
            }
        }
    }

    private void removerDoCarrinho(HttpServletRequest request, Connection conn) throws SQLException {
        int produtoId = Integer.parseInt(request.getParameter("id"));
        String sql = "DELETE FROM carrinho WHERE produto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoId);
            stmt.executeUpdate();
            
            System.out.println("Produto ID " + produtoId + " removido do carrinho!");
        }
    }

    public List<ItemCarrinho> obterItensCarrinho(Connection conn) throws SQLException {
        List<ItemCarrinho> itens = new ArrayList<>();
        String sql = "SELECT c.produto_id, p.nome, p.preco, p.precofabrica, c.quantidade FROM carrinho c JOIN produtos p ON c.produto_id = p.idproduto";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                itens.add(new ItemCarrinho(rs.getInt("produto_id"), rs.getString("nome"),
                                           rs.getDouble("preco"), rs.getInt("quantidade"), rs.getDouble("precofabrica")));
            }
        }
        return itens;
    }

    private void concluirVenda(HttpServletRequest request, HttpServletResponse response, Connection conn) throws SQLException, ServletException, IOException {
        // Obtenha os itens do carrinho e calcule o total
        List<ItemCarrinho> itensCarrinho = obterItensCarrinho(conn);
        double valorTotal = itensCarrinho.stream()
                                         .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                                         .sum();

        // Obtenha o horário atual
        java.time.LocalDateTime horarioAtual = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String horarioVenda = horarioAtual.format(formatter);

        // Insira uma nova venda na tabela `vendas`
        String sqlVenda = "INSERT INTO vendas (valor_total, horario_venda) VALUES (?, ?)";
        try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
            stmtVenda.setDouble(1, valorTotal);
            stmtVenda.setString(2, horarioVenda);  // Adiciona o horário da venda
            int linhasAfetadasVenda = stmtVenda.executeUpdate();  // Verifica linhas afetadas para vendas

            // Verificar se a venda foi inserida com sucesso
            if (linhasAfetadasVenda == 0) {
                System.err.println("Erro: A venda não pôde ser registrada.");
                response.sendRedirect("novavenda.jsp?mensagem=143");
                return;
            }

            // Obtenha o ID da venda inserida
            ResultSet generatedKeys = stmtVenda.getGeneratedKeys();
            if (generatedKeys.next()) {
                int vendaId = generatedKeys.getInt(1);

                // Insira cada item do carrinho na tabela `itens_venda`
                String sqlItem = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_de_venda, preco_de_fabrica) VALUES (?, ?, ?, ?, ?)";
                int totalLinhasAfetadasItens = 0;
                try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
                    for (ItemCarrinho item : itensCarrinho) {
                        stmtItem.setInt(1, vendaId);
                        stmtItem.setInt(2, item.getId());
                        stmtItem.setInt(3, item.getQuantidade());
                        stmtItem.setDouble(4, item.getPreco());
                        stmtItem.setDouble(5, item.getPrecofabrica());
                        totalLinhasAfetadasItens += stmtItem.executeUpdate();  // Soma linhas afetadas para itens

                        // Atualize a quantidade do produto na tabela `produtos` após a venda
                        String sqlAtualizaProduto = "UPDATE produtos SET quant = quant - ? WHERE idproduto = ?";
                        try (PreparedStatement stmtAtualizaProduto = conn.prepareStatement(sqlAtualizaProduto)) {
                            stmtAtualizaProduto.setInt(1, item.getQuantidade());  // Subtrai a quantidade vendida
                            stmtAtualizaProduto.setInt(2, item.getId());  // Produto ID
                            stmtAtualizaProduto.executeUpdate();  // Executa a atualização
                        }
                    }
                }

                // Verificar se todos os itens foram inseridos com sucesso
                if (totalLinhasAfetadasItens == itensCarrinho.size()) {
                    // Limpe o carrinho após a conclusão da venda
                    limparCarrinho(conn);
                    // Exibe mensagem de sucesso no console
                    System.out.println("Venda efetuada com sucesso. ID da venda: " + vendaId + ", Valor total: " + valorTotal);
                    response.sendRedirect("novavenda.jsp?mensagem=200");
                } else {
                    System.err.println("Erro: Nem todos os itens foram inseridos corretamente.");
                    response.sendRedirect("novavenda.jsp?mensagem=170");
                }
            } else {
                System.err.println("Erro: Falha ao obter o ID da venda.");
                response.sendRedirect("novavenda.jsp?mensagem=171");
            }
        }
    }


    
    private void limparCarrinho(Connection conn) throws SQLException {
        String sql = "DELETE FROM carrinho";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

}