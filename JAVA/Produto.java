package bancodedados;

public class Produto {
    private int idproduto;
    private String nome;
    private String descricao;
    private double preco;
    private double precofabrica;
    private int quantidade;
    private int quantidadeMax;
    
    public Produto(int idproduto, String nome, String descricao, double preco, double precofabrica, int quantidade,  int quantidadeMax) {
    	
        this.idproduto = idproduto;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.precofabrica = precofabrica;
        this.quantidade = quantidade;
        this.quantidadeMax = quantidadeMax;
    }

    public int getId() { return idproduto; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }
    public double getPrecofabrica() { return precofabrica; }
    public int getQuant() { return quantidade; }
    public int getQuantMax() { return quantidadeMax; }
}

