package org.bench.appmockito.ejemplos.services;

import org.bench.appmockito.ejemplos.Datos;
import org.bench.appmockito.ejemplos.models.Examen;
import org.bench.appmockito.ejemplos.repositories.ExamenRepository;
import org.bench.appmockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//EXTENDWITH: ANOTACION IMPORTANTE HABILITAR MOCKITO EN JUNIT 5 Y LA INYECCIÓN AUTOMÁTICA DE MOCKS: @MOCK, @SPY,
//@INJECTMOCKS, ENTRE OTROS.
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    //ES IGUAL A: ExamenRepositorio repositorio = mock(ExamenRepositorio.class)
    //DONDE LOS MOCK TIENEN QUE SER INTERFACES
    @Mock
    ExamenRepository examenRepository;

    @Mock
    PreguntaRepository preguntaRepository;

    //ES MUY IMPORTANTE QUE PARA IMPLEMENTAR LA ANOTACION @INJECTMOCKS)EN EXAMENSERVICEIMPL (QUE SIEMPRE TIENE QUE SER
    //  UNA IMPLEMENTACION) SE INYECTE POR CONTRUCTOR LOS 2 REPOSITORIES Y QUE EXAMENSERVICEIMPL
    @InjectMocks
    ExamenServiceImpl examenServiceImp;

    @Test
    void findExamenPorNombre() {
        ///////////////////// EJEMPLO SIN UTILIZAR ANOTACIONES @MOCK Y @INJECTMOCKS ////////////////////////////////////
        ExamenRepository repository = mock(ExamenRepository.class);
        ExamenService service = new ExamenServiceImpl(repository, null);
        Arrays.asList(
                new Examen(5L, "Matemáticas"),
                new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));

        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        //TEST LOGICA DE NEGOCIO / WHEN
        Optional<Examen> examen = service.findExamenPorNombre("Matemáticas");

        //ASSERTIONS / THEN
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matemáticas", examen.get().getNombre());

        ///////////////////// EJEMPLO UTILIZANDO ANOTACIONES @MOCK Y @INJECTMOCKS //////////////////////////////////////

        //MOCK / GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        //TEST LOGICA DE NEGOCIO / WHEN
        Optional<Examen> examen1 = examenServiceImp.findExamenPorNombre("Matemáticas");

        //ASSERTIONS / THEN
        assertTrue(examen1.isPresent());
        assertEquals(5L, examen1.orElseThrow().getId());
        assertEquals("Matemáticas", examen1.get().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        //MOCK / GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EMPTY_LIST);

        //TEST LOGICA DE NEGOCIO / WHEN
        Optional<Examen> examen = examenServiceImp.findExamenPorNombre("Matemáticas");

        //ASSERTIONS / THEN
        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        //MOCK / GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //TEST LOGICA DE NEGOCIO / WHEN
        Examen examen = examenServiceImp.findExamenPorNombreConPreguntas("Historia");

        //ASSERTIONS / THEN
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("derivadas"));
    }

    @Test
    void testPreguntasExamenVerify() {
        //MOCK / GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //TEST LOGICA DE NEGOCIO / WHEN
        Examen examen = examenServiceImp.findExamenPorNombreConPreguntas("Matemáticas");

        //ASSERTIONS / THEN
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));

        //VERIFY: VERIFICA SI LOS METODOS DE LOS MOCK FUERON LLAMADOS
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());

    }

    @Test
    void testNoExisteExamenVerify() {
        //MOCK / GIVEN
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //TEST LOGICA DE NEGOCIO / WHEN
        Examen examen = examenServiceImp.findExamenPorNombreConPreguntas("Matemáticas");

        //ASSERTIONS / THEN
        assertNull(examen);

        //VERIFY: VERIFICA SI LOS METODOS DE LOS MOCK FUERON LLAMADOS
        verify(examenRepository).findAll();
        //ESTE VERIFY FALLA PORQUE EL OBJETO AL SER NULL EJECUTA EL TEST LOGICA DE NEGOCIO
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }


    @Test
    void testGuardarExamen() {
        //MOCK / GIVEN
        when(examenRepository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN_FISICA);

        //TEST LOGICA DE NEGOCIO / WHEN
        Examen newExamen = Datos.EXAMEN_FISICA;
        newExamen.setPreguntas(Datos.PREGUNTAS_FISICA);
        Examen examen = examenServiceImp.guardar(newExamen);

        //ASSERTIONS / THEN
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Física", examen.getNombre());
        assertEquals(2, examen.getPreguntas().size());

        //VERIFY: VERIFICA SI LOS METODOS DE LOS MOCK FUERON LLAMADOS
        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testManejoException() {
        //MOCK / GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(RuntimeException.class);

        //ASSERTIONS / THEN
        assertThrows(RuntimeException.class, () -> {
            examenServiceImp.findExamenPorNombreConPreguntas("Matemáticas");
        });

        //VERIFY: VERIFICA SI LOS METODOS DE LOS MOCK FUERON LLAMADOS
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(isNull());
    }

    //UTILIZAMOS DOTHROW CUANDO EL METODO QUE QUEREMOS HACER EL TEST ES VOID
    @Test
    void testDoThrow() {
        //MOCK / GIVEN
        doThrow(RuntimeException.class).when(preguntaRepository).guardarVarias(anyList());

        //TEST LOGICA DE NEGOCIO / WHEN
        Examen examen = Datos.EXAMEN_FISICA;
        examen.setPreguntas(Datos.PREGUNTAS);

        //ASSERTIONS / THEN
        assertThrows(RuntimeException.class, () -> {
            examenServiceImp.guardar(examen);
        });
    }

    @Test
    void testArgumentMatchers() {
        //MOCK / GIVEN
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //ASSERTIONS / THEN
        Examen examen = examenServiceImp.findExamenPorNombreConPreguntas("Matemáticas");
        assertEquals(5L, examen.getId());

        //VERIFY: VERIFICA SI LOS METODOS DE LOS MOCK FUERON LLAMADOS
        verify(examenRepository).findAll();

        //ARGTHAT NOS PERMITE VERIFICAR QUE SE HAYA PASADO POR EL METODO FINDPREGUNTASPOREXAMENID Y QUE ADEMAS SE PASE
        //POR PARAMETRO UN VALOR A DICHO METODO TOTALMENTE CUSTOMIZABLE CON LAMBDAS.
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg >= 5L));
    }

    /////////////////////////// VER USO DE SPY EN LA CLASE EXAMENSERVICEIMPLSPYTEST DENTRO DE ESTE PAQUETE /////////////////////////////////


    @Test
    void testNumeroDeInvocaciones() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        examenServiceImp.findExamenPorNombreConPreguntas("Matemáticas");

        verify(preguntaRepository).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, times(1)).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atLeast(1)).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atMost(1)).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L);
    }

}