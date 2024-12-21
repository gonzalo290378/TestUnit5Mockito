package org.bench.appmockito.ejemplos.repositories;

import java.util.List;

//ESTE METODO SE SIMULAN  CON MOCK (NO NOS INTERESA SABER COMO VIENEN LOS DATOS, YA SE POR MEDIO DE DB O API EXTERNA)
public interface PreguntaRepository {
    List<String> findPreguntasPorExamenId(Long id);
    void guardarVarias(List<String> preguntas);
}
