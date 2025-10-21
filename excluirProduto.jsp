<%@ page import="java.sql.*" %>
<%@ page import="bancodedados.cnx" %>

<%
// Verifica se a sessão está ativa
if (session == null || session.getAttribute("userId") == null) {
    // Se a sessão não estiver ativa, redireciona para a página index.jsp
    String redirectUrl = request.getContextPath() + "/index.jsp?redirected=true";
    response.sendRedirect(redirectUrl);
    return; // Assegura que o código seguinte não seja executado
}
%>
<%
    String id = request.getParameter("id");
	// Declara variáveis para armazenar os dados do produto

	String nome = "";
	
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

            String sql = "SELECT nome FROM produtos WHERE idproduto = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
            	nome = rs.getString("nome");
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
		
			<title>Excluir Produto</title>
    <script>
        function validarFormulario() {
            // Verifica se a caixa de seleção está marcada
            const checkbox = document.getElementById("termos");

            if (!checkbox.checked) {
                alert("Você deve marcar a caixa para continuar.");
                return false; // Impede o envio do formulário
            }

            return true; // Permite o envio do formulário
        }
    </script>		
</head>
<body>

<header>
</header>

<main>
<%
    String mensagemSucesso = request.getParameter("sucesso");
    String mensagemErro = request.getParameter("erro");

    if (mensagemSucesso != null) {
%>
    <div style="color: green; font-weight: bold;">
        <%= mensagemSucesso %>
    </div>
<% 
    } else if (mensagemErro != null) { 
%>
    <div style="color: red; font-weight: bold;">
        <%= mensagemErro %>
    </div>
<% 
    } 
%>
	<h1>Tem certeza que deseja excluir o produto:<%= nome %> ?</h1>
	<h2>Essa ação NÃO poderá ser desfeita!</h2>
	<form action="excluirProduto" method="post" onsubmit="return validarFormulario()">
		<input type="hidden" id="id" name="id" value="<%= id %>" required>
	    <input type="checkbox" id="termos" name="termos">
        <label for="termos">Eu QUERO excluir esse produto!</label><br><br>
	<input type="submit">
	</form>
</main>

<footer>
</footer>

</body>