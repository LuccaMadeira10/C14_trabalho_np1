package br.inatel.cdg.boletim.repository.impl;

import br.inatel.cdg.boletim.repository.NotasRepository;

import java.util.HashMap;
import java.util.Map;

// implementação simples em memória do repositório de notas
public class NotasRepositoryImpl implements NotasRepository {
    
    // armazena as notas de cada aluno: alunoId -> (avaliação -> nota)
    private final Map<String, Map<String, Double>> notasAlunos = new HashMap<>();
    
    // armazena as notas de recuperação: alunoId -> nota
    private final Map<String, Double> notasRecuperacao = new HashMap<>();
    
    public NotasRepositoryImpl() {
        // dados de exemplo
        inicializarDadosExemplo();
    }
    
    private void inicializarDadosExemplo() {
        // Aluno 123 - aprovado direto (média 76)
        Map<String, Double> notas123 = new HashMap<>();
        notas123.put("P1", 70.0);
        notas123.put("P2", 80.0);
        notasAlunos.put("123", notas123);
        
        // Aluno 456 - recuperação, aprovado na rec (média 52, rec 60, final 56)
        Map<String, Double> notas456 = new HashMap<>();
        notas456.put("P1", 40.0);
        notas456.put("P2", 60.0);
        notasAlunos.put("456", notas456);
        notasRecuperacao.put("456", 60.0);
        
        // Aluno 789 - reprovado direto (média 26)
        Map<String, Double> notas789 = new HashMap<>();
        notas789.put("P1", 20.0);
        notas789.put("P2", 30.0);
        notasAlunos.put("789", notas789);
    }
    
    @Override
    public Map<String, Double> getNotasDoAluno(String id) {
        return notasAlunos.get(id);
    }
    
    @Override
    public Double getRecuperacao(String id) {
        return notasRecuperacao.get(id);
    }
    
    @Override
    public void salvarNota(String id, String avaliacao, Double nota) {
        if ("RECUPERACAO".equals(avaliacao)) {
            notasRecuperacao.put(id, nota);
        } else {
            notasAlunos.computeIfAbsent(id, k -> new HashMap<>()).put(avaliacao, nota);
        }
    }
}