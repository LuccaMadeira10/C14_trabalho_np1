package br.inatel.cdg.boletim;

import br.inatel.cdg.boletim.repository.AlunoRepository;
import br.inatel.cdg.boletim.repository.NotasRepository;
import br.inatel.cdg.boletim.repository.impl.AlunoRepositoryImpl;
import br.inatel.cdg.boletim.repository.impl.NotasRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// teste de integração que valida todo o fluxo do sistema de notas
public class SistemaNotasIntegrationTest {
    
    private BoletimService service;
    private AlunoRepository alunoRepo;
    private NotasRepository notasRepo;
    
    @Before
    public void setUp() {
        alunoRepo = new AlunoRepositoryImpl();
        notasRepo = new NotasRepositoryImpl();
        service = new BoletimService(alunoRepo, notasRepo);
    }
    
    @Test
    public void testFluxoCompleto_AlunoAprovadoDireto() {
        // Cenário: João Silva com notas que resultam em aprovação direta
        String idAluno = "123";
        
        // Verificações
        double media = service.calcularMediaSemRecuperacao(idAluno);
        assertEquals(76.0, media, 0.01); // P1=70 (40%) + P2=80 (60%) = 76
        
        assertTrue("Deveria estar aprovado direto", service.aprovadoDireto(idAluno));
        assertFalse("Não deveria precisar de recuperação", service.precisaRecuperacao(idAluno));
        assertFalse("Não deveria estar reprovado direto", service.reprovadoDireto(idAluno));
        
        assertEquals("APROVADO", service.situacaoParcial(idAluno));
        assertEquals("APROVADO", service.situacaoFinal(idAluno));
        assertEquals(SituacaoAluno.APROVADO, service.getSituacaoFinalEnum(idAluno));
    }
    
    @Test
    public void testFluxoCompleto_AlunoRecuperacaoAprovado() {
        // Cenário: Maria Santos em recuperação mas aprovada na final
        String idAluno = "456";
        
        // Verificações da situação parcial
        double media = service.calcularMediaSemRecuperacao(idAluno);
        assertEquals(52.0, media, 0.01); // P1=40 (40%) + P2=60 (60%) = 52
        
        assertFalse("Não deveria estar aprovado direto", service.aprovadoDireto(idAluno));
        assertTrue("Deveria precisar de recuperação", service.precisaRecuperacao(idAluno));
        assertFalse("Não deveria estar reprovado direto", service.reprovadoDireto(idAluno));
        
        assertEquals("RECUPERACAO", service.situacaoParcial(idAluno));
        
        // Verificações pós-recuperação
        double mediaFinal = service.calcularMediaPosRecuperacao(idAluno);
        assertEquals(56.0, mediaFinal, 0.01); // (52 + 60) / 2 = 56
        
        assertEquals("APROVADO", service.situacaoFinal(idAluno));
        assertEquals(SituacaoAluno.APROVADO, service.getSituacaoFinalEnum(idAluno));
    }
    
    @Test
    public void testFluxoCompleto_AlunoReprovadoDireto() {
        // Cenário: Pedro Oliveira reprovado direto
        String idAluno = "789";
        
        // Verificações
        double media = service.calcularMediaSemRecuperacao(idAluno);
        assertEquals(26.0, media, 0.01); // P1=20 (40%) + P2=30 (60%) = 26
        
        assertFalse("Não deveria estar aprovado direto", service.aprovadoDireto(idAluno));
        assertFalse("Não deveria precisar de recuperação", service.precisaRecuperacao(idAluno));
        assertTrue("Deveria estar reprovado direto", service.reprovadoDireto(idAluno));
        
        assertEquals("REPROVADO_DIRETO", service.situacaoParcial(idAluno));
        assertEquals("REPROVADO", service.situacaoFinal(idAluno));
        assertEquals(SituacaoAluno.REPROVADO, service.getSituacaoFinalEnum(idAluno));
    }
    
    @Test
    public void testSalvarNota_NovoAluno() {
        // Adiciona um novo aluno
        Aluno novoAluno = new Aluno("999", "Ana Costa");
        ((AlunoRepositoryImpl) alunoRepo).adicionarAluno(novoAluno);
        
        // Salva as notas
        service.salvarNota("999", Avaliacao.P1, 45.0);
        service.salvarNota("999", Avaliacao.P2, 55.0);
        
        // Verifica situação
        double media = service.calcularMediaSemRecuperacao("999");
        assertEquals(51.0, media, 0.01); // P1=45 (40%) + P2=55 (60%) = 51
        
        assertEquals("RECUPERACAO", service.situacaoParcial("999"));
        
        // Salva nota de recuperação
        service.salvarNota("999", Avaliacao.RECUPERACAO, 40.0);
        
        // Verifica situação final
        double mediaFinal = service.calcularMediaPosRecuperacao("999");
        assertEquals(45.5, mediaFinal, 0.01); // (51 + 40) / 2 = 45.5
        
        assertEquals("REPROVADO", service.situacaoFinal("999")); // 45.5 < 50
    }
    
    @Test
    public void testCenarioLimite_Media60Exata() {
        // Testa o limite exato de 60 para aprovação
        Aluno aluno = new Aluno("600", "Limite 60");
        ((AlunoRepositoryImpl) alunoRepo).adicionarAluno(aluno);
        
        // Configura notas para média exata de 60: P1=50, P2=65 -> 50*0.4 + 65*0.6 = 59
        service.salvarNota("600", Avaliacao.P1, 50.0);
        service.salvarNota("600", Avaliacao.P2, 66.67); // 50*0.4 + 66.67*0.6 ≈ 60
        
        double media = service.calcularMediaSemRecuperacao("600");
        assertTrue("Média deveria ser >= 60", media >= 60.0);
        assertEquals("APROVADO", service.situacaoParcial("600"));
    }
    
    @Test
    public void testCenarioLimite_Media50ExataRecuperacao() {
        // Testa o limite exato de 50 para aprovação na recuperação
        Aluno aluno = new Aluno("500", "Limite Rec 50");
        ((AlunoRepositoryImpl) alunoRepo).adicionarAluno(aluno);
        
        service.salvarNota("500", Avaliacao.P1, 40.0);
        service.salvarNota("500", Avaliacao.P2, 50.0); // média = 46
        service.salvarNota("500", Avaliacao.RECUPERACAO, 54.0); // (46+54)/2 = 50
        
        double mediaFinal = service.calcularMediaPosRecuperacao("500");
        assertTrue("Média final deveria ser >= 50", mediaFinal >= 50.0);
        assertEquals("APROVADO", service.situacaoFinal("500"));
    }
}