package org.bench.junit5app.ejemplos.models;

import org.bench.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    private Cuenta cuenta;

    //--------------------------------------------------- HOOKS --------------------------------------------------------

    @BeforeAll
    static void beforeAll() {
        System.out.println("Antes de iniciar todos los test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizandos todos los test");
    }

    @BeforeEach
    void initMetodoTest() {
        System.out.println("Iniciando el test");
        this.cuenta = new Cuenta("Gonzalo", new BigDecimal("1000"));
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el test");
    }

//    -----------------------------------------------PARTE 1 JUNIT5 ----------------------------------------------------

    @Nested
    @DisplayName("A - NOMBRE Y SALDO")
    class CuentaTestNombreSaldo {
        @Test
        @DisplayName("A.1 - NOMBRE")
        void testNombreCuenta() {
            String esperado = "Gonzalo";
            String real = cuenta.getPersona();
            // PUEDO AGREGAR UN SEGUNDO PARAMETRO AL ASSERTNOTNULL, ASSERTEQUALS, ASSERTTRUE CON EL CUAL CUANDO
            // HAYA UN ERROR SE DISPARARA ESE PARAMETRO CON UN TEXTO DEL PORQUE FALLO EL TEST
            assertNotNull(real, "La cuenta no puede ser nula");
            assertEquals(esperado, real, "El nombre de la cuenta no es el que se esperaba: se esperaba " + esperado
                    + " sin embargo fue " + real);
            assertTrue(real.equals("Gonzalo"), "Nombre cuenta esperada debe ser igual a la real");
        }

        @Test
        @DisplayName("A.2 - SALDO")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("A.3 - REFERENCIAS")
        void testReferenciaCuenta() {
            cuenta = new Cuenta("Ricardo", new BigDecimal("8900"));
            Cuenta cuenta2 = new Cuenta("Ricardo", new BigDecimal("8900"));
            assertNotEquals(cuenta2, cuenta);
        }
    }

    @Nested
    @DisplayName("B - OPERACIONES")
    class CuentaOperacionesTest {
        @Test
        @DisplayName("B.1 - DEBITO")
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900", cuenta.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("B.2 - CREDITO")
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100", cuenta.getSaldo().toPlainString());

        }

        @Test
        @DisplayName("B.3 - TRANSFERENCIAS")
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("Ricardo", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Gonzalo", new BigDecimal("1500"));

            Banco banco = new Banco();
            banco.setNombre("Banco Santander");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("B.4 - DINERO EXCEPTION")
        void testDineroInsuficienteExceptionCuenta() {
            Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
                cuenta.debito(new BigDecimal(1500));
            });
            String actual = exception.getMessage();
            String esperado = "Dinero Insuficiente";
            assertEquals(esperado, actual);
        }

        @Test
        // CON DISABLE DESABILITAMOS EL TEST DEL METODO
        //@Disabled
        @DisplayName("B.5 - ASSERT ALL")
        void testRelacionBancoCuentas() {
            Cuenta cuenta1 = new Cuenta("Ricardo", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Gonzalo", new BigDecimal("1500"));

            Banco banco = new Banco();
            banco.addCuenta(cuenta1);
            banco.addCuenta(cuenta2);

            banco.setNombre("Banco Santander");

            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

            // ASSERTALL PERMITE QUE TODAS LAS AFIRMACIONES SE EVALÚEN Y LUEGO INFORMA SOBRE TODAS LAS FALLAS AL FINALIZAR.
            // ESTO ES ÚTIL PARA TENER UNA VISIÓN COMPLETA DE TODOS LOS PROBLEMAS EN UNA PRUEBA EN LUGAR DE TENER QUE SOLUCIONAR
            // UN PROBLEMA A LA VEZ.
            assertAll(
                    () -> assertEquals("1000", cuenta2.getSaldo().toPlainString(), "el valor del saldo de la cuenta2 no es el esperado."),
                    () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(), "el valor del saldo de la cuenta1 no es el esperado."),
                    () -> assertEquals(2, banco.getCuentas().size(), () -> "el banco no tienes las cuentas esperadas"),
                    () -> assertEquals("Banco Santander", cuenta1.getBanco().getNombre()),
                    () -> assertEquals("Gonzalo", banco.getCuentas()
                            .stream()
                            .filter(c -> c.getPersona().equals("Gonzalo"))
                            .findAny()
                            .get()
                            .getPersona()),
                    () -> assertTrue(banco.getCuentas()
                            .stream()
                            .anyMatch(account -> account.getPersona().equals("Ricardo")))
            );
        }
    }


    @Nested
    @DisplayName("C - ENABLE ON OS")
    class SistemaOperativoTest {
        @Test
        @DisplayName("C.1 - MAC")
        @EnabledOnOs(OS.MAC)
        void testSoloMac() {
        }

        @Test
        @DisplayName("C.2 - LINUX O MAC")
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
        }

        @Test
        @DisplayName("C.3 - WINDOWS")
        @EnabledOnOs({OS.WINDOWS})
        void testSoloWindows() {
        }
    }

    @Nested
    @DisplayName("D - ENABLE ON JRE")
    class JavaVersionTest {
        @Test
        @DisplayName("D.1 - ENABLE JAVA 8")
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {
        }

        @Test
        @DisplayName("D.2 - ENABLE JAVA 15")
        @EnabledOnJre(JRE.JAVA_15)
        void soloJDK15() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_8)
        @DisplayName("D.3 - DISABLE JAVA 8")
        void testNoJDK15() {
        }
    }

    @Nested
    @DisplayName("E - PROPIEDADES DE SISTEMA")
    class SistemPropertiesTest {
        @Test
        @DisplayName("E.1 - TRAZA DE PROPIEDADES")
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + ":" + v));
        }

        @Test
        @DisplayName("E.2 - CHECK JAVA VERSION")
        @EnabledIfSystemProperty(named = "java.version", matches = ".*15.*")
        void testJavaVersion() {
        }


        @Test
        @DisplayName("E.3 - CHECK USERNAME")
        @EnabledIfSystemProperty(named = "user.name", matches = "gonza")
        void testUsername() {
        }
    }

    @Nested
    @DisplayName("F - VARIABLES DE ENTORNO")
    class VariableAmbienteTest {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + " = " + v));
        }

        @Test
        @DisplayName("F.1 - JAVA_HOME")
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-15.0.1.*")
        void testJavaHome() {
        }
    }


    //    ------------------------------------ PARTE II PRUEBAS PARAMETRIZADAS ---------------------------------------------


    @Nested
    @DisplayName("G - PRUEBAS PARAMETRIZADAS")
    class PruebasParametrizadasTest {

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @DisplayName("G.1 - PASO DE VALORES")
        @ValueSource(strings = {"100", "200", "300", "500", "700"})
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @DisplayName("G.2 - TEST DE REPETICIONES")
        // REPEATEDTEST EL TEST VARIAS VECES DE ACUERDO A LOS PARAMETROS Y EL NOMBRE
        @RepeatedTest(value = 5, name = "{displayName} - Repetición numero {currentRepetition} de {totalRepetitions}")
            // LUEGO SE INYECTA EL VALOR DE LA CANTIDAD DE REPETICIONES QUE QUEREMOS HACER EN EL PARAMETRO DEL NOMBRE DEL METODO
            // (EJEMPLO DE ABAJO)
        void testDebitoCuentaRepetir(RepetitionInfo info) {
            if (info.getCurrentRepetition() == 3) {
                System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
            }
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900", cuenta.getSaldo().toPlainString());
        }
    }

    // ESTE TEST CON METHODSOURCE ES IGUAL QUE LA PRUEBA DE ARRIBA, CON LA DIFERENCIA DE QUE UTILIZAMOS UN METODO
    // PARA PASAR LOS PARAMETROS DEL TEST PARAMETRIZADO
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @DisplayName("H - METODO SOURCE")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700");
    }

    //    ------------------------------------ PARTE III TIME OUT  -----------------------------------------------------

    @Nested
    @DisplayName("I - TIME OUT")
    class EjemploTimeoutTest {
        @Test
        @DisplayName("I.1 - TIME OUT")
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

    }

}