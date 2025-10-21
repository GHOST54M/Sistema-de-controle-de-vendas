package bancodedados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class ordenarVendasProdQuant {

    // Método para listar os produtos mais vendidos com base nos filtros de data
	public List<ItemVenda> listarProdutosMaisVendidos(Integer dia, Integer mes, Integer ano,Integer idusuario) {
	    List<ItemVenda> lista = new ArrayList<>();
	    
	    
	    // Criação do calendário para pegar o mês e o ano atuais
	    Calendar calendar = Calendar.getInstance();
	    int mesAtual = calendar.get(Calendar.MONTH) + 1;  // Janeiro = 1
	    int anoAtual = calendar.get(Calendar.YEAR);
	    int diaAtual = calendar.get(Calendar.DAY_OF_MONTH);

	    // Construção da consulta SQL com base nos filtros de data
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT iv.produto_id, SUM(iv.quantidade) AS total_quantidade, p.nome AS nome_produto ")
	       .append("FROM itens_venda iv ")
	       .append("JOIN produtos p ON iv.produto_id = p.idproduto ")
	       .append("JOIN vendas v ON iv.venda_id = v.id ");

	    // Adiciona o filtro de data conforme a condição
	    if (ano == null && mes != null && dia != null) {
	        // Quando o ano for nulo, mas o mês  e dia forem fornecidos
	        sql.append("WHERE DAY(v.data) = ? AND MONTH(v.data) = ? AND YEAR(v.data) = ? ");

	    } else if (dia != null && mes == null && ano == null) {
	        // Filtro por data atual
	        sql.append("WHERE DAY(v.data) = ? AND MONTH(v.data) = ? AND YEAR(v.data) = ? ");
	    } else if (ano != null && mes == null && dia == null) {
	        // Filtro por ano
	        sql.append("WHERE YEAR(v.data) = ? ");
	    } else if (dia == null && mes == null && ano == null) {
	        // Quando todos os parâmetros são nulos, usa a data atual
	        sql.append("WHERE YEAR(v.data) = ? AND MONTH(v.data) = ? AND DAY(v.data) = ? ");
	    }else if (mes != null && ano == null) {
	    	//Filtra por mês do ano
	        sql.append("WHERE MONTH(v.data) = ? AND YEAR(v.data) = ? ");
	    }

	    // Agora a condição WHERE também garante que venda_id seja igual ao id de vendas
	    sql.append("AND iv.venda_id = v.id ");

	    // Agrupando e ordenando os resultados
	    sql.append("GROUP BY iv.produto_id ")
	       .append("ORDER BY total_quantidade DESC");

	    try (Connection conn = cnx.getConnectionForUser(idusuario);
	         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

	        // Definir os parâmetros de acordo com os filtros
	        int parameterIndex = 1;
	        if (ano == null && mes != null && dia != null) {
	            stmt.setInt(parameterIndex++, dia); // dia fornecido
	            stmt.setInt(parameterIndex++, mes); // mês fornecido
	            stmt.setInt(parameterIndex++, anoAtual); // ano atual

	        } else if (dia != null && mes == null && ano == null) {
	            stmt.setInt(parameterIndex++, dia); // dia fornecido
	            stmt.setInt(parameterIndex++, mesAtual); // mês atual
	            stmt.setInt(parameterIndex++, anoAtual); // ano atual
	        } else if (ano != null && mes == null && dia == null) {
	            stmt.setInt(parameterIndex++, ano); // ano fornecido
	        } else if (dia == null && mes == null && ano == null) {
	            stmt.setInt(parameterIndex++, anoAtual); // ano atual
	            stmt.setInt(parameterIndex++, mesAtual); // mês atual
	            stmt.setInt(parameterIndex++, diaAtual); // dia atual

	        }  else if (mes != null && ano == null) {
	            stmt.setInt(parameterIndex++, mes); // mês fornecido
	            stmt.setInt(parameterIndex++, anoAtual); // ano atual
	        }

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                int produtoId = rs.getInt("produto_id");
	                int totalQuantidade = rs.getInt("total_quantidade");
	                String nomeProduto = rs.getString("nome_produto");

	                // Adiciona o item à lista
	                lista.add(new ItemVenda(produtoId, totalQuantidade, nomeProduto));

	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return lista;
	}

}
