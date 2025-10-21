package bancodedados;

import java.sql.Time;

public class HoraComMaisVendas {

    private Time hora;

    // Construtor que recebe a hora como argumento
    public HoraComMaisVendas(Time hora) {
        this.hora = hora;
    }

    // Getter para o atributo hora
    public Time getHora() {
        return hora;
    }

    // Setter para o atributo hora
    public void setHora(Time hora) {
        this.hora = hora;
    }
}
