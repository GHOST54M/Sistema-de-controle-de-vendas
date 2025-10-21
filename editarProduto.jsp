<%
// Verifica se a sessão está ativa
if (session == null || session.getAttribute("userId") == null) {
    // Se a sessão não estiver ativa, redireciona para a página index.jsp
    String redirectUrl = request.getContextPath() + "/index.jsp?redirected=true";
    response.sendRedirect(redirectUrl);
    return; // Assegura que o código seguinte não seja executado
}
%>

<!DOCTYPE html>
<%@ page import="java.sql.*" %>
<%@ page import="bancodedados.cnx" %>
<%
    String id = request.getParameter("id");
	// Declara variáveis para armazenar os dados do produto
	int idp = 0;
	String nome = "";
	int quantMax = 0;
	String descricao = "";
	
	if (id != null) {
        try {
        	
            Integer userId = (Integer) session.getAttribute("userId"); 
            String idbanco = null;

            try {
                idbanco = cnx.buscarIdBanco(userId);
            } catch (Exception e) {
                e.printStackTrace(); // Para depuração
                out.println("Erro ao buscar o banco de dados: " + e.getMessage());
            }
        	
            String url = "jdbc:mysql://localhost:3306/banco_" + idbanco;
            String usuario = "root";
            String senha = "";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(url, usuario, senha);

            
            
            String sql = "SELECT * FROM produtos WHERE idproduto = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            // Armazena os valores nas variáveis JSP se o produto for encontrado
            if (rs.next()) {
 
            	nome = rs.getString("nome");
            	descricao = rs.getString("descricao");
            	quantMax = rs.getInt("quantmax");
            	idp = rs.getInt("idproduto");
            }

            // Fecha a conexão e os objetos de manipulação de dados
            rs.close();
            stmt.close();
            conexao.close();
        } catch (Exception e) {
            out.println("Erro ao conectar ou recuperar dados: " + e.getMessage());
        }
    } else {
        out.println("ID do produto não fornecido.");
    }
%>
<html>
<head>
	
        <meta name="viewport" content="width=device-width, initial-scale=1">       
		<meta charset="UTF-8">
		
			<title>Editar Produto</title>
		<style>
		form{
		    display: flex;
    		flex-direction: column;
    		align-items: center;
		}		
		</style>
</head>
<body>

<header>
</header>

<main>

<% 
    String mensagemErro = request.getParameter("erro");
    if (mensagemErro != null) { 
%>
    <div style="color: red; font-weight: bold;">
        <%= mensagemErro %>
    </div>
<% 
    } 

    String mensagemSucesso = request.getParameter("sucesso");
    if (mensagemSucesso != null) { 
%>
    <div style="color: green; font-weight: bold;">
        <%= mensagemSucesso %>
    </div>
<% 
    } 
%>

	<section class="">
		<form action="editarProduto" method="POST" id="produtoForm">
	
			<input type="hidden" id="id" name="id" value="<%= idp %>" required>
			<label for="nome">Nome produto:</label>
			<input type="text" id="nome" name="nome" value="<%= nome %>" required>
						 						    
			<label for="descricao">Descrição:</label>
			<input type="text" id="descricao" name="descricao" value="<%= descricao %>" required>
						    
			<label for="quantmax">Quantidade máxima no estoque:</label>
			<input type="text" id="quantmax" name="quantmax" value="<%= quantMax %>" required>
			
			<input type="submit">
			
		</form>	
		
	</section>

</main>

</body>
</html>