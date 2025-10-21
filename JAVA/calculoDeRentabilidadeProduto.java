package bancodedados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class calculoDeRentabilidadeProduto {
    
    public List<ItemVenda> listarProdutosMaisRentaveis(Integer dia, Integer mes, Integer ano, Integer idusuario) {
        List<ItemVenda> lista = new ArrayList<>();
        
        // SQL base
        StringBuilder sql = new StringBuilder(
            "SELECT p.nome, iv.produto_id, SUM(iv.quantidade * (iv.preco_de_venda - iv.preco_de_fabrica)) AS rentabilidade_total "
            + "FROM itens_venda iv "
            + "JOIN produtos p ON iv.produto_id = p.idproduto "
            + "JOIN vendas v ON iv.venda_id = v.id "
            + "WHERE 1=1 "
        );
        
        // Obter o ano e o mês atuais
        int anoAtual = java.time.LocalDate.now().getYear();
        int mesAtual = java.time.LocalDate.now().getMonthValue();

        // Condições para ano, mês e dia
        if (ano == null && mes == null && dia == null) {
            sql.append("AND YEAR(v.data) = ? AND MONTH(v.data) = ?");
        } else if (ano != null && mes == null && dia == null) {
            sql.append("AND YEAR(v.data) = ?");
        } else if (ano != null && mes != null && dia == null) {
            sql.append("AND YEAR(v.data) = ? AND MONTH(v.data) = ?");
        } else if (ano == null && mes != null && dia == null) {
            sql.append("AND YEAR(v.data) = ? AND MONTH(v.data) = ?");
        } else if (ano == null && mes == null && dia != null) {
            sql.append("AND YEAR(v.data) = ? AND MONTH(v.data) = ? AND DAY(v.data) = ?");
        } else if (ano != null && mes != null && dia != null) {
            sql.append("AND YEAR(v.data) = ? AND MONTH(v.data) = ? AND DAY(v.data) = ?");
        }
        
        // Ordenar pela rentabilidade total
        sql.append(" GROUP BY iv.produto_id, p.nome ORDER BY rentabilidade_total DESC");
        
        try (Connection conn = cnx.getConnectionForUser(idusuario);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            // Definir os parâmetros de data conforme os critérios
            int paramIndex = 1;
            
            if (ano == null && mes == null && dia == null) {
                stmt.setInt(paramIndex++, anoAtual);
                stmt.setInt(paramIndex++, mesAtual);
            } else if (ano != null && mes == null && dia == null) {
                stmt.setInt(paramIndex++, ano);
            } else if (ano != null && mes != null && dia == null) {
                stmt.setInt(paramIndex++, ano);
                stmt.setInt(paramIndex++, mes);
            } else if (ano == null && mes != null && dia == null) {
                stmt.setInt(paramIndex++, anoAtual);
                stmt.setInt(paramIndex++, mes);
            } else if (ano == null && mes == null && dia != null) {
                stmt.setInt(paramIndex++, anoAtual);
                stmt.setInt(paramIndex++, mesAtual);
                stmt.setInt(paramIndex++, dia);
            } else if (ano != null && mes != null && dia != null) {
                stmt.setInt(paramIndex++, ano);
                stmt.setInt(paramIndex++, mes);
                stmt.setInt(paramIndex++, dia);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    double rentabilidadeTotal = rs.getDouble("rentabilidade_total");
                    String nomeProduto = rs.getString("nome");
                    lista.add(new ItemVenda(nomeProduto, rentabilidadeTotal));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
