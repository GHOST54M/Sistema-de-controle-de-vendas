package bancodedados;

public class ItensDetalhados {
	
	private String nome;
	private int quantidade;
	private double precoFabrica;
	private double precoVenda;

    public ItensDetalhados(String nomeProduto, int quantidadeVendida, double precoProduto, double precoVenda) {
        this.nome = nomeProduto;
        this.quantidade = quantidadeVendida;
        this.precoFabrica = precoProduto;
        this.precoVenda = precoVenda;
    }
    
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPrecoFabrica() {
		return precoFabrica;
	}
	public void setPrecoFabrica(double preco) {
		this.precoFabrica = preco;
	}

	public double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(double precoVenda) {
		this.precoVenda = precoVenda;
	}

}
