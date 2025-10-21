package bancodedados;

public class ItemVenda {
    private Integer id;
    private Integer venda_id;
    private Integer produto_id;
    private Integer quantidade;
    private Integer totalQuantidade;
    private String nomeProduto;

	private Double retabilidade;

    public ItemVenda(Integer id, Integer venda_id, Integer produto_id, Integer quantidade, Integer totalQuantidade) {
        this.id = id;
        this.venda_id = venda_id;
        this.produto_id = produto_id;
        this.quantidade = quantidade;
        this.totalQuantidade = totalQuantidade;
    }
    public ItemVenda(int produto_id, int totalQuantidade, String nomeProduto) {
        this.produto_id = produto_id;
        this.totalQuantidade = totalQuantidade;
        this.nomeProduto = nomeProduto;
    }
    public ItemVenda(String nomeProduto, Double retabilidade) {
        this.nomeProduto = nomeProduto;
        this.retabilidade = retabilidade;
    }

    // Getters e setters
    public Integer getId() {
        return id;
    }

    public Integer getVenda_Id() {
        return venda_id;
    }

    public Integer getProduto_Id() {
        return produto_id;
    }
    
    public Integer getQuantidade() {
    	return quantidade;
    }
    public Integer getTotalQuantidade() {
    	return totalQuantidade;
    }
    public String getNomeProduto() {
        return nomeProduto;
    }
    public Double getRetabilidade() {
		return retabilidade;
	}
}
