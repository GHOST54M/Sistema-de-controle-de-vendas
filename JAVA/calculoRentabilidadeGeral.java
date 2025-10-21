package bancodedados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class calculoRentabilidadeGeral {

    public List<Rentabilidade> calcularRentabilidadeGeral(Integer ano, Integer mes, Integer dia, Integer idusuario) {
        List<Rentabilidade> rentabilidadeList = new ArrayList<>(); 

        // Obter a data atual
        LocalDate hoje = LocalDate.now();
        int anoAtual = hoje.getYear();
        int mesAtual = hoje.getMonthValue();

        String sql = "SELECT SUM(i.quantidade * (i.preco_de_venda - i.preco_de_fabrica)) AS rentabilidade_total "
                   + "FROM itens_venda i "
                   + "JOIN vendas v ON i.venda_id = v.id "
                   + "WHERE ";

        // Condições para construir a consulta SQL com base nos parâmetros
        if (ano == null && mes != null && dia != null) {
        	
            // dia e o mês forem fornecidos, use o ano atual
            sql += "DAY(v.data) = ? AND MONTH(v.data) = ? AND YEAR(v.data) = ?";
        } else if (ano == null && mes != null && dia == null) {
        	
            // mês for fornecido, use o ano atual
            sql += "MONTH(v.data) = ? AND YEAR(v.data) = ?";
        } else if (ano != null && mes == null && dia == null) {
        	
            // ano fornecido
            sql += "YEAR(v.data) = ?";
        } else if (ano == null && mes == null && dia == null) {
        	
            // filtre com o mês atual
            sql += "MONTH(v.data) = ? AND YEAR(v.data) = ?";
        } else if(ano == null && mes == null && dia != null) {
            sql += "DAY(v.data) = ? AND MONTH(v.data) = ? AND YEAR(v.data) = ?";

        }

        try (Connection conn = cnx.getConnectionForUser(idusuario);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Definir os parâmetros da consulta SQL com base nos critérios
            if (ano == null && mes != null && dia != null) {
                stmt.setInt(1, mes);      // mês
                stmt.setInt(2, dia);      // dia
                stmt.setInt(3, anoAtual); // ano atual
            } else if (ano == null && mes != null && dia == null) {
                stmt.setInt(1, mes);      // mês
                stmt.setInt(2, anoAtual); // ano atual
            } else if (ano != null && mes == null && dia == null) {
                stmt.setInt(1, ano);      // ano
            } else if (ano == null && mes == null && dia == null) {
                stmt.setInt(1, mesAtual); // mês atual
                stmt.setInt(2, anoAtual); // ano atual
            } else if (ano == null && mes == null && dia != null) {
                stmt.setInt(1, dia);      // dia
                stmt.setInt(2, mesAtual); // mês atual
                stmt.setInt(3, anoAtual); // ano atual           	
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double rentabilidadeTotal = rs.getDouble("rentabilidade_total");
                    rentabilidadeList.add(new Rentabilidade(rentabilidadeTotal)); // Adicionar à lista
                } else {
                    System.out.println("Sem dados!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentabilidadeList;
    }
}
