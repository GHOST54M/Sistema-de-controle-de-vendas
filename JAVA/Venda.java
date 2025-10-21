package bancodedados;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Venda {
    private int id;
    private Date data;
    private double valorTotal;
    private Time horario_venda;
    public Venda(int id, Date data, double valorTotal) {
        this.id = id;
        this.data = data;
        this.valorTotal = valorTotal;
    }

	// Getters e setters
    public int getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public double getValorTotal() {
        return valorTotal;
    }

	public void setId(int int1) {
		// TODO Auto-generated method stub
		
	}

	public void setData(Timestamp timestamp) {
		// TODO Auto-generated method stub
		
	}

	public void setValorTotal(double valorTotal) {
		// TODO Auto-generated method stub
		
	}

	public Time getHorario_venda() {
		return horario_venda;
	}

	public void setHorario_venda(Time horario_venda) {
		this.horario_venda = horario_venda;
	}


}
