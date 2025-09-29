package br.inatel.cdg.boletim.repository;

import java.util.Map;

// contrato de acesso a notas do aluno
public interface NotasRepository {
    Map<String, Double> getNotasDoAluno(String id);      // chaves P1 P2 TRABALHO
    Double getRecuperacao(String id);                    // nota de recuperacao se houver
    void salvarNota(String id, String avaliacao, Double nota); // persistencia simples
}
