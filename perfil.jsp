<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="bancodedados.consultarUsuario" %>
<%@ page import="bancodedados.Usuario" %>
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
		<link rel="stylesheet" type="text/css" href="css/estilosRepetidos.css">	
		
		<title>Perfil</title>
		<style>

		.titulo{
		font-family: Italiana;
		font-size: 96px;
		font-weight: 400;
		line-height: 113.09px;
		margin:0px;
		
		}
		.conteudo{
		display: flex;
    	flex-direction: column;
    	align-items: center;
    	font-family: Asul;
		font-weight: 400;
		line-height: 29.11px;
			
		}
		.formUsuario{
		    width: 520px;
		    height: 470px;
		    padding:10px;
		    background: #CFD5FF;
		    width: 550px;
			border-radius: 10px;
			border: 1px solid black;
		}
		#nome, #email{
		width: 392px;
		height: 30px;
		margin: 8px;

		}
		#nome, #email, .nome, .email{
		margin-left:70px;
		
		}
		#Celular, #Datanascimento{
		width: 210px;
		height: 30px;
		
		}		
		</style>
</head>
<body>
<header></header>
<main>
	<section class="conteudo">
		<p class="titulo">VENANO</p>
		<section class="dados-usuario">
				<%
				int userId = (int) session.getAttribute("userId");
				consultarUsuario consultarUsuario = new consultarUsuario();
				Usuario usuario = consultarUsuario.getUsuarioById(userId);
				
				%>
		    <form id="userForm" action="editarUsuario" method="POST" onsubmit="return validateForm()"class="formUsuario">
		        
		        <p>Dados do Usuário</p>
		        
		        <button type="button" id="editButton" onclick="toggleEdit()">Editar</button>
		        <button type="submit" id="submitButton" style="display: none;">Salvar Alterações</button>
		        
		        <input type="hidden" id="id" name="id" value="<%= userId %>" readonly><br>
		        
		        <label for="nome" class="nome">Nome:</label><br>
		        <input type="text" id="nome" name="nome" value="<%= usuario.getNome() %>" readonly><br>
		        
		        <label for="email" class="email">Email:</label><br>
		        <input type="text" id="email" name="email" value="<%= usuario.getEmail() %>" readonly><br>
		        <div>
		        <label for="Celular">Celular:</label>
		        <label for="Datanascimento">Data de nascimento:</label>
		        <br>
		        <input type="text" id="Celular" name="Celular" value="<%= usuario.getCelular() %>" readonly>       
		        <input type="text" id="Datanascimento" name="Datanascimento" value="<%= usuario.getDatanascimento() %>" readonly>    
		        </div>
		        <br>
		        <label for="cnpj">CNPJ:</label>
		    	<input type="text" id="cnpj" name="" value="<%= usuario.getCnpj() %>" readonly>       
		        <div>
					<p>Membro desde:</p><%= usuario.getDatainscricao() %>
				</div>
		        <script src="perfil.js"></script>
		    </form>
		    
		    <form action="Logout" action="POST">
		    	<input type="submit" value="Sair">
		    </form>
			<a href="relatoriovendas.jsp">Voltar</a>

		</section>
	</section>
	

</main>
<footer></footer>
</body>
</html>