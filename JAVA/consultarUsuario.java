package bancodedados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class consultarUsuario {
	// Função para obter as informações do usuário
    public Usuario getUsuarioById(int userId) {
        Usuario usuario = null;

        String sql = "SELECT * FROM usuario WHERE idusuario = ?";

        try (Connection conn = cnx.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setNome(rs.getString("nomeusuario"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setCelular(rs.getString("celular"));
                    usuario.setDatanascimento(rs.getString("datanascimento"));
                    usuario.setIdbanco(rs.getInt("idbanco"));
                    usuario.setDatainscricao(rs.getDate("datainscricao"));
                    usuario.setCnpj(rs.getString("cnpj"));

                    // Adicione aqui outros campos conforme necessário
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
}
