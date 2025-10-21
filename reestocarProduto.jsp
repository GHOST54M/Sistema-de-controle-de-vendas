<%@ page import="java.sql.*" %>
<%@ page import="bancodedados.cnx" %>
<%
// Verifica se a sess�o est� ativa
if (session == null || session.getAttribute("userId") == null) {
    // Se a sess�o n�o estiver ativa, redireciona para a p�gina index.jsp
    String redirectUrl = request.getContextPath() + "/index.jsp?redirected=true";
    response.sendRedirect(redirectUrl);
    return; // Assegura que o c�digo seguinte n�o seja executado
}
    String id = request.getParameter("id");
	// Declara vari�veis para armazenar os dados do produto

	String nome = "";
	
	if (id != null) {
	       try {
	        	
	            Integer userId = (Integer) session.getAttribute("userId"); 
	            String idbanco = null;

	            try {
	                idbanco = cnx.buscarIdBanco(userId);
	            } catch (Exception e) {
	                e.printStackTrace(); // Para depura��o
	                out.println("Erro ao buscar o banco de dados: " + e.getMessage());
	            }
	        	
	            String url = "jdbc:mysql://localhost:3306/banco_" + idbanco;
	            String usuario = "root";
	            String senha = "";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(url, usuario, senha);

            String sql = "SELECT nome FROM produtos WHERE idproduto = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
            	nome = rs.getString("nome");
            }
            // Fecha a conex�o e os objetos de manipula��o de dados
            rs.close();
            stmt.close();
            conexao.close();
        } catch (Exception e) {
            out.println("Erro ao conectar ou recuperar dados: " + e.getMessage());
        }
    } else {
        out.println("ID do produto n�o fornecido.");
    }
%>

<html>
<head>

		<meta charset="UTF-8">

<title>
</title>
</head>
<body>
    <!-- Formul�rio para inserir ID do produto e quantidade -->
    <form action="reestocarProduto" method="post">
    	<p>Reestocagem do produto: <%= nome %></p>
        <label for="quantidadeAdicionar">Quantidade:</label>
        <input type="number" id="quantidadeAdicionar" name="quantidadeAdicionar" required>
        <br>
        <label for="quantidadeAdicionar">Pre�o de venda:</label>
        <input type="number" step="0.01" id="custo" name="preco" required>
        <br>
        <label for="quantidadeAdicionar">Custo unit�rio:</label>
        <input type="number" step="0.01" id="custo" name="custo" required>
        
        <input type="hidden" id="id" name="id" value="<%= id %>" required>
        <br><br>
        <button type="submit">Reestocar</button>
    </form>

    <hr>

    <!-- Exibe a mensagem de erro ou sucesso -->
    <%
        String mensagemErro = (String) request.getAttribute("mensagemErro");
        String mensagemSucesso = (String) request.getAttribute("mensagemSucesso");

        if (mensagemErro != null) {
            out.println("<p style='color: red;'>" + mensagemErro + "</p>");
        } else if (mensagemSucesso != null) {
            out.println("<p style='color: green;'>" + mensagemSucesso + "</p>");
        }
    %>

</body>

</html>