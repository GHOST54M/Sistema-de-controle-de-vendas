<%
// Verifica se a sess�o est� ativa
if (session == null || session.getAttribute("userId") == null) {
    // Se a sess�o n�o estiver ativa, redireciona para a p�gina index.jsp
    String redirectUrl = request.getContextPath() + "/index.jsp?redirected=true";
    response.sendRedirect(redirectUrl);
    return; // Assegura que o c�digo seguinte n�o seja executado
}
%>
<!DOCTYPE html>

<html>
<head>
	
        <meta name="viewport" content="width=device-width, initial-scale=1">       
		<meta charset="UTF-8">
		
			<title>Cadastrar Produto</title>
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
	<section class="">
		<form id="formCadastroProduto" action="cadastroProduto" method="POST">
		
			<label for="nome">Nome produto:</label>
			<input type="text" id="nome" name="nome" required>
						    
			<label for="descricao">Descri��o:</label>
			<input type="text" id="descricao" name="descricao" required>
						    
			<label for="quantmax">Quantidade m�xima no estoque:</label>
			<input type="number" id="quantmax" name="quantmax" min="1" step="1" required>
			
			<input type="submit">
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
		</form>	
			<script src="validarCadastroProduto.js"></script>

	</section>

</main>

</body>
</html>