package bancodedados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class cnx {
    private static final String usuario = "root";
    private static final String senha = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // Conexão padrão ao banco global
        String url = "jdbc:mysql://localhost:3306/modeloads";
        return DriverManager.getConnection(url, usuario, senha);
    }

    // Alterado para aceitar idusuario como parâmetro
    public static Connection getConnectionForUser(int idusuario) throws SQLException {
        // Busca o idbanco associado ao idusuario
        String idbanco = buscarIdBanco(idusuario);
        if (idbanco == null) {
            throw new SQLException("Banco de dados correspondente ao usuário não encontrado.");
        }

        // Construir a URL para o banco de dados do usuário
        String url = "jdbc:mysql://localhost:3306/banco_" + idbanco;
        return DriverManager.getConnection(url, usuario, senha);
    }

    public static String buscarIdBanco(int idusuario) {
        String idbanco = null;
        String query = "SELECT idbanco FROM usuario WHERE idusuario = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idusuario); // Usando o tipo correto para idusuario

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    idbanco = resultSet.getString("idbanco");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idbanco;
    }
}
