document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector("form");

    form.addEventListener("submit", function(event) {
        event.preventDefault();
        
        // Campos do formulário
        const nome = document.getElementById("nome").value.trim();
        const email = document.getElementById("email").value.trim();
        const celular = document.getElementById("celular").value.trim();
        const datanascmento = document.getElementById("datanascmento").value;
        const senha = document.getElementById("senha").value;
        const senhaconfirmar = document.getElementById("senhaconfirmar").value;
        const cnpj = document.getElementById("cnpj").value.trim(); // Campo do CNPJ

        // Variável para marcar se o formulário é válido
        let isValid = true;
        let errorMessage = "";

        // Validação do nome
        if (nome === "") {
            isValid = false;
            errorMessage += "O nome é obrigatório.\n";
        }

        // Validação do e-mail
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            isValid = false;
            errorMessage += "Por favor, insira um e-mail válido.\n";
        }

        // Validação do celular (somente números e tamanho mínimo)
        const celularPattern = /^[0-9]{10,}$/;
        if (!celularPattern.test(celular)) {
            isValid = false;
            errorMessage += "O número de celular deve conter apenas números e ter pelo menos 10 dígitos.\n";
        }

        // Validação da data de nascimento
        if (datanascmento === "") {
            isValid = false;
            errorMessage += "A data de nascimento é obrigatória.\n";
        } else {
            const dataNascTexto = new Date(datanascmento).toISOString().split('T')[0];
            if (!dataNascTexto) {
                isValid = false;
                errorMessage += "A data de nascimento deve ser uma data válida.\n";
            }
        }

        // Validação da senha e confirmação da senha
        if (typeof senha !== "string" || typeof senhaconfirmar !== "string") {
            isValid = false;
            errorMessage += "As senhas devem ser válidas como texto.\n";
        } else if (senha !== senhaconfirmar) {
            isValid = false;
            errorMessage += "As senhas não coincidem.\n";
        }

        // Exibir mensagem de erro ou enviar o formulário
        if (!isValid) {
            alert(errorMessage);
        } else {
            form.submit();
        }
    });
});
