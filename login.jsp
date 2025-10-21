<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        
        <meta name="viewport" content="width=device-width, initial-scale=1">       
		<meta charset="UTF-8">
		
		<title>Login VENano</title>
		<style>
			h1{
			
			font-family: Italiana;
			font-size: 96px;
			font-weight: 400;
			line-height: 113.09px;
			text-align: left;
			text-underline-position: from-font;
			text-decoration-skip-ink: none;
			display: flex;
    		justify-content: center;
			
			}
			.posicionamento{
			display: flex;
    		justify-content: center;			
			}

			form{
			display:flex;
			flex-direction: column;
			background: #ABBAC7;
			width: 507px;
			height: 576px;
			border-radius: 30px;
				
			}
			form > label, form > div > label{
			font-size: 25px;
    		padding-left: 6px;
			margin: 10px 20px 20px 70px;    					
			}
			#email, #senha{
			width: 367px;
			height: 50px;			
			border: 1px;
			margin: 10px 20px 20px 70px;
			padding: 5px;
    		font-size: 21px;
    		background: #D9D9D9;
    		border: 1px solid #000000;			
			}
			.esqueceusenha{
			font-family: Inter;
			font-size: 20px;
			font-weight: 300;
			line-height: 24.2px;
			text-decoration-line: underline;
			text-decoration-style: solid;
			text-underline-position: from-font;
			text-decoration-skip-ink: none;
			color: #000000;
			
			}
			.Lembrar{
			margin: 10px 20px 20px 70px;
			
			}
			.botao{
			width: 221px;
			height: 75px;
			border-radius: 10px;
			border: 1px;
			background: #689DFF;
			border: 1px solid #000000;
			font-family: Inter;
			font-size: 32px;
			font-weight: 300;
			line-height: 38.73px;
			margin: 10px 20px 20px 140px;
						
			}
		</style>
</head>
<body>

<header>
</header>

<main>
<h1>VENANO</h1>
	<section class="posicionamento">
			<form action="Login" method="POST">
			
				<label for="email">Email:</label>
				<input type="text" id="email" name="email" required>
				<div>
					<label for="senha">Senha:</label>
					<a href="#" class="esqueceusenha">Esqueceu a senha?</a>
				</div>
				<input type="password" id="senha" name="senha" required>
				<div>
				
				<input type="checkbox" class="Lembrar">
				</div>
				<input type="submit" class="botao" value="Entrar">
				<a href="cadastroUsuario.jsp">Cadastrar-se</a>
	
			</form>
		
	</section>
	<%
        if (request.getParameter("error") != null) {
            out.println("<p style='color:red;'>Email ou senha inválidos!</p>");
        }
    %>
</main>

<footer>
</footer>

</body>
</html> 