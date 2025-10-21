package bancodedados;

public class ItemCarrinho {
	 
	private int id;
    private String nome;
    private double preco;
    private int quantidade;
	private double precofabrica;
	
    public double getPrecofabrica() {
		return precofabrica;
	}
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public ItemCarrinho(int id, String nome, double preco, int quantidade, double precofabrica) {
	        this.id = id;
	        this.nome = nome;
	        this.preco = preco;
	        this.quantidade = quantidade;
	        this.precofabrica = precofabrica;
	    }
}
