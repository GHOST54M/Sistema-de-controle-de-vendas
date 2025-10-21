package bancodedados;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class registroDeModificacoesConsulta {
    public List<registroDeModificacoes> listarRegistroDeModificacoes(Integer userId) {
        List<registroDeModificacoes> lista = new ArrayList<>();
        Connection conn = null;
        ResultSet rs = null;

        try {
            // Conectar ao banco de dados
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = cnx.getConnectionForUser(userId);

            // Consulta para buscar os logs
            String query = "SELECT dados_novos, data_operacao, tipo_operacao FROM log_alteracoes ORDER BY data_operacao DESC";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Preencher a lista com os logs
            while (rs.next()) {
                String dadosNovos = rs.getString("dados_novos");
                Timestamp dataOperacao = rs.getTimestamp("data_operacao");
                String tipoOperacaoStr = rs.getString("tipo_operacao");

                // Converter a string do banco para o enum TipoOperacao
                registroDeModificacoes.TipoOperacao tipoOperacao = null;
                try {
                    tipoOperacao = registroDeModificacoes.TipoOperacao.valueOf(tipoOperacaoStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Caso o valor no banco não seja válido
                    tipoOperacao = registroDeModificacoes.TipoOperacao.INSERÇÃO;
                }

                // Adicionar o registro à lista
                lista.add(new registroDeModificacoes(dadosNovos, dataOperacao, tipoOperacao));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Fechar os recursos
            try {
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lista;
    }
}
