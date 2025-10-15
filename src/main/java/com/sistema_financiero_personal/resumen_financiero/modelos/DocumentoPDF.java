package com.sistema_financiero_personal.resumen_financiero.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "documento_pdf")
public class DocumentoPDF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documento_pdf_id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Lob
    @Column(name = "archivo_pdf", nullable = false)
    private byte[] archivoPdf;

    @Column(name = "tamanio", nullable = false)
    private Long tamanio;

    // Constructor vacío requerido por JPA/Hibernate
    public DocumentoPDF() {
    }

    public DocumentoPDF(String nombre, byte[] archivoPdf, Long tamanio) {
        this.nombre = nombre;
        this.archivoPdf = archivoPdf;
        this.tamanio = tamanio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public byte[] getArchivoPdf() { return archivoPdf; }
    public void setArchivoPdf(byte[] archivoPdf) { this.archivoPdf = archivoPdf; }


    public Long getTamanio() { return tamanio; }
    public void setTamanio(Long tamanio) { this.tamanio = tamanio; }


}