    package com.sistema_financiero_personal.movimiento.modelos;

    import jakarta.persistence.*;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;

    @Entity
    @DiscriminatorValue("INGRESO")
    public class Ingreso extends Movimiento {
        @Enumerated(EnumType.STRING)
        @Column(name = "categoria")
        CategoriaIngreso categoria;
        public Ingreso() { }
        public Ingreso(double monto, LocalDateTime fecha, String descripcion, CategoriaIngreso categoria) {
            super(monto, fecha, descripcion);
            this.categoria = categoria;
        }

        public CategoriaIngreso getCategoria() {
            return categoria;
        }

        public void setCategoria(CategoriaIngreso categoria) {
            this.categoria = categoria;
        }
    }

