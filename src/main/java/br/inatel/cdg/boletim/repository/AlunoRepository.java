package br.inatel.cdg.boletim.repository;

import java.util.Optional;

// contrato de acesso a dados do aluno
public interface AlunoRepository {
    boolean existsById(String id);              // true se o aluno existe
    Optional<String> findNomeById(String id);   // nome do aluno se existir
}
