package com.sistema_financiero_personal.cuentas.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;

public class DAOCuenta extends DAOBase<Cuenta> {

    public DAOCuenta() {super(Cuenta.class);}

}
