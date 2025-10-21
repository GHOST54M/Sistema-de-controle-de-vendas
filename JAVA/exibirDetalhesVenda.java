package bancodedados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/exibirDetalhesVenda")
public class exibirDetalhesVenda extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public exibirDetalhesVenda() {
        super();
 
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
  
        String id = request.getParameter("id");
      	System.out.println(id);
        int Id = Integer.parseInt(id);
        
        HttpSession session = (HttpSession) request.getSession(false); // false para não criar nova sessão se não existir

        if (session == null || session.getAttribute("userId") == null) {
            // Redireciona para a página de login se não houver sessão ou userId
            response.sendRedirect("login.jsp");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        
    	Connection conn;
		try {
			conn = cnx.getConnectionForUser(userId);
	        List<ItensDetalhados> itens = obterItensVenda(conn , Id);
	     // Criando o conteúdo HTML dinâmico
            StringBuilder html = new StringBuilder();
            html.append("<html><head><title>Detalhes da Venda</title></head><body>");
            html.append("<h2>Detalhes da Venda: ").append(Id).append("</h2>");
            html.append("<div class='itens'>");

            // Iterando sobre a lista de itens e criando as divs para exibir os dados
            for (ItensDetalhados item : itens) {
                html.append("<ul class='item' style='list-style-type: none; padding: 10px;'>");
                html.append("<li style='padding-bottom: 10px;'>").append(item.getNome()).append("</li>");
                html.append("<li style='padding-bottom: 10px;'>").append(item.getQuantidade()).append(" Unidade(s)</li>");
                html.append("<li style='padding-bottom: 10px;'> R$").append(item.getPrecoVenda()).append(" Por unidade</li>");
                html.append("<hr></hr>");
                html.append("</ul>");
            }

            html.append("</div>");
            html.append("</body></html>");

            // Definindo o tipo de conteúdo da resposta
            response.setContentType("text/html");
            response.getWriter().write(html.toString());	
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
 	
}
    private List<ItensDetalhados> obterItensVenda(Connection conn, int vendaId) throws SQLException {
        List<ItensDetalhados> itens = new ArrayList<>();
        
        // SQL para obter os dados relacionados entre as tabelas
        String sql = "SELECT \r\n"
        		+ "    i.id AS item_id, \r\n"
        		+ "    i.quantidade, \r\n"
        		+ "    p.nome, \r\n"
        		+ "    p.precofabrica,\r\n"
        		+ "    p.preco\r\n"
        		+ "FROM \r\n"
        		+ "    itens_venda i\r\n"
        		+ "JOIN \r\n"
        		+ "    produtos p ON i.produto_id = p.idproduto\r\n"
        		+ "WHERE \r\n"
        		+ "    i.venda_id = ?;\r\n"
        		+ "";  // Parâmetro para o ID da venda

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Define o parâmetro da consulta (ID da venda)
            stmt.setInt(1, vendaId);

            // Executa a consulta
            try (ResultSet rs = stmt.executeQuery()) {
                // Itera sobre os resultados
                while (rs.next()) {
                    // Cria o objeto ItensDetalhados e popula com os dados da consulta
                    String nomeProduto = rs.getString("nome");
                    double precoProduto = rs.getDouble("precoFabrica");
                    int quantidadeVendida = rs.getInt("quantidade");
                    double precoVenda = rs.getDouble("preco");


                    // Adiciona os dados à lista
                    ItensDetalhados item = new ItensDetalhados(nomeProduto, quantidadeVendida, precoProduto, precoVenda);
                    itens.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // Propaga a exceção para ser tratada fora da função
        }

        return itens;
    }

}