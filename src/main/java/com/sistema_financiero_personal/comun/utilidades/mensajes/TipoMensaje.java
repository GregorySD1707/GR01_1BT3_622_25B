package com.sistema_financiero_personal.comun.utilidades.mensajes;

public enum TipoMensaje {
    EXITO("success"),
    ERROR("error"),
    ADVERTENCIA("warning"),
    INFO("info");

    private final String tipo;

    TipoMensaje(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}