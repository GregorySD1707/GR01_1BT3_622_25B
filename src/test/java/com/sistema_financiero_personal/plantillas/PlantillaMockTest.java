package com.sistema_financiero_personal.plantillas;

import com.sistema_financiero_personal.movimiento.modelos.*;
import com.sistema_financiero_personal.plantillas.daos.DAOPlantilla;
import com.sistema_financiero_personal.plantillas.servicios.ServicioPlantilla;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;


public class PlantillaMockTest {

    private DAOPlantilla daoPlantillaMock;
    private ServicioPlantilla servicioPlantillas;

    @BeforeEach
    void setUp() {
        daoPlantillaMock = Mockito.mock(DAOPlantilla.class);
        servicioPlantillas = new ServicioPlantilla(daoPlantillaMock);
    }

    @Test
    public void given_data_when_create_template_then_delegates_to_dao() {
        Long usuarioId = 1L;
        String nombre = "Pago Arriendo";
        double monto = 35.0;
        CategoriaGasto categoriaGasto = CategoriaGasto.SERVICIOS;

        Plantilla plantilla = new Plantilla();
        plantilla.setNombre(nombre);
        plantilla.setMonto(monto);
        plantilla.setCategoriaGasto(categoriaGasto);

        when(daoPlantillaMock.crearPlantilla(plantilla, usuarioId)).thenReturn(true);

        servicioPlantillas.crearPlantilla(plantilla, usuarioId);

        verify(daoPlantillaMock, times(1)).crearPlantilla(plantilla, usuarioId);
    }

    @Test
    public void given_data_when_delete_template_then_delegates_to_dao() {
        Long plantilla_Id = 1L;

        when(daoPlantillaMock.eliminarPlantilla(plantilla_Id)).thenReturn(true);

        servicioPlantillas.eliminarPlantilla(plantilla_Id);

        verify(daoPlantillaMock, times(1)).eliminarPlantilla(plantilla_Id);
    }


}
