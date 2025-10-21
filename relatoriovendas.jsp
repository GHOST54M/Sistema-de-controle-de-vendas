<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="bancodedados.Venda" %>
<%@ page import="bancodedados.ItemVenda" %>
<%@ page import="bancodedados.Produto" %>
<%@ page import="bancodedados.consultarVendas" %>
<%@ page import="bancodedados.consultaItemVendas" %>
<%@ page import="bancodedados.consultarProdutos" %>
<%@ page import="bancodedados.ordenarVendas" %>
<%@ page import="bancodedados.ordenarVendasProdQuant" %>
<%@ page import="bancodedados.ordenarVendas" %>
<%@ page import="bancodedados.calculoDeRentabilidadeProduto" %>
<%@ page import="bancodedados.calculoDeRentabilidadeProdutoExibir" %>
<%@ page import="bancodedados.calculoRentabilidadeGeral" %>
<%@ page import="bancodedados.Rentabilidade" %>
<%@ page import="bancodedados.HoraVendaMedia" %>
<%@ page import="bancodedados.HoraComMaisVendas" %>

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
<html>
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
		        
        <meta name="viewport" content="width=device-width, initial-scale=1">       
		<meta charset="UTF-8">
		
		<title>Relatório de vendas</title>
    	
    	<link rel="stylesheet" type="text/css" href="css/relatoriovendas.css">	
    	<link rel="stylesheet" type="text/css" href="css/estilosRepetidos.css">	

    	<style>
    		
    		.titulo{
    		margin-left: 150px;
    		padding:20px;
    		font-family: "IBM Plex Sans Devanagari", sans-serif;
			font-style: normal;
			font-family: IBM Plex Sans Devanagari;
			font-size: 32px;
			font-weight: 300;
			line-height: 48.96px;
		    		
    		}
    		
    		.button-group{
    		display: flex;
    		width: (100% -135px);
    		margin-left:135px;
    		font-family: "IBM Plex Sans Devanagari", sans-serif;
			font-style: normal;
			font-family: IBM Plex Sans Devanagari;
			font-size: 20px;
			
    		}
    		
    		.button-group span {
		    flex: 1; 
		    text-align: center; 
		    padding: 10px;
		    cursor: pointer; 
		    border-bottom: 2px solid #3C3C81;
		    border-right: 2px solid #3C3C81; 
			}
			
    		.button-group span:hover {
		    background-color: #f0f0f0; 
			}
			
    		.ranking_posicionamento, .vendas-lucro-horario{
            display: flex;
    		justify-content: space-evenly;
			font-family: Inter;
			font-size: 16px;
			font-weight: 300;
			line-height: 19.36px;
			margin:25px;

        	}
        	
        	.rodutos-rentaveis, .produtos-mais-vendidos, .tabela-vendas, .margem-lucro, .lucro-horario{
        	display: flex;
		    flex-direction: column;
		    align-items: center;
        	width: 400px;
        	
        	}
	        .section-hidden {
			display: none;
			
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
	
<section class="button-group">
    <span onclick="toggleSection('vendas-section')">Relatório de vendas</span>
    <span onclick="toggleSection('produtos-section')">Relatório de produtos</span>
    <span onclick="toggleSection('lucro-section')">Relatórios gerais</span>
</section>

	
	<section class="titulo">
		<p>Relatórios</p>
	</section>
	
	<section id="produtos-section" class="ranking_posicionamento toggle-section section-hidden">
		<!-- Ranking produtos -->		
		<section class="rodutos-rentaveis">
			<p>Produtos mais rentáveis:</p>
				<form action="produtosMaisRentaveis" method="GET">
			        <label for="dia">Dia:</label>
			        <input type="number" id="dia" name="dia" min="1" max="31">
			        
			        <label for="mes">Mês:</label>
			        <input type="number" id="mes" name="mes" min="1" max="12">
			        
			        <label for="ano">Ano:</label>
			        <input type="number" id="ano" name="ano" min="1900" max="2100">
			    
			        <button type="submit">Filtrar</button>
			    </form>
					<table border="1">
					    <tr>
					        <th>N°</th>
					        <th>Nome do Produto</th>
					        <th>Rentabilidade</th>
					    </tr>
					    <% 
					        List<ItemVenda> produtosMaisRentaveis = (List<ItemVenda>) request.getAttribute("produtosMaisRentaveis");
					
					        if (produtosMaisRentaveis == null) {
					            // Cria uma nova instância de ordenarVendasProdQuant e busca os produtos mais rentáveis
					            Integer userId = (Integer) session.getAttribute("userId"); 
					            calculoDeRentabilidadeProduto prodQuant = new calculoDeRentabilidadeProduto();
					            produtosMaisRentaveis = prodQuant.listarProdutosMaisRentaveis(null, null, null, userId); // Padrão para a primeira exibição
					        }
					        
					        if (produtosMaisRentaveis != null && !produtosMaisRentaveis.isEmpty()) {
					            int numero = 1; // Variável contador para a coluna N°
					            for (ItemVenda produto : produtosMaisRentaveis) {
					    %>
					                <tr>
					                    <td><%= numero++ %></td> <!-- Preenche a coluna N° com o número sequencial -->
					                    <td><%= produto.getNomeProduto() %></td>
					                    <td>R$ <%= String.format("%.2f", produto.getRetabilidade()) %></td>
					                </tr>
					    <% 
					            }
					        } else { 
					    %>
					            <tr>
					                <td colspan="3">Nenhum produto encontrado.</td>
					            </tr>
					    <% 
					        }
					    %>
					</table>

		</section>
		
		<!-- Produtos mais vendidos -->		
		<section class="produtos-mais-vendidos">
		    <p>Produtos mais vendidos</p>
		    <!-- Formulário para filtrar os produtos vendidos -->
		    <form action="produtosMaisVendidos" method="POST">
		        <label for="dia">Dia:</label>
		        <input type="number" id="dia" name="dia" min="1" max="31">
		        
		        <label for="mes">Mês:</label>
		        <input type="number" id="mes" name="mes" min="1" max="12">
		        
		        <label for="ano">Ano:</label>
		        <input type="number" id="ano" name="ano" min="1900" max="2100">
		    
		        <button type="submit">Filtrar</button>
		    </form>
		    
		    <table border="1">
		        <tr>
		            <th>Vendidos</th>
		            <th>Nome do produto</th>
		        </tr>
		        <% 
		            // Recupera os produtos mais vendidos do atributo request
		            List<ItemVenda> produtosMaisVendidos = (List<ItemVenda>) request.getAttribute("produtosMaisVendidos");
					if(produtosMaisVendidos == null){
						Integer userId = (Integer) session.getAttribute("userId");
						ordenarVendasProdQuant MaisVendidos = new ordenarVendasProdQuant();
		            	produtosMaisVendidos = MaisVendidos.listarProdutosMaisVendidos(null, null, null, userId); 
		                
					}
		            // Verifica se a lista de produtos está vazia ou nula
		            if (produtosMaisVendidos != null && !produtosMaisVendidos.isEmpty()) {
		                for (ItemVenda produto : produtosMaisVendidos) {
		        %>	
		                    <tr>
		                        <td><%= produto.getTotalQuantidade() %></td>
		                        <td><%= produto.getNomeProduto() %></td>
		                    </tr>
		        <% 
		                }
		            } else {
		        %>
		                <tr>
		                    <td colspan="2">Nenhum produto encontrado para o filtro aplicado.</td>
		                </tr>
		        <% 
		            }
		        %>
		    </table>
		</section>
	
	</section>	
	
	<section id="vendas-section" class="vendas-lucro-horario toggle-section">		
				<!-- Tabela de vendas -->
		<section class="tabela-vendas">
					<p>Tabela de Vendas</p>
					
					<!-- Novo formulário para filtrar por dia, mês e ano -->
					<form action="ordenarVendas" method="post">
					
					    <label for="ordem">Ordenar por:</label>
					    <select name="ordem" id="ordem">
					        <option value="asc">Crescente</option>
					        <option value="desc">Decrescente</option>
					    </select>
					    
					    <label for="dia">Dia:</label>
					    <input type="number" id="dia" name="dia" min="1" max="31">
					
					    <label for="mes">Mês:</label>
					    <input type="number" id="mes" name="mes" min="1" max="12">
					
					    <label for="ano">Ano:</label>
					    <input type="number" id="ano" name="ano" min="2000" max="2100">
					
					    <button type="submit">Filtrar</button>
					</form>
					
					<!-- Tabela para exibir as vendas -->
					<table border="1">
					    <tr>
					        <th>ID</th>
					        <th>Data</th>
					        <th>Valor Total</th>
					    </tr>
					    <%
					        // Verifica se o atributo 'vendas' foi enviado pelo servlet
					        List<Venda> vendas = (List<Venda>) request.getAttribute("vendas");
					
					        // Se o atributo 'vendas' for nulo, esta é a primeira vez que a página está sendo acessada
					        if (vendas == null) {
					            // Cria uma nova instância de consultarVendas e busca as vendas com ordenação padrão
					            Integer userId = (Integer) session.getAttribute("userId"); // Cast para Integer
					            consultarVendas vendasLista = new consultarVendas();
					            vendas = vendasLista.listarVendas("desc", null, null, null, userId); // Padrão para a primeira exibição
					        }
					
					        // Exibe os dados de vendas na tabela
					        if (vendas != null && !vendas.isEmpty()) {
					            for (Venda venda : vendas) {
					    %>
					    <tr>
					        <td><button onclick="abrirDetalhesVendaModal('<%= venda.getId() %>')"><%= venda.getId() %></button></td>
					        <td><%= venda.getData() %></td>
					        <td>R$ <%= String.format("%.2f", venda.getValorTotal()) %></td>
					    </tr>
					    <%
					            }
					        } else {
					    %>
					    <tr>
					        <td colspan="3">Nenhuma venda encontrada.</td>
					    </tr>
					    <% } %>
					</table>
		
				</section>
				
		<section class="lucro-horario">					
					<!-- Horário de pico -->								
				<section class="horario-pico">
			    <p>Hora Mais Frequente das Vendas</p>
					<%
					Integer userId = (Integer) session.getAttribute("userId");
					HoraVendaMedia horaVendaMedia = new HoraVendaMedia();
					HoraComMaisVendas horaComMaisVendas = horaVendaMedia.calcularHoraMaisFrequente(userId);
			
					if (horaComMaisVendas != null) {
					        	
					    out.println(horaComMaisVendas.getHora());
					    		
					} else {
			
					}
			
					
					%>		
			</section>
			</section>		
		<div id="modalVenda" class="modal">
					<div class="modal-content">
						<span class="close" onclick="fecharCarrinho()">&times;</span>
						<div id="conteudoVenda">
						</div>
					</div>
				</div>  
				
	<script src="relatoriovendas.js"></script>		

	</section>	
	
	<section id="lucro-section" class="vendas-lucro-horario toggle-section section-hidden">
					<!-- Margem de lucro -->			
				<section class="margem-lucro">
						<p>Margem de lucro</p>
							<form action="rentabilidadeGeral" method="GET">
						        <label for="dia">Dia:</label>
						        <input type="number" id="dia" name="dia" min="1" max="31">
						        
						        <label for="mes">Mês:</label>
						        <input type="number" id="mes" name="mes" min="1" max="12">
						        
						        <label for="ano">Ano:</label>
						        <input type="number" id="ano" name="ano" min="1900" max="2100">
						    
						        <button type="submit">Filtrar</button>
						    </form>
							<table border = "1">
							        <tr>
							            <th>Rentabilidade Total</th>
							        </tr>
							        <%
							            // Recupera a lista de rentabilidades da requisição
							            List<Rentabilidade> rentabilidadeGeral = (List<Rentabilidade>) request.getAttribute("rentabilidadeGeral");
							        	
							            if (rentabilidadeGeral == null) {
							            	
							            	calculoRentabilidadeGeral rentalibidade = new calculoRentabilidadeGeral();
							                rentabilidadeGeral = rentalibidade.calcularRentabilidadeGeral(null ,null ,null, userId); 
							            }
							            if(rentabilidadeGeral != null){
							                for (Rentabilidade rentabilidade : rentabilidadeGeral) {
							        %>			        
							                    <tr>
							                        <td>R$ <%=String.format("%.2f",  rentabilidade.getRentabilidadeTotal()) %></td>			                        
							                    </tr>		                    
							        <%
							                }
							            } else {
							        %>
							                <tr>
							                    <td colspan="1">Nenhuma rentabilidade disponível</td>
							                </tr>
							        <%
							            }
							        %>
							    </table>
					</section>
	</section>
    
</main>

<footer>
</footer>

</body>
</html>