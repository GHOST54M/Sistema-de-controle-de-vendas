<%@ page import="java.io.IOException" %>
<%@ page import="serviconfe.chamadaAPI" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
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
<html lang="pt-br">
<head>
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&display=swap" rel="stylesheet">
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&family=Italiana&display=swap" rel="stylesheet">
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&family=Italiana&display=swap" rel="stylesheet">				
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&family=IBM+Plex+Sans+Devanagari:wght@100;200;300;400;500;600;700&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&family=Italiana&display=swap" rel="stylesheet">       
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&family=Hubballi&family=IBM+Plex+Sans+Devanagari:wght@100;200;300;400;500;600;700&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&family=Italiana&display=swap" rel="stylesheet">
		
    <meta charset="UTF-8">
    <title>Consulta NFe</title>
    <style>
    .consulta{

    }
   	.lista, .consulta{
    list-style: none;
	font-style: normal;
    font-family: IBM Plex Sans Devanagari;
	line-height: 22.94px;
    }
    .consulta-form{
    font-size: 20px;
    }
    
    .campo{
    width: 410px;
    height:30px;
    font-family: IBM Plex Sans Devanagari;
    
    }
	.sucesso {
     color: green;
     font-weight: bold;
    }
	.erro {
	color: red;
	font-weight: bold;
	}
    </style>
</head>
<body>
<header>
</header>

<main>
	<section class="consulta">
	
	    <form action="ConsultarNFe" method="POST" class="consulta-form" onsubmit="return validarNFe()">
	        <label for="nfeNumber">Número da NFe (44 dígitos):</label>
	        <br>
	        <input type="text" id="nfeNumber" class="campo" name="nfeNumber" pattern="^\d{44}$" title="O número da NFe deve conter exatamente 44 dígitos numéricos" required>
	        <br>
	        <button type="submit">Consultar</button>
	    </form>
		<ul class="lista"> 	    
		<%
		List<Object> produtos = (List<Object>) request.getAttribute("produtos");
		    if (produtos != null && !produtos.isEmpty()) {
		        for (Object item : produtos) {
		            if (item instanceof bancodedados.Produto) {
		                // Cast para Produto se o item for uma instância de Produto
		                bancodedados.Produto produto = (bancodedados.Produto) item;
		%>
		
		       
				<li><strong><%= produto.getNome() %></strong></li>
		        <li><%= produto.getDescricao() %></li>
		        <li>R$<%= produto.getPrecofabrica() %></li>
		        <li><%= produto.getQuant() %> Unidade(s)</li>
		        <hr></hr>
		    </li>
		<%
		            } else if (item instanceof String) {
		                // Exibir mensagens de erro se o item for uma string
		%>  
		        <li style="color: red;"><%= item %></li>
		<%
		            }
		        }
		    } else {
		%>  
		        <li>Nenhum produto ou mensagem encontrada.</li>
		<%
		    }
		%>
		</ul>    
<%
		Boolean consultaSucesso = (Boolean) request.getAttribute("consultaSucesso")	;
		if(consultaSucesso != null){
%>
		<form action="cadastroProdutoNfe" method="post">
		        <%
		            if (produtos != null && !produtos.isEmpty()) {
		                int index = 0;
		                for (Object item : produtos) {
		                    if (item instanceof bancodedados.Produto) {
		                        bancodedados.Produto produto = (bancodedados.Produto) item;
		        %>
		 		<input type="hidden" name="produtos[<%= index %>].nome" value="<%= produto.getNome() %>">
		        <input type="hidden" name="produtos[<%= index %>].descricao" value="<%= produto.getDescricao() %>">
		        <input type="hidden" name="produtos[<%= index %>].preco" value="<%= produto.getPreco() %>">
		        <input type="hidden" name="produtos[<%= index %>].precofabrica" value="<%= produto.getPrecofabrica() %>">
		        <input type="hidden" name="produtos[<%= index %>].quant" value="<%= produto.getQuant() %>">
		        <input type="hidden" name="produtos[<%= index %>].quantMax" value="<%= produto.getQuantMax() %>">
		        <%
		        index++; } 
		        }
		      }
		        %>
		    <button type="submit">Cadastrar Produtos</button>
		</form>
<p>
        <%
            String mensagem = (String) request.getAttribute("mensagem");
            if (mensagem != null) {
                if (mensagem.contains("Erro")) {
                    // Mensagem de erro
        %>
            <span class="erro"><%= mensagem %></span>
        <%
                } else {
                    // Mensagem de sucesso
        %>
            <span class="sucesso"><%= mensagem %></span>
        <%
                }
            }
		}
        %>
    </p>

<script>
        // Função para consultar os dados do produto da NFe
        async function consultarProdutoNFe(event) {
            event.preventDefault(); // Impede o envio do formulário

            const nfeNumber = document.getElementById("nfeNumber").value;

            // Verifica se o número da NFe tem exatamente 44 dígitos
            if (!/^\d{44}$/.test(nfeNumber)) {
                alert("O número da NFe deve conter exatamente 44 dígitos.");
                return;
            }
    </script>
    
	</section>
</main>
    
<footer>
</footer>
</body>
</html>
