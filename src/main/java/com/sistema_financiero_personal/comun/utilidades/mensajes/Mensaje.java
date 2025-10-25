package com.sistema_financiero_personal.comun.utilidades.mensajes;

public class Mensaje {
    private TipoMensaje tipo;
    private String texto;

    public Mensaje(TipoMensaje tipo, String texto) {
        this.tipo = tipo;
        this.texto = texto;
    }

    public TipoMensaje getTipo() {
        return tipo;
    }

    public void setTipo(TipoMensaje tipo) {
        this.tipo = tipo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    // Método para obtener el tipo como string para JavaScript
    public String getTipoString() {
        return tipo.getTipo();
    }
}