package org.bench.appmockito.ejemplos.repositories;

import org.bench.appmockito.ejemplos.Datos;
import org.bench.appmockito.ejemplos.models.Examen;
import java.util.List;

public class ExamenRepositoryImpl implements ExamenRepository {
    @Override
    public Examen guardar(Examen examen) {
        System.out.println("Implementacion virtual de metodo guardar()");
        return Datos.EXAMEN_FISICA;
    }

    @Override
    public List<Examen> findAll() {
        System.out.println("Implementacion virtual de metodo findAll()");
        return Datos.EXAMENES;
    }
}
