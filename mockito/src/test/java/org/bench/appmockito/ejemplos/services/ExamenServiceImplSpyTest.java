package org.bench.appmockito.ejemplos.services;

import org.bench.appmockito.ejemplos.models.Examen;
import org.bench.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.bench.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//EXTENDWITH: ANOTACION IMPORTANTE HABILITAR MOCKITO EN JUNIT 5 Y LA INYECCIÓN AUTOMÁTICA DE MOCKS: @MOCK, @SPY,
//@INJECTMOCKS, ENTRE OTROS.
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplSpyTest {

    //LOS SPY SIEMPRE TIENEN QUE SER IMPLEMENTACIONES Y NO INTERFACES
    @Spy
    ExamenRepositoryImpl examenRepository;
    @Spy
    PreguntaRepositoryImpl preguntaRepository;
    @InjectMocks
    ExamenServiceImpl examenService;

    //LA ANOTACIÓN @SPY ES ÚTIL PARA PRUEBAS UNITARIAS CUANDO NECESITAS CONSERVAR EL COMPORTAMIENTO REAL DEL OBJETO,
    //PERO TAMBIÉN QUIERES LA FLEXIBILIDAD DE SIMULAR CIERTOS MÉTODOS O VERIFICAR INTERACCIONES. ES UNA HERRAMIENTA
    //PODEROSA PARA MANTENER LA FIDELIDAD DE LAS PRUEBAS MIENTRAS SE CONTROLA EL ENTORNO DE PRUEBA.
    @Test
    void testSpy() {
        List<String> preguntas = Arrays.asList("aritmetica");

        // DORETURN SE UTILIZA PARA DEFINIR EL COMPORTAMIENTO DE UN MÉTODO SIMULADO DE UNA MANERA QUE EVITA LA INVOCACIÓN
        // DEL MÉTODO REAL. ESTO ES PARTICULARMENTE ÚTIL CUANDO ESTÁS TRATANDO CON MÉTODOS QUE TIENEN EFECTOS SECUNDARIOS,
        // COMO MÉTODOS FINAL O STATIC, QUE NO PUEDEN SER ESPIADOS DIRECTAMENTE, O MÉTODOS QUE SON COMPLEJOS O LENTOS Y
        // NO QUIERES EJECUTARLOS DURANTE LAS PRUEBAS. SI LLEGO A UTILIZAR WHEN, AL SER PREGUNTAREPOSITORY UN SPY, LLAMA
        // AL COMPORTAMIENTO REAL DEL OBJETO, LO CUAL NO DESEAMOS.
        // POR LO GENERAL, CUANDO TRABAJAS CON @SPY, ES RECOMENDABLE UTILIZAR DORETURN PARA EVITAR CUALQUIER
        // COMPORTAMIENTO NO DESEADO AL ESPIAR MÉTODOS.
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        //SI DESCOMENTO ESTA LINEA CUANDO SE VERIFIQUE EL verify(examenRepository).findAll() SE HARA UNA SIMULACION
        //Y NO UNA LLAMADA AL METODO REAL
        //doReturn(Datos.EXAMENES).when(examenRepository).findAll();
        Examen examen = examenService.findExamenPorNombreConPreguntas("Matemáticas");

        assertEquals(5L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());

        //COMO EXAMENREPOSITORY ES UN SPY ES UNA LLAMADA REAL
        verify(examenRepository).findAll();

        //COMO EXAMENREPOSITORY ES UN SPY PERO COMO LO ESTAMOS SIMULANDO EN EL DORETURN NO SE HACE UNA LLAMADA REAL
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }
}