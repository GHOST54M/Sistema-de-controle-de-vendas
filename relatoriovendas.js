        // Função para abrir o modal do carrinho
		function abrirDetalhesVendaModal(vendaId) {
		    document.getElementById("modalVenda").style.display = "block";
		
		    // Requisição AJAX para carregar o conteúdo das vendas
		    const xhr = new XMLHttpRequest();
		    xhr.open("GET", "exibirDetalhesVenda?id=" + vendaId, true);  // Passa o ID da venda na URL
		    xhr.onload = function () {
		        if (xhr.status === 200) {
		            document.getElementById("conteudoVenda").innerHTML = xhr.responseText;
		        } else {
		            document.getElementById("conteudoVenda").innerHTML = "Erro ao carregar detalhes da compra.";
		        }
		    };
		    xhr.send();
		}


        // Função para fechar o modal do carrinho
        function fecharCarrinho() {
            document.getElementById("modalVenda").style.display = "none";
        }
        function ordenarVendas(ordem) {
            // Envia o parâmetro de ordem para o servidor via requisição GET
            window.location.href = "?ordem=" + ordem;
        }

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