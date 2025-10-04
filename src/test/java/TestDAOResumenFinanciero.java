import com.sistema_financiero_personal.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.modelos.ResumenFinanciero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import  java.util.List;

public class TestDAOResumenFinanciero {

    private DAOResumenFinanciero daoResumenFinanciero;

    @BeforeEach
    void setUp(){
        daoResumenFinanciero = new DAOResumenFinanciero();
    }

    @Test
    void testGuardarYListar(){
        ResumenFinanciero resumenFinanciero = new ResumenFinanciero();

        double ingresos = 1.23;
        double gastos = 4.56;

        resumenFinanciero.setIngresosTotales(ingresos);
        resumenFinanciero.setGastosTotales(gastos);
        resumenFinanciero.setAhorroNeto(ingresos-gastos);

        daoResumenFinanciero.guardar(resumenFinanciero);

        List<ResumenFinanciero> resumenes = daoResumenFinanciero.listar();

        assertNotNull(resumenes);
        assertFalse(resumenes.isEmpty());
        assertTrue(resumenes.stream().anyMatch(u -> u.getIngresosTotales() == ingresos));

    }

}
