<%@ page import="bancodedados.ItemCarrinho" %>
<%@ page import="bancodedados.carrinhoCompras" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="bancodedados.Produto" %>
<%@ page import="bancodedados.consultarProdutos" %>
<%@ page import="bancodedados.cnx" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.*" %>


<%
if (session == null || session.getAttribute("userId") == null) {
    String redirectUrl = request.getContextPath() + "/index.jsp?redirected=true";
    response.sendRedirect(redirectUrl);
    return;
} 
%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/estilosRepetidos.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">  
    <meta charset="UTF-8">
    <title>Produtos</title>
    <style>
        .conteudo{
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-left: 135px;
        }
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            padding-top: 60px;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }
        .modal-content {
            background-color: #fefefe;
            margin: auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 500px;
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
<header>
    <section class="menu">
        <p>VENANO</p>
        <section>
            <a href="perfil.jsp">Perfil</a>
        </section>              
    </section>
</header>
<main>
	<section class="menu-vertical">
		<a href="novavenda.jsp" class="menu-vertical-elemento"><img src="imagens/novavenda.png" class="menu-vertical-img"><br>Nova venda</a>
		<a href="estoque.jsp" class="menu-vertical-elemento"><img src="imagens/estoque1.png" class="menu-vertical-img"><br>Estoque</a>
		<a href="relatoriovendas.jsp" class="menu-vertical-elemento"><img src="imagens/relatorio1.png" class="menu-vertical-img"><br>Relatorio</a>
	</section>

	<section class="titulo">
	   	<p>Produtos em estoque</p>
	</section>
	
	<section class="conteudo">
	    <%
		// Exibe mensagens de erro ou sucesso
		String mensagemParam = request.getParameter("mensagem");
		int mensagem = (mensagemParam != null) ? Integer.parseInt(mensagemParam) : -1;
	
		if (mensagem == 101) {
		    out.println("<p style='color:red;'>A quantidade selecionada não está disponível no estoque!</p>");
		} else if (mensagem == 200) {
		    out.println("<p style='color:green;'>Venda efetuada com sucesso!</p>");
		} else if (mensagem == 170) {
		    out.println("<p style='color:red;'>Nem todos os itens foram inseridos corretamente!</p>");
		} else if (mensagem == 171) {
		    out.println("<p style='color:red;'>Falha ao obter o ID da venda.</p>");
		} else if (mensagem == 143){
		    out.println("<p style='color:red;'>Erro: A venda não pôde ser registrada.</p>");		
		}
	    java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

	    %>

	    <!-- Formulário para adicionar ao carrinho -->
	    <form action="finalizarVenda.jsp" method="get">
	        <label for="produtoSelect">Escolha o produto:</label>
	        <select id="produtoSelect" name="id">
	            <%
	            Integer userId = (Integer) session.getAttribute("userId"); 
	            consultarProdutos produtoDAO = new consultarProdutos();
	            List<Produto> produtos = produtoDAO.listarProdutos(userId);

	            if (produtos != null && !produtos.isEmpty()) {
	                for (Produto produto : produtos) {
	            %>
	                <option value="<%= produto.getId() %>"><%= produto.getNome() %> - R$<%= produto.getPreco() %> - Disponível: <%= produto.getQuant() %></option>
	            <%
	                }
	            } else {
	            %>
	                <option disabled>Estoque vazio.</option>
	            <%
	            }
	            %>
	        </select>
	        
	        <label for="quantidade">Quantidade:</label>
	        <input type="number" id="quantidade" name="quantidade" min="1" value="1" required>

	        <!-- Hidden input para ação -->
	        <input type="hidden" name="action" value="add">

	        <button type="submit">Adicionar ao Carrinho</button>
	    </form>
	    
			<%	
			
		    Connection conn = null;
			conn = cnx.getConnectionForUser(userId);
            carrinhoCompras verCarrinho = new carrinhoCompras();
            List<ItemCarrinho> carrinho = verCarrinho.obterItensCarrinho(conn);				
			    if (carrinho != null && !carrinho.isEmpty()) {
			%>
			    <table border="1">
			        <thead>
			            <tr>
			                <th>Nome do Produto</th>
			                <th>Quantidade</th>
			                <th>Preço</th>

			            </tr>
			        </thead>
			        <tbody>
			            <%
			                double totalCarrinho = 0.0;
			                for (ItemCarrinho item : carrinho) {
			                    double totalItem = item.getPreco() * item.getQuantidade();
			                    totalCarrinho += totalItem;
			            %>
			                <tr>
			                    <td><%= item.getNome() %></td>
			                    <td><%= item.getQuantidade() %></td>
			                    <td>R$ <%= item.getPreco() %></td>
			                </tr>
			            <% } %>
			        </tbody>
			    </table>
			
				<h3>Total do Carrinho: R$ <%= df.format(totalCarrinho) %></h3>
			<%
			    } else {
			%>
			    <p>Seu carrinho está vazio.</p>
			<%
			    }
			%>
	
	    <!-- Formulário para finalizar a compra, incluindo os itens do carrinho -->
	    <form action="finalizarVenda.jsp" method="post">
	        <!-- Campos ocultos para os itens do carrinho -->
	        <%
	        if (carrinho != null && !carrinho.isEmpty()) {
	            for (ItemCarrinho item : carrinho) {
	        %>
	            <input type="hidden" name="ids[]" value="<%= item.getId() %>">
	            <input type="hidden" name="quantidades[]" value="<%= item.getQuantidade() %>">
	        <%
	            }
	        }
	        %>
	        
	        <!-- Campo oculto para a ação de checkout -->
	        <input type="hidden" name="action" value="checkout">
	        <button type="submit">Finalizar Compra</button>
	    </form>
	    
	</section>

</main>
</body>
</html>
