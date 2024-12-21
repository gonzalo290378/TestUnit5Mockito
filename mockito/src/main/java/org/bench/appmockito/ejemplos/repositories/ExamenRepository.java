package org.bench.appmockito.ejemplos.repositories;

import org.bench.appmockito.ejemplos.models.Examen;

import java.util.List;

//ESTE METODO SE SIMULAN  CON MOCK (NI SIQUIERA SE EJECUTA, PORQUE NO ES LLAMADO)
public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findAll();
}
