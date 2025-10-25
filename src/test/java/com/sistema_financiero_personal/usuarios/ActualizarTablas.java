package com.sistema_financiero_personal.usuarios;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class ActualizarTablas {
    @Test
    public void crearRecordatorio(){
        try {
            System.out.println("🔧 Iniciando...");

            // Forma simple para Hibernate 6.x
            SessionFactory factory = new Configuration().configure().buildSessionFactory();

            System.out.println("✅ Tabla creada!");

            factory.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }

