package bancodedados;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class consultarVendas {

    public List<Venda> listarVendas(String ordem, LocalDate dia, Integer mes, Integer ano, Integer idusuario) {
        List<Venda> vendas = new ArrayList<>();
        
        // Define ordem padrão como 'desc' se o valor for inesperado
        if (ordem == null || (!ordem.equalsIgnoreCase("asc") && !ordem.equalsIgnoreCase("desc"))) {
            ordem = "desc";
        }

        // Obtém o mês e o ano atuais para os casos onde apenas parte dos dados é fornecida
        LocalDate today = LocalDate.now();
        int mesAtual = today.getMonthValue();
        int anoAtual = today.getYear();

        // Constrói a consulta SQL dinamicamente com base nos parâmetros fornecidos
        StringBuilder sql = new StringBuilder("SELECT * FROM vendas WHERE 1=1");

        // Caso todos os parâmetros sejam nulos, traz as vendas do dia atual
        if (dia == null && mes == null && ano == null) {
        	sql.append(" AND DATE(data) = ?");
        } else if (dia != null && mes == null && ano == null) {
            // Filtro por dia do mês e ano atuais
        	sql.append(" AND DAY(data) = ? AND MONTH(data) = ? AND YEAR(data) = ?");
        } else if (dia != null) {
            // Filtro por data específica
        	sql.append(" AND DATE(data) = ?");
        } else if (mes != null && ano == null) {
            // Filtro por mês fornecido e ano atual
        	sql.append(" AND MONTH(data) = ? AND YEAR(data) = ?");
        } else if (mes != null && ano != null) {
            // Filtro por mês e ano específicos
        	sql.append(" AND MONTH(data) = ? AND YEAR(data) = ?");
        } else if (ano != null) {
            // Filtro por ano específico
        	sql.append(" AND YEAR(data) = ?");
        }

        // Adiciona a ordenação
        sql.append(" ORDER BY valor_total ").append(ordem);

        try (Connection conn = cnx.getConnectionForUser(idusuario);
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            // Define os parâmetros para o filtro de dia, mês e ano
            if (dia == null && mes == null && ano == null) {
                // Filtro por dia atual
                stmt.setDate(paramIndex++, Date.valueOf(today));
            } else if (dia != null && mes == null && ano == null) {
                // Filtro por dia no mês e ano atuais
                stmt.setInt(paramIndex++, dia.getDayOfMonth());
                stmt.setInt(paramIndex++, mesAtual);
                stmt.setInt(paramIndex++, anoAtual);
            } else if (dia != null) {
                // Filtro por data específica
                stmt.setDate(paramIndex++, Date.valueOf(dia));
            } else if (mes != null && ano == null) {
                // Filtro por mês fornecido e ano atual
                stmt.setInt(paramIndex++, mes);
                stmt.setInt(paramIndex++, anoAtual);
            } else if (mes != null && ano != null) {
                // Filtro por mês e ano específicos
                stmt.setInt(paramIndex++, mes);
                stmt.setInt(paramIndex++, ano);
            } else if (ano != null) {
                // Filtro por ano específico
                stmt.setInt(paramIndex++, ano);
            }

            ResultSet rs = stmt.executeQuery();

            // Processa os resultados e cria a lista de vendas
            while (rs.next()) {
                Venda venda = new Venda(rs.getInt("id"), rs.getDate("data"), rs.getDouble("valor_total"));
                vendas.add(venda);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendas;
    }
}