        function toggleEdit() {
            const form = document.getElementById('userForm');
            const inputs = form.querySelectorAll('input:not([type="hidden"])');
            const editButton = document.getElementById('editButton');
            const submitButton = document.getElementById('submitButton');

            inputs.forEach(input => {
                input.readOnly = !input.readOnly;
            });

            if (inputs[0].readOnly) {
                editButton.textContent = 'Editar';
                submitButton.style.display = 'none';
            } else {
                editButton.textContent = 'Cancelar';
                submitButton.style.display = 'inline';
            }
        }
	        
	       	function validateForm() {
            const email = document.getElementById('email').value;
            const celular = document.getElementById('Celular').value;
            const dataNascimento = document.getElementById('Datanascimento').value;

            // Regex para validação de email
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                alert("Por favor, insira um email válido.");
                return false;
            }

  			const celularRegex = /^(\(\d{2}\) \d{5}-\d{4}|\d{10,11})$/;
        	if (!celularRegex.test(celular)) {
            alert("Por favor, insira um número de celular válido no formato (XX) XXXXX-XXXX ou XXXXXXXXXX.");
            return false;
       		}

            // Regex para validação de data de nascimento no formato dd/mm/yyyy
            const dataNascimentoRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/;
            if (!dataNascimentoRegex.test(dataNascimento)) {
                alert("Por favor, insira uma data de nascimento válida no formato dd/mm/yyyy.");
                return false;
            }

            return true;
        }