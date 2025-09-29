package br.inatel.cdg.boletim.repository.impl;

import br.inatel.cdg.boletim.Aluno;
import br.inatel.cdg.boletim.repository.AlunoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// implementação simples em memória do repositório de alunos
public class AlunoRepositoryImpl implements AlunoRepository {
    
    private final Map<String, Aluno> alunos = new HashMap<>();
    
    public AlunoRepositoryImpl() {
        // dados de exemplo
        adicionarAluno(new Aluno("123", "João Silva"));
        adicionarAluno(new Aluno("456", "Maria Santos"));
        adicionarAluno(new Aluno("789", "Pedro Oliveira"));
    }
    
    public void adicionarAluno(Aluno aluno) {
        alunos.put(aluno.getId(), aluno);
    }
    
    @Override
    public boolean existsById(String id) {
        return alunos.containsKey(id);
    }
    
    @Override
    public Optional<String> findNomeById(String id) {
        Aluno aluno = alunos.get(id);
        return aluno != null ? Optional.of(aluno.getNome()) : Optional.empty();
    }
}