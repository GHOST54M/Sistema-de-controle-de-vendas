document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("formCadastroProduto");

    if (!form) {
        console.error("Formulário não encontrado!");
        return;
    }

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        // Obtendo os elementos
        const nomeField = document.getElementById("nome");
        const quantMaxField = document.getElementById("quantmax");

        // Verifique se os campos existem
        if (!nomeField || !quantMaxField) {
            console.error("Elementos do formulário não foram encontrados!");
            return;
        }

        // Validação dos campos
        const nome = nomeField.value.trim();
        const quantMax = quantMaxField.value.trim();
        const quantMaxValue = Number(quantMax);

        let isValid = true;
        let errorMessage = "";

        // Validação do nome
        if (nome === "") {
            isValid = false;
            errorMessage += "O nome é obrigatório.\n";
        }

        // Validação da quantidade máxima
        if (!Number.isInteger(quantMaxValue) || quantMaxValue <= 0) {
            isValid = false;
            errorMessage += "A quantidade máxima no estoque deve ser um número inteiro positivo.\n";
        }

        // Exibir erros ou submeter o formulário
        if (!isValid) {
            alert(errorMessage);
        } else {
            form.submit();
        }
    });
});
