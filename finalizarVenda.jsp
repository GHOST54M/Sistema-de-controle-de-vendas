<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Enviar para Carrinho</title>
</head>
<body>
<%
    // Recupera os parâmetros "action" e "id" da requisição
    String action = request.getParameter("action");
    String id = request.getParameter("id");
    String quantidade = request.getParameter("quantidade");
%>

<form id="autoSubmitForm" action="carrinhoCompras" method="GET">
    <!-- Campos ocultos para enviar os dados -->
    <input type="hidden" name="action" value="<%= action %>">
    <input type="hidden" name="id" value="<%= id %>">
    <input type="hidden" name="quantidade" value="<%= quantidade %>">
</form>

<script>
    // Envia o formulário automaticamente
    document.getElementById("autoSubmitForm").submit();
</script>

</body>
</html>
