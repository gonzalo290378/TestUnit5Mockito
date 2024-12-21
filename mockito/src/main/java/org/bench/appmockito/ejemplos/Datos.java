package org.bench.appmockito.ejemplos;

import org.bench.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(
            new Examen(5L, "Matemáticas"),
            new Examen(6L, "Lenguaje"),
            new Examen(7L, "Historia"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(
            new Examen(null, "Matemáticas"),
            new Examen(null, "Lenguaje"),
            new Examen(null, "Historia"));

    public final static List<String> PREGUNTAS = Arrays.asList(
            "aritmética","integrales", "derivadas", "trigonometría", "geometría", "limite");
    public final static Examen EXAMEN_FISICA = new Examen(8L, "Física");

    public final static List<String> PREGUNTAS_FISICA = Arrays.asList(
            "Gravedad", "Fuerza");

    public final static List<Examen> EMPTY_LIST = Collections.emptyList();
}
