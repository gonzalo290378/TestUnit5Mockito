package org.bench.appmockito.ejemplos.repositories;



import org.bench.appmockito.ejemplos.Datos;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntaRepositoryImpl implements PreguntaRepository {
    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("Implementacion virtual de metodo findPreguntasPorExamenId()");
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("Implementacion virtual de metodo guardarVarias()");
    }
}
