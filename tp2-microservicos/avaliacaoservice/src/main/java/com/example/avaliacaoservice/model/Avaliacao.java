package com.example.avaliacaoservice.model;

public class Avaliacao {

    private Long id;
    private Long filmeId;
    private int nota;
    private String comentario;

    public Avaliacao() {}

    public Avaliacao(Long id, Long filmeId, int nota, String comentario) {
        this.id = id;
        this.filmeId = filmeId;
        this.nota = nota;
        this.comentario = comentario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFilmeId() { return filmeId; }
    public void setFilmeId(Long filmeId) { this.filmeId = filmeId; }

    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}