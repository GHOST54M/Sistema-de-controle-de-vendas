package bancodedados;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class consultarProdutos {

    public List<Produto> listarProdutos(Integer idusuario) {
        List<Produto> produtos = new ArrayList<>();

        try (Connection conexao = cnx.getConnectionForUser(idusuario);
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produtos")) {

            while (rs.next()) {
                int idproduto = rs.getInt("idproduto");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                double precofabrica = rs.getDouble("precofabrica");
                int quantidade = rs.getInt("quant");
                int quantidadeMax = rs.getInt("quantMax");
                produtos.add(new Produto(idproduto, nome, descricao, preco, precofabrica, quantidade, quantidadeMax));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }
}
