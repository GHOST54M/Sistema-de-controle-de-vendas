package bancodedados;

public class registroDeModificacoes {
	private String dadosNovos;
    private java.sql.Timestamp dataOperacao2;
    public TipoOperacao tipoOperacao;
    
    public enum TipoOperacao {
        INSERÇÃO, EDICAO, REMOÇÃO, REESTOCAR
    }
    
    public TipoOperacao getTipoOperacao() {
        return tipoOperacao;
    }
    
    public void setTipoOperacao(TipoOperacao tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }
    
    public registroDeModificacoes(String dadosNovos2, java.sql.Timestamp dataOperacao2, TipoOperacao tipoOperacao) {
        this.dadosNovos = dadosNovos2;
        this.setDataOperacao2(dataOperacao2);
        this.tipoOperacao = tipoOperacao;

    }

	public String getDadosNovos() {
        return dadosNovos;
    }

    public void setDadosNovos(String dadosNovos) {
        this.dadosNovos = dadosNovos;
    }
    
    public String toString() {
        return "Dados Novos: " + dadosNovos + ", Data da Operação: " + dataOperacao2;
    }

	public java.sql.Timestamp getDataOperacao2() {
		return dataOperacao2;
	}

	public void setDataOperacao2(java.sql.Timestamp dataOperacao2) {
		this.dataOperacao2 = dataOperacao2;
	}

}
