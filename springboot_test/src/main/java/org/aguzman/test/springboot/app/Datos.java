package org.aguzman.test.springboot.app;

import org.aguzman.test.springboot.app.models.Banco;
import org.aguzman.test.springboot.app.models.Cuenta;

import java.math.BigDecimal;

public class Datos {
    //COMO REALIZAMOS DEBITOS EN UNA PRUEBA UNITARIA Y AL SER FINAL LA VARIABLE, AL REALIZAR LA TRANSFERENCIA
    //YA DESCUENTA EL VALOR EN LA CUENTA, POR LO QUE VA A PRODUCIR UN ERROR EN EL OTRO TEST UNITARIO.
    //PARA ELLO ES MEJOR QUE CREEMOS METODOS PARA REALIZAR LAS OPERACIONES Y CREACIONES DE NUEVAS INSTANCIAS

    /*
    public static final Cuenta CUENTA_001 = new Cuenta(1L, "Andrés", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002 = new Cuenta(2L, "Jhon", new BigDecimal("2000"));
    public static final Banco BANCO = new Banco(1L, "El banco financiero", 0);*/

    public static Cuenta crearCuenta001() {
        return new Cuenta(1L, "Andrés", new BigDecimal("1000"));
    }

    public static Cuenta crearCuenta002() {
        return new Cuenta(2L, "Jhon", new BigDecimal("2000"));
    }

    public static Banco crearBanco() {
        return new Banco(1L, "El banco financiero", 0);
    }
}
