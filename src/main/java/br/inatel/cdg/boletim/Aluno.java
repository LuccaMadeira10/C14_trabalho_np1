package br.inatel.cdg.boletim;

// classe simples de aluno, id e nome
public class Aluno {

    private final String id;   // identificador unico
    private final String nome; // nome do aluno

    // construtor basico, valida id nao vazio
    public Aluno(String id, String nome) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id obrigatorio");
        }
        this.id = id;
        this.nome = nome;
    }

    // getters simples
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
