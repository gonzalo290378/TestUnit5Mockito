package org.aguzman.test.springboot.app;

import org.aguzman.test.springboot.app.exceptions.DineroInsuficienteException;
import org.aguzman.test.springboot.app.models.Banco;
import org.aguzman.test.springboot.app.models.Cuenta;
import org.aguzman.test.springboot.app.repositories.BancoRepository;
import org.aguzman.test.springboot.app.repositories.CuentaRepository;
import org.aguzman.test.springboot.app.services.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.math.BigDecimal;
import static org.aguzman.test.springboot.app.Datos.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//ACA NO TENEMOS QUE CONFIGURAR ANOTACIONES COMO EXTENDWITH YA QUE SPRING LO AUTOCONFIGURA DE MANERA AUTOMATICA
@SpringBootTest
class SpringbootTestApplicationTests {

    //UTILIZA @MOCKBEAN EN EL CONTEXTO DE SPRING EN VEZ DE @MOCK
    @MockBean
    CuentaRepository cuentaRepository;

    //UTILIZA @MOCKBEAN EN EL CONTEXTO DE SPRING EN VEZ DE @MOCK
    @MockBean
    BancoRepository bancoRepository;

    //UTILIZA @AUTOWIRED EN EL CONTEXTO DE SPRING EN VEZ DE @INYECTMOCKS, POR LO CUAL PODEMOS INYECTAR LA INTERFACE
    //SIN PROBLEMA
    @Autowired
    CuentaService service;

    @BeforeEach
    void setUp() {
//		cuentaRepository = mock(CuentaRepository.class);
//		bancoRepository = mock(BancoRepository.class);
//		service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
//		Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
//		Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
//		Datos.BANCO.setTotalTransferencias(0);
    }

    @Test
    void contextLoads() {
        //MOCK / GIVEN
        when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
        when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
        when(bancoRepository.findById(1L)).thenReturn(crearBanco());

        //TEST LOGICA DE NEGOCIO / WHEN
        service.transferir(1L, 2L, new BigDecimal("100"), 1L);
        BigDecimal saldoOrigen = service.revisarSaldo(1L);
        BigDecimal saldoDestino = service.revisarSaldo(2L);
        int total = service.revisarTotalTransferencias(1L);

        //ASSERTIONS / THEN
        assertEquals("900", saldoOrigen.toPlainString());
        assertEquals("2100", saldoDestino.toPlainString());
        assertEquals(1, total);

        //VERIFY APARTE DE CHEQUEAR QUE SE HAYA REALIZADO EL TEST EN EL METODO, TAMBIEN CHEQUEA CUANTAS VECES SE PROBO
        verify(cuentaRepository, times(2)).findById(1L);
        verify(cuentaRepository, times(2)).findById(2L);
        verify(cuentaRepository, times(2)).update(any(Cuenta.class));
        verify(bancoRepository, times(2)).findById(1L);
        verify(bancoRepository).update(any(Banco.class));
        verify(cuentaRepository, times(4)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();
    }

    @Test
    void contextLoads2() {
        //MOCK / GIVEN
        when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
        when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
        when(bancoRepository.findById(1L)).thenReturn(crearBanco());

        //TEST LOGICA DE NEGOCIO / WHEN
        assertThrows(DineroInsuficienteException.class, () -> {
            service.transferir(1L, 2L, new BigDecimal("1200"), 1L);
        });

        BigDecimal saldoOrigen = service.revisarSaldo(1L);
        BigDecimal saldoDestino = service.revisarSaldo(2L);
        int total = service.revisarTotalTransferencias(1L);


        //ASSERTIONS / THEN
        assertEquals("1000", saldoOrigen.toPlainString());
        assertEquals("2000", saldoDestino.toPlainString());
        assertEquals(0, total);

        //VERIFY APARTE DE CHEQUEAR QUE SE HAYA REALIZADO EL TEST EN EL METODO, TAMBIEN CHEQUEA CUANTAS VECES SE PROBO
        verify(cuentaRepository, times(2)).findById(1L);
        verify(cuentaRepository, times(1)).findById(2L);
        verify(cuentaRepository, never()).update(any(Cuenta.class));
        verify(bancoRepository, times(1)).findById(1L);
        verify(bancoRepository, never()).update(any(Banco.class));
        verify(cuentaRepository, times(3)).findById(anyLong());
        verify(cuentaRepository, never()).findAll();
    }

    @Test
    void contextLoads3() {
        when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());

        Cuenta cuenta1 = service.findById(1L);
        Cuenta cuenta2 = service.findById(1L);

        //Es lo mismo assertSame que assertTue
        assertSame(cuenta1, cuenta2);
        assertTrue(cuenta1 == cuenta2);
        assertEquals("Andrés", cuenta1.getPersona());
        assertEquals("Andrés", cuenta2.getPersona());

        verify(cuentaRepository, times(2)).findById(1L);
    }
}
