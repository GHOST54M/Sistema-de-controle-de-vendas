
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="bancodedados.Produto" %>
<%@ page import="bancodedados.consultarProdutos" %>
<%@ page import="bancodedados.registroDeModificacoesConsulta" %>
<%@ page import="bancodedados.registroDeModificacoes" %>

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
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&family=Hubballi&family=IBM+Plex+Sans+Devanagari:wght@100;200;300;400;500;600;700&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&family=Italiana&display=swap" rel="stylesheet">
		
		<meta name="viewport" content="width=device-width, initial-scale=1">   
		<link rel="stylesheet" type="text/css" href="css/estilosRepetidos.css">			   
		<meta charset="UTF-8">
		
			<title>Estoque</title>
<style>
        .tabela{
 		display: flex;
    	flex-direction: column;
    	align-items: center;      
        }
        .lapis, .excluir, .reposicao{
        width:35px;
        height:35px;
        }
		.registro-reposicao{
		display: flex;
    	flex-direction: column;
    	align-items: center;		
		}
		iframe{
		height: 300px;
		}
        .log-entry {
            background-color: #fff;
            max-height:400px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin-bottom: 10px;
            padding: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        .log-entry:nth-child(even) {
            background-color: #f9f9f9;
        }
        .log-entry p {
            margin: 5px 0;
        }
        .log-entry .date {
            font-size: 0.9em;
            color: #777;
        }
        .log-entry .details {
            font-size: 1.1em;
            color: #333;
        }
        .modal, .modalNFe {
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
            max-width: 500px;
            max-height:400px;
    }

    .close {
        color: #aaa;
        float: right;
        font-size: 28px;
        font-weight: bold;
    }

    .close:hover,
    .close:focus {
        color: black;
        text-decoration: none;
        cursor: pointer;
    }
    
    .log-alteracoes{
    display: flex;
    align-items: center;
    flex-direction: column;
	}
	
    .lista{
    list-style: none;
    font-family: Hubballi;
	line-height: 22.94px;
    }
    
    .lista > li > p {
   font-size: 24px;
   font-weight: 700;
    
    }
    
    .lista > li{
    margin:18px;
    padding: 15px;
    width: 600px;
    font-size: 20px;    
    box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.2);
    
    }
    
    .titulo{
    padding:20px;
    font-family: "IBM Plex Sans Devanagari", sans-serif;
	font-style: normal;
	font-family: IBM Plex Sans Devanagari;
	font-size: 26px;
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
		
   	<section class="tabela">
        <div>
            <a href="javascript:void(0);" onclick="openModal()">Novo Produto</a>
            <a href="javascript:void(0);" onclick="openModalNFe()">Cadastro via NFe</a>
        </div>
        <table border="1">
            <tr>
                <th>QTD</th>
                <th>Produto</th>
                <th>Preço</th>
                <th>Código</th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
    
            <%
            	Integer userId = (Integer) session.getAttribute("userId"); // Cast para Integer
                consultarProdutos produtoDAO = new consultarProdutos();
                List<Produto> produtos = produtoDAO.listarProdutos(userId);
                
                for (Produto produto : produtos) {
            %>
                <tr>
                    <td><%= produto.getQuant() %></td>
                    <td><%= produto.getNome() %></td>
                    <td>R$<%= produto.getPreco() %></td>
                    <td><%= produto.getId() %></td>
                    <td><a href="javascript:void(0);" onclick="openReestocarModal(<%=produto.getId() %>)"><img src="imagens/reestocar.png" class="reposicao"></a></td>                  
                    <td><a href="javascript:void(0);" onclick="openEditModal(<%=produto.getId() %>)"><img src="imagens/lapis.png" class="lapis"></a></td>
                    <td><a href="javascript:void(0);" onclick="openDeleteModal(<%=produto.getId() %>)"><img src="imagens/excluir.png" class="excluir"></a></td>
                </tr>
            <%
                }
            %>
        </table>
    </section>
	
	<section class="button-group">
		<span onclick="toggleSection('registroReestocagem')">Visualizar registro de reestocagem</span>
    	<span onclick="toggleSection('registroCadastro')">Visualizar registro de Cadastro</span>
    	<span onclick="toggleSection('registroRemocao')">Visualizar registro de Exclusões</span>
    	
	</section>
	
	<section id="registroReestocagem" class="log-alteracoes toggle-section">
		<p class="titulo">Registro de reestocagem</p>
	    <ul class="lista">
	        <%
	            // Obter a lista de logs passada pelo Servlet
	            registroDeModificacoesConsulta listaModificacoes = new registroDeModificacoesConsulta();
	            List<registroDeModificacoes> logs = listaModificacoes.listarRegistroDeModificacoes(userId);
	            // Exibir cada log na tabela
	            if (logs != null) {
	                for (registroDeModificacoes registroDeModificacoes : logs) {
	                    // Filtrar apenas os registros do tipo REESTOCAR
	                    if (registroDeModificacoes.getTipoOperacao() == registroDeModificacoes.tipoOperacao.REESTOCAR) {
	        %>
	                        <li>
	                            <p>Data da operação:</p><%= registroDeModificacoes.getDataOperacao2() %><br>
	                            <%= registroDeModificacoes.getDadosNovos() %> 
	                        </li>
	        <%
	                    }
	                }
	            } else {
	                out.println("Nenhum log encontrado.");
	            }
	        %>
	    </ul>
	</section>
	
	<section id="registroCadastro" class="log-alteracoes toggle-section section-hidden">
		<p class="titulo">Registro de cadastro</p>
	    <ul class="lista">
	        <%
	            // Exibir cada log na tabela
	            if (logs != null) {
	                for (registroDeModificacoes registroDeModificacoes : logs) {
	                    // Filtrar apenas os registros do tipo REESTOCAR
	                    if (registroDeModificacoes.getTipoOperacao() == registroDeModificacoes.tipoOperacao.INSERÇÃO) {
	        %>
	                        <li>
	                            <p>Data da operação:</p><%= registroDeModificacoes.getDataOperacao2() %><br>
	                            <%= registroDeModificacoes.getDadosNovos() %> 
	                        </li>
	        <%
	                    }
	                }
	            }
	        %>
	    </ul>
	</section>
	
  	<section id="registroRemocao" class="log-alteracoes toggle-section section-hidden">
		<p class="titulo">Registro de exclusões</p>
	    <ul class="lista">
	        <%
	            // Exibir cada log na tabela
	            if (logs != null) {
	                for (registroDeModificacoes registroDeModificacoes : logs) {
	                    // Filtrar apenas os registros do tipo REESTOCAR
	                    if (registroDeModificacoes.getTipoOperacao() == registroDeModificacoes.tipoOperacao.REMOÇÃO) {
	        %>
	                        <li>
	                            <p>Data da operação:</p><%= registroDeModificacoes.getDataOperacao2() %><br>
	                            <%= registroDeModificacoes.getDadosNovos() %> 
	                        </li>
	        <%
	                    }
	                }
	            }
	        %>
	    </ul>
	</section>
	  
    <!-- Modais -->
    <section class="modais">
		    <div id="produtoModal" class="modal">
		        <div class="modal-content">
		            <span class="close" onclick="closeModal()">&times;</span>
		            <iframe src="cadastroProduto.jsp" width="100%" height="600px"></iframe>
		        </div>
		    </div>
		    
		     <div id="produtoModalNFe" class="modalNFe">
		        <div class="modal-content">
		            <span class="close" onclick="closeModalNFe()">&times;</span>
		            <iframe src="cadastroProdutoNotalFiscal.jsp" width="100%" height="600px"></iframe>
		        </div>
		    </div>
		    
		      <div id="reestocarModal" class="modal">
		        <div class="modal-content">
		            <span class="close" onclick="closeReestocarModal()">&times;</span>
		            <iframe id="reestocarIframe" src="" width="100%" height="600px"></iframe>
		        </div>
		    </div>
		      
		    <div id="editarModal" class="modal">
		        <div class="modal-content">
		            <span class="close" onclick="closeEditModal()">&times;</span>
		            <iframe id="editarIframe" src="" width="100%" height="600px"></iframe>
		        </div>
		    </div>
		
		    <div id="excluirModal" class="modal">
		        <div class="modal-content">
		            <span class="close" onclick="closeDeleteModal()">&times;</span>
		            <iframe id="excluirIframe" src="" width="100%" height="600px"></iframe>
		        </div>
		    </div>
    </section>  
    <script>
	function toggleSection(sectionId) {
		// Oculta todas as seções
		const sections = document.querySelectorAll('.toggle-section');
		sections.forEach(section => section.classList.add('section-hidden'));

		// Mostra a seção clicada
		const selectedSection = document.getElementById(sectionId);
		if (selectedSection) {
			selectedSection.classList.remove('section-hidden');
		}
	}
	
        function openModal() {
            document.getElementById("produtoModal").style.display = "block";
        }

        function closeModal() {
            document.getElementById("produtoModal").style.display = "none";
        }

        function openModalNFe() {
            document.getElementById("produtoModalNFe").style.display = "block";
        }

        function closeModalNFe() {
            document.getElementById("produtoModalNFe").style.display = "none";
        }

        function openReestocarModal(produtoId) {
            document.getElementById("reestocarModal").style.display = "block";
            document.getElementById("reestocarIframe").src = "reestocarProduto.jsp?id=" + produtoId;
        }
        function closeReestocarModal() {
            document.getElementById("reestocarModal").style.display = "none";
        }
        function openEditModal(produtoId) {
            document.getElementById("editarModal").style.display = "block";
            document.getElementById("editarIframe").src = "editarProduto.jsp?id=" + produtoId;
        }

        function closeEditModal() {
            document.getElementById("editarModal").style.display = "none";
        }

        function openDeleteModal(produtoId) {
            document.getElementById("excluirModal").style.display = "block";
            document.getElementById("excluirIframe").src = "excluirProduto.jsp?id=" + produtoId;
        }

        function closeDeleteModal() {
            document.getElementById("excluirModal").style.display = "none";
        }
        
        window.addEventListener('message', function(event) {
            if (event.data === 'editSuccess' || event.data === 'deleteSuccess') {
                // Fecha o modal e recarrega a página após sucesso
                closeEditModal();
                closeDeleteModal();
                location.reload(); // Recarrega a página principal
            }
        
        });
    </script>

</main>
</body>
</html>