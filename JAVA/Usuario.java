package bancodedados;

import java.sql.Date;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String email;
	private String celular;
    private String datanascimento;
    private int idbanco;
    private Date datainscricao;
	private String cnpj;
    
    public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getDatanascimento() {
		return datanascimento;
	}

	public void setDatanascimento(String datanascimento) {
		this.datanascimento = datanascimento;
	}

	public int getIdbanco() {
		return idbanco;
	}

	public void setIdbanco(int idbanco) {
		this.idbanco = idbanco;
	}

	public Date getDatainscricao() {
		return datainscricao;
	}

	public void setDatainscricao(Date datainscricao) {
		this.datainscricao = datainscricao;
	}

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

}
