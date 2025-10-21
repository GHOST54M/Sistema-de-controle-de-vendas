package bancodedados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class HoraVendaMedia {

    private Connection conn;

    public HoraComMaisVendas calcularHoraMaisFrequente(Integer idusuario) throws SQLException {
        Map<Integer, Integer> horasContagem = new HashMap<>();
        
        try {
            conn = cnx.getConnectionForUser(idusuario); 
        } catch (SQLException e) {
            e.printStackTrace();  
        }

        // SQL para obter as horas das vendas
        String sql = "SELECT HOUR(horario_venda) AS hora FROM vendas";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int hora = rs.getInt("hora");
                // Incrementa a contagem para essa hora
                horasContagem.put(hora, horasContagem.getOrDefault(hora, 0) + 1);
            }
        }

        // Se não houver dados
        if (horasContagem.isEmpty()) {
            return null;  // Caso não haja vendas, retorna null ou uma mensagem indicando ausência de dados
        }

        // Encontrar a hora mais frequente
        int horaMaisFrequente = -1;
        int maiorContagem = 0;
        for (Map.Entry<Integer, Integer> entry : horasContagem.entrySet()) {
            if (entry.getValue() > maiorContagem) {
                maiorContagem = entry.getValue();
                horaMaisFrequente = entry.getKey();
            }
        }

        // Cria um objeto Time com a hora mais frequente
        Time horaMaisFrequenteTime = Time.valueOf(String.format("%02d:00:00", horaMaisFrequente));
		System.out.println(horaMaisFrequenteTime);

        return new HoraComMaisVendas(horaMaisFrequenteTime);
    }
}
