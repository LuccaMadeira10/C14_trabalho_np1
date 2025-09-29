package br.inatel.cdg.boletim;

import br.inatel.cdg.boletim.repository.AlunoRepository;
import br.inatel.cdg.boletim.repository.NotasRepository;
import br.inatel.cdg.boletim.repository.impl.AlunoRepositoryImpl;
import br.inatel.cdg.boletim.repository.impl.NotasRepositoryImpl;

// classe principal para demonstrar o sistema de notas do Inatel
public class SistemaNotasInatel {
    
    public static void main(String[] args) {
        // inicializa repositórios com dados de exemplo
        AlunoRepository alunoRepo = new AlunoRepositoryImpl();
        NotasRepository notasRepo = new NotasRepositoryImpl();
        
        // cria o serviço de boletim
        BoletimService service = new BoletimService(alunoRepo, notasRepo);
        
        System.out.println("=== SISTEMA DE NOTAS INATEL ===");
        System.out.println("Regras:");
        System.out.println("- Nota >= 60: APROVADO");
        System.out.println("- 30 <= Nota < 60: RECUPERAÇÃO");
        System.out.println("- Nota < 30: REPROVADO DIRETO");
        System.out.println("- Para passar na recuperação: média final >= 50");
        System.out.println();
        
        // demonstra diferentes cenários
        demonstrarCenario(service, alunoRepo, "123", "João Silva");
        demonstrarCenario(service, alunoRepo, "456", "Maria Santos");  
        demonstrarCenario(service, alunoRepo, "789", "Pedro Oliveira");
    }
    
    private static void demonstrarCenario(BoletimService service, AlunoRepository alunoRepo, String id, String nome) {
        System.out.println("--- " + nome + " (ID: " + id + ") ---");
        
        try {
            double media = service.calcularMediaSemRecuperacao(id);
            String situacaoParcial = service.situacaoParcial(id);
            String situacaoFinal = service.situacaoFinal(id);
            
            System.out.printf("Média sem recuperação: %.1f%n", media);
            System.out.println("Situação parcial: " + situacaoParcial);
            
            if ("RECUPERACAO".equals(situacaoParcial)) {
                double mediaFinal = service.calcularMediaPosRecuperacao(id);
                System.out.printf("Média pós-recuperação: %.1f%n", mediaFinal);
            }
            
            System.out.println("Situação final: " + situacaoFinal);
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        System.out.println();
    }
}