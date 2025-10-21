<!DOCTYPE html>
<html>
<head>
	
        <meta name="viewport" content="width=device-width, initial-scale=1">       
		<meta charset="UTF-8">
		
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&display=swap" rel="stylesheet">
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&family=Italiana&display=swap" rel="stylesheet">
		<link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Asul:wght@400;700&family=Cabin:ital,wght@0,400..700;1,400..700&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&family=Italiana&display=swap" rel="stylesheet">				
		<title>VENano</title>	
    	<link rel="stylesheet" type="text/css" href="css/reset.css">
    		
		<style>
			.menu{
			background: #CFD5FF;
			width: 100%;			
			height: 70px;
			
			}
			.menu > h1{
			  font-family: "Italiana", sans-serif;
 			  font-weight: 400;
  			  font-style: normal;
  			  font-size: 50px;
  			  position: relative;
			  top: 12px;
		      left: 2%;
		      width: 300px;
			}
			.container{
			width:400px;
			margin-top:15px;
			margin-bottom:15px;
			}
			.container_controle_vendas {
			    margin-left: 10%;
			    margin-right: auto;
			    margin-top:20px;
			}
			
			.container_nossa_missao {
			    margin-right: 10%;
			    margin-left: auto;
			
			}
						
			.links{
			    display: flex;
    			flex-direction: column;
    			align-items: center;
    			font-size: 25px;
				margin: 0 auto;
				width: fit-content;
				text-align: center;    			
			}
			.japossuiconta, .experimente{
				margin-top:20px;
				margin-bottom:20px;

			}
			.experimente, .japossuiconta{
			text-decoration:none;
			
			}
			.experimente{
			display: flex;
    		align-items: center;
    		justify-content: center;
			background: #CFD5FF;
			width: 272px;
			height:60px;
			border: 1px solid black;
			border-radius: 10px;
			color: black;
			}
			.japossuiconta{
			
			}
			.container > p{
			  font-family: "Asul", serif;
			  font-weight: 400;
			  font-style: normal;
			  font-size: 18px;	
			  		  
			}
			h2{
			  font-family: "Inter", serif;
  			  font-weight: 400;
  			  font-style: normal;
  			  font-size: 25px;			  
  			  
			}
			.curiosidades{
				display: flex;
    			justify-content: space-evenly;			
			}
			.curiosidades-conteudo{
			display: flex;
    		flex-direction: column;
   		 	align-items: center;
			}
			.curiosidades > .curiosidades-conteudo > img{
			width: 60px;
			height: 60px;
			margin-bottom: 5px;
			}
			
			.curiosidades > .curiosidades-conteudo > p{
			  font-family: "Asul", serif;
			  font-weight: 400;
			  font-style: normal;
			  font-size: 15px;	
			}	
			.duvidas{
			margin-top:25px; 
		    display: flex;
		    align-items: center;
		    justify-content: center;
		    font-family: Inter;
			font-size: 22px;
			font-weight: 300;
			line-height: 26.63px;
		    			
			}		
		</style>
</head>
<body>

<header>
	<section class="menu">
		<h1>VENANO</h1>
	</section>
</header>

<main>

	<section class="container_controle_vendas container">
		<h2>Sistema de Controle de Vendas</h2>
		<p>Controle as vendas do seu negócio de forma automática com nosso software, 
		nele você acompanha todas as vendas do seu negócio, gerencie o seu estoque, 
		tenha acesso a relatório precisos e completos sobre as suas vendas.</p>
	</section>
	
	<section class="container links">
			<a href="cadastroUsuario.jsp" class="experimente">Experimente!</a>
			<a href="login.jsp" class="japossuiconta">Já possui uma conta?</a>
	</section>
			
	<section class="container_nossa_missao container">
		<h2>Nossa Missão</h2>
		<p>Nossa missão é apoiar nano e micro empreendedores através da tecnologia 
		fornecendo uma aplicação que facilite a gestão e controle dos seus negócios.</p>
	</section>
	
	<section class="curiosidades">
		<div class="curiosidades-conteudo">
			<img src="imagens/venda.png">
			<p>Acompanhe suas Vendas</p>
		</div>
		<div class="curiosidades-conteudo">
			<img src="imagens/estoque.png">
			<p>Controle de Estoque</p>
		</div>
		<div class="curiosidades-conteudo">
			<img src="imagens/reposicao.png">
			<p>Relatório de Reposição</p>
		</div>
		<div class="curiosidades-conteudo">
			<img src="imagens/notafiscal.png">
			<p>Inserção no Estoque via Nota Fiscal</p>
		</div>
		<div class="curiosidades-conteudo">
			<img src="imagens/relatorio.png">
			<p>Relatório de Vendas</p>
		</div>
		<div class="curiosidades-conteudo">
			<img src="imagens/estoqueevenda.png">
			<p>Integração entre Estoque e Vendas</p>
		</div>
		<div class="curiosidades-conteudo">
			<img src="imagens/personalizacao.png">
			<p>Alto Nível de Personalização</p>
		</div>		
	</section>
	
	<section class="duvidas">
		<p>Dúvidas Frequentes</p>
	
	</section>
</main>

<footer>
</footer>

</body>
</html>