package com.sistema_financiero_personal.modelos;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Table(name="recordatorio")

public class Recordatorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recordatorio_id")
    private Long id;
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(length = 50)
    private String descripcion;
    private Recurrencia recurrencia;
    @Column(name = "dias_de_anticipacion")
    private int diasDeAnticipacion;
    private double monto;

    public Recordatorio(){}

    public Recordatorio(LocalDate fechaInicio, LocalDate fechaFin, String descripcion, Recurrencia recurrencia, double monto, int diasDeAnticipacion) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descripcion = descripcion;
        this.recurrencia = recurrencia;
        this.diasDeAnticipacion = diasDeAnticipacion;
        this.monto = monto;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Recurrencia getRecurrencia() {
        return recurrencia;
    }

    public void setRecurrencia(Recurrencia recurrencia) {
        this.recurrencia = recurrencia;
    }

    public int getDiasDeAnticipacion() {
        return diasDeAnticipacion;
    }

    public void setDiasDeAnticipacion(int diasDeAnticipacion) {
        this.diasDeAnticipacion = diasDeAnticipacion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Determina si este recordatorio debe notificarse en la fecha dada
     * @param hoy la fecha actual
     * @return true si debe notificarse hoy
     */
        public Optional <LocalDate> obtenerFechaVencimiento(LocalDate hoy) {
            // 1. Descartar si la fecha de hoy está fuera del rango general del recordatorio.
            if (hoy.isAfter(fechaFin)) {
                return Optional.empty();
            }

            // CASO 1: Validación general
            LocalDate proximaFechaVencimiento = (recurrencia == Recurrencia.NINGUNA)
                    ? fechaInicio
                    : calcularProximaFechaVencimiento(hoy);

                    
            if (proximaFechaVencimiento == null) {
                return Optional.empty();
            }
            
            int diasDeAnticipacionAUX = diasDeAnticipacion;

            // CASO 2: Recurrencia DIARIA
            if (recurrencia == Recurrencia.DIARIA && !hoy.isBefore(fechaInicio)) {
                diasDeAnticipacionAUX = 0; // Notificar todos los días desde inicio
            }

            LocalDate fechaNotificacion = proximaFechaVencimiento.minusDays(diasDeAnticipacionAUX);

            // 3. Determinar el período de notificación para esa próxima fecha.

            // 4. Verificar si 'hoy' está dentro del período de notificación válido.
            // El período es [inicioNotificacion, proximaFechaVencimiento].
            boolean debeNotificarse = !hoy.isBefore(fechaNotificacion) && !hoy.isAfter(proximaFechaVencimiento);

            if(debeNotificarse) {
                return Optional.of(proximaFechaVencimiento);
            } else {
                return Optional.empty();
            }
        }

        /**
         * Calcula la próxima fecha de vencimiento a partir de la fecha de inicio,
         * que sea igual o posterior a la fecha 'desde'.
         * @param desde La fecha de referencia para el cálculo.
         * @return La próxima fecha de vencimiento o null si ninguna es válida.
         */
        private LocalDate calcularProximaFechaVencimiento(LocalDate desde) {
            LocalDate proximaFecha = fechaInicio;

            // Si 'desde' ya es posterior a la fecha de inicio, adelantamos proximaFecha
            // para encontrar la ocurrencia relevante.
            if (proximaFecha.isBefore(desde)) {
                switch (recurrencia) {
                    case DIARIA:
                        // Calcula cuántos días han pasado y suma los necesarios.
                        proximaFecha = desde;           
                         break;
                    case SEMANAL:
                        long semanasDeDiferencia = java.time.temporal.ChronoUnit.WEEKS.between(fechaInicio, desde);
                        proximaFecha = fechaInicio.plusWeeks(semanasDeDiferencia);
                        if (proximaFecha.isBefore(desde)) {
                            proximaFecha = proximaFecha.plusWeeks(1);
                        }
                        break;
                    case MENSUAL:
                        long mesesDeDiferencia = java.time.temporal.ChronoUnit.MONTHS.between(fechaInicio, desde);
                        proximaFecha = fechaInicio.plusMonths(mesesDeDiferencia);
                        if (proximaFecha.isBefore(desde)) {
                            proximaFecha = proximaFecha.plusMonths(1);
                        }
                        break;
                    case ANUAL:
                        long añosDeDiferencia = java.time.temporal.ChronoUnit.YEARS.between(fechaInicio, desde);
                        proximaFecha = fechaInicio.plusYears(añosDeDiferencia);
                        if (proximaFecha.isBefore(desde)) {
                            proximaFecha = proximaFecha.plusYears(1);
                        }
                        break;
                    default:
                        return null;
                }
            }
            
            // Asegurarnos que la fecha calculada no supere la fecha de fin.
            return proximaFecha.isAfter(fechaFin) ? null : proximaFecha;
        }
}

