package bancodedados;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cadastroUsuario")
public class cadastroUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public cadastroUsuario() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obter os parâmetros do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String celular = request.getParameter("celular");
        String datanascimento = request.getParameter("datanascmento");
        String cnpj = request.getParameter("cnpj");
        System.out.println(cnpj);
	
        // Obter a data atual para datainscricao
        Date dataInscricao = new Date(System.currentTimeMillis());

        String checkEmailSql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        String insertSql = "INSERT INTO usuario (nomeusuario, email, senha, celular, datanascimento, datainscricao, cnpj) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE usuario SET idbanco = ? WHERE idusuario = ?";

        try (Connection connection = cnx.getConnection();
             PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailSql);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {

            // Verificar se o email já existe no banco de dados
            checkEmailStmt.setString(1, email);
            
            ResultSet rs = checkEmailStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // Email já existe, não inserir
                request.setAttribute("errorMessage", "Erro: O email fornecido não está disponível.");
                // Redirecionar para a página de cadastro para exibir a mensagem
                request.getRequestDispatcher("cadastroUsuario.jsp").forward(request, response);
                return;
            }
            
            // Inserir o usuário          
            insertStatement.setString(1, nome);
            insertStatement.setString(2, email);
            insertStatement.setString(3, senha);
            insertStatement.setString(4, celular);
            insertStatement.setString(5, datanascimento);
            insertStatement.setDate(6, dataInscricao);
            insertStatement.setString(7, cnpj);

            int rowsInserted = insertStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Usuário cadastrado com sucesso!");

                // Obter o idusuario gerado
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idusuario = generatedKeys.getInt(1);

                        // Gerar um idbanco usando idusuario e número aleatório de 6 dígitos
                        int numeroAleatorio = new Random().nextInt(900000) + 100000; // Número entre 100000 e 999999
                        String idbanco = idusuario + String.valueOf(numeroAleatorio);

                        // Atualizar o idbanco no banco de dados
                        updateStatement.setString(1, idbanco);
                        updateStatement.setInt(2, idusuario);

                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            // Criar banco de dados e tabelas
                            criarBancoDeDados(connection, idbanco);

                            response.sendRedirect("login.jsp"); // Redireciona para login.jsp
                        } else {
                            System.out.println("Não foi possível atualizar o idbanco.");
                        }
                    } else {
                        System.out.println("Erro ao obter o idusuario gerado.");
                    }
                }
            } else {
                System.out.println("Não foi possível cadastrar o usuário.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir ou atualizar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void criarBancoDeDados(Connection connection, String idbanco) throws SQLException {
        String dbName = "banco_" + idbanco; // Nome do banco baseado no idbanco
        try (Statement statement = connection.createStatement()) {
            // Criar o banco de dados
            statement.executeUpdate("CREATE DATABASE " + dbName);

            // Usar o banco de dados recém-criado
            statement.executeUpdate("USE " + dbName);

            // Criar tabelas no novo banco de dados
            String[] tabelas = {
                "CREATE TABLE `carrinho` ("
                        + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                        + "`produto_id` int(11) DEFAULT NULL,"
                        + "`quantidade` int(11) DEFAULT 1,"
                        + "PRIMARY KEY (`id`),"
                        + "KEY `produto_id` (`produto_id`)"
                        + ")",
                "CREATE TABLE `itens_venda` ("
                        + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                        + "`venda_id` int(11) DEFAULT NULL,"
                        + "`produto_id` int(11) DEFAULT NULL,"
                        + "`quantidade` int(11) DEFAULT NULL,"
                        + "`preco_de_venda` float NOT NULL,"
                        + "`preco_de_fabrica` float NOT NULL,"
                        + "PRIMARY KEY (`id`),"
                        + "KEY `venda_id` (`venda_id`),"
                        + "KEY `produto_id` (`produto_id`)"
                        + ")",
                "CREATE TABLE `produtos` ("
                        + "`idproduto` int(8) NOT NULL AUTO_INCREMENT,"
                        + "`nome` varchar(100) NOT NULL,"
                        + "`preco` float NOT NULL,"
                        + "`precofabrica` float NOT NULL,"
                        + "`descricao` varchar(200) NOT NULL,"
                        + "`quant` int(3) NOT NULL,"
                        + "`quantmax` int(3) NOT NULL,"
                        + "PRIMARY KEY (`idproduto`)"
                        + ")",
                "CREATE TABLE `vendas` ("
                        + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                        + "`data` date NOT NULL DEFAULT current_timestamp(),"
                        + "`valor_total` decimal(10,2) NOT NULL,"
                        + "`horario_venda` time NOT NULL,"
                        + "PRIMARY KEY (`id`)"
                        + ")",
                "CREATE TABLE log_alteracoes (\r\n"
                + "    id INT AUTO_INCREMENT PRIMARY KEY,\r\n"
                + "    tipo_operacao ENUM('REESTOCAR','INSERÇÃO', 'EDIÇÃO', 'REMOÇÃO') NOT NULL,\r\n"
                + "    tabela_afetada VARCHAR(50) NOT NULL,\r\n"
                + "    dados_antigos TEXT,\r\n"
                + "    dados_novos TEXT,\r\n"
                + "    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP\r\n"
                + ")",
                // Adicionar as chaves estrangeiras
                "ALTER TABLE `carrinho` ADD CONSTRAINT `carrinho_ibfk_1` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`idproduto`)",
                "ALTER TABLE `itens_venda` ADD CONSTRAINT `itens_venda_ibfk_1` FOREIGN KEY (`venda_id`) REFERENCES `vendas` (`id`),"
                        + "ADD CONSTRAINT `itens_venda_ibfk_2` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`idproduto`)"
            };

            for (String sql : tabelas) {
                statement.executeUpdate(sql);
            }
        }
    }
}
