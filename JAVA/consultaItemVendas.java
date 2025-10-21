package bancodedados;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class consultaItemVendas {

    public List<ItemVenda> listarVendasDetalhes() {
        List<ItemVenda> item_vendas = new ArrayList<>();

        try (Connection conexao = cnx.getConnection();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM itens_vendas WHERE produto_id = ?")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int venda_id = rs.getInt("venda_id");
                int produto_id = rs.getInt("produto_id");
                int quantidade = rs.getInt("quantidade");
                item_vendas.add(new ItemVenda(id, venda_id, produto_id, quantidade, quantidade));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return item_vendas;
    }
}
