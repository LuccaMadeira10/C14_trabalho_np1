package br.inatel.cdg.boletim;

import br.inatel.cdg.boletim.exceptions.AlunoNaoEncontradoException;
import br.inatel.cdg.boletim.exceptions.NotaInvalidaException;
import br.inatel.cdg.boletim.repository.AlunoRepository;
import br.inatel.cdg.boletim.repository.NotasRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BoletimServiceTest {

    @Mock
    private AlunoRepository alunoRepo;
    
    @Mock
    private NotasRepository notasRepo;
    
    private BoletimService service;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new BoletimService(alunoRepo, notasRepo);
    }
    
    // TESTES Gustavo 

    @Test
    public void testCalcularMediaSemRecuperacao_MediaCorreta() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 70.0);
        notas.put("P2", 80.0);
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        double media = service.calcularMediaSemRecuperacao(idAluno);
        
        // Assert - P1 peso 0.4 + P2 peso 0.6 = 70*0.4 + 80*0.6 = 28 + 48 = 76.0
        assertEquals(76.0, media, 0.01);
    }
    
    @Test(expected = AlunoNaoEncontradoException.class)
    public void testCalcularMediaSemRecuperacao_AlunoNaoEncontrado() {
        // Arrange
        String idAluno = "999";
        when(alunoRepo.existsById(idAluno)).thenReturn(false);
        
        // Act
        service.calcularMediaSemRecuperacao(idAluno);
    }
    
    @Test(expected = NotaInvalidaException.class)
    public void testCalcularMediaSemRecuperacao_NotaInvalida() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 150.0); // nota inválida > 100
        notas.put("P2", 80.0);
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        service.calcularMediaSemRecuperacao(idAluno);
    }
    
    @Test
    public void testSituacaoParcial_Aprovado() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 70.0);
        notas.put("P2", 80.0); // média = 76.0 >= 60
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        String situacao = service.situacaoParcial(idAluno);
        
        // Assert
        assertEquals("APROVADO", situacao);
    }
    
    @Test
    public void testSituacaoParcial_Recuperacao() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 60.0); // média = 52.0 (30 <= 52 < 60)
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        String situacao = service.situacaoParcial(idAluno);
        
        // Assert
        assertEquals("RECUPERACAO", situacao);
    }
    
    @Test
    public void testSituacaoParcial_ReprovadoDireto() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 20.0);
        notas.put("P2", 30.0); // média = 26.0 < 30
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        String situacao = service.situacaoParcial(idAluno);
        
        // Assert
        assertEquals("REPROVADO_DIRETO", situacao);
    }
    
    @Test
    public void testSituacaoFinal_AprovadoPosRecuperacao() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 60.0); // média parcial = 52.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        when(notasRepo.getRecuperacao(idAluno)).thenReturn(60.0); // média final = (52+60)/2 = 56.0 >= 50
        
        // Act
        String situacao = service.situacaoFinal(idAluno);
        
        // Assert
        assertEquals("APROVADO", situacao);
    }
    
    @Test
    public void testSituacaoFinal_ReprovadoPosRecuperacao() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 60.0); // média parcial = 52.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        when(notasRepo.getRecuperacao(idAluno)).thenReturn(30.0); // média final = (52+30)/2 = 41.0 < 50
        
        // Act
        String situacao = service.situacaoFinal(idAluno);
        
        // Assert
        assertEquals("REPROVADO", situacao);
    }
    
    @Test
    public void testSituacaoFinal_JaAprovado() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 70.0);
        notas.put("P2", 80.0); // média = 76.0 >= 60
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        String situacao = service.situacaoFinal(idAluno);
        
        // Assert
        assertEquals("APROVADO", situacao);
    }
    
    @Test
    public void testSituacaoFinal_ReprovadoDireto() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 20.0);
        notas.put("P2", 30.0); // média = 26.0 < 30
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        String situacao = service.situacaoFinal(idAluno);
        
        // Assert
        assertEquals("REPROVADO", situacao);
    }
    
    @Test
    public void testGetSituacaoFinalEnum_Reprovado() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 20.0);
        notas.put("P2", 25.0); // média = 23.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        SituacaoAluno situacao = service.getSituacaoFinalEnum(idAluno);
        
        // Assert
        assertEquals(SituacaoAluno.REPROVADO, situacao);
    }

 
    // Testes parte Lucca, esses testes checam salvar notas, configurar pesos, medias e se o aluno precisa de recuperacao ou esta aprovado ou reprovado direto
  
    @Test
    public void testConfigurarPesos() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 60.0);
        notas.put("P2", 80.0);
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act - configurar pesos iguais
        service.configurarPesos(0.5, 0.5);
        double media = service.calcularMediaSemRecuperacao(idAluno);
        
        // Assert - P1=60 (50%) + P2=80 (50%) = 70.0
        assertEquals(70.0, media, 0.01);
    }
    
    @Test
    public void testSalvarNota_ComSucesso() {
        // Arrange
        String idAluno = "123";
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        
        // Act
        service.salvarNota(idAluno, Avaliacao.P1, 85.0);
        
        // Assert
        verify(alunoRepo).existsById(idAluno);
        verify(notasRepo).salvarNota(idAluno, "P1", 85.0);
    }
    
    @Test(expected = AlunoNaoEncontradoException.class)
    public void testSalvarNota_AlunoNaoEncontrado() {
        // Arrange
        String idAluno = "999";
        when(alunoRepo.existsById(idAluno)).thenReturn(false);
        
        // Act
        service.salvarNota(idAluno, Avaliacao.P1, 85.0);
    }
    
    @Test(expected = NotaInvalidaException.class)
    public void testSalvarNota_NotaInvalida() {
        // Arrange
        String idAluno = "123";
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        
        // Act
        service.salvarNota(idAluno, Avaliacao.P1, 150.0); // nota inválida
    }
    
    @Test
    public void testPrecisaRecuperacao_True() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 50.0); // média = 46.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        boolean precisa = service.precisaRecuperacao(idAluno);
        
        // Assert
        assertTrue(precisa);
    }
    
    @Test
    public void testPrecisaRecuperacao_False() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 70.0);
        notas.put("P2", 80.0); // média = 76.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        boolean precisa = service.precisaRecuperacao(idAluno);
        
        // Assert
        assertFalse(precisa);
    }
    
    @Test
    public void testAprovadoDireto_True() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 70.0);
        notas.put("P2", 80.0); // média = 76.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        boolean aprovado = service.aprovadoDireto(idAluno);
        
        // Assert
        assertTrue(aprovado);
    }
    
    @Test
    public void testAprovadoDireto_False() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 50.0); // média = 46.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        boolean aprovado = service.aprovadoDireto(idAluno);
        
        // Assert
        assertFalse(aprovado);
    }
    
    @Test
    public void testReprovadoDireto_True() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 20.0);
        notas.put("P2", 25.0); // média = 23.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        boolean reprovado = service.reprovadoDireto(idAluno);
        
        // Assert
        assertTrue(reprovado);
    }
    
    @Test
    public void testReprovadoDireto_False() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 50.0); // média = 46.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        boolean reprovado = service.reprovadoDireto(idAluno);
        
        // Assert
        assertFalse(reprovado);
    }
    
    @Test
    public void testGetSituacaoFinalEnum_Aprovado() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 70.0);
        notas.put("P2", 80.0); // média = 76.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        SituacaoAluno situacao = service.getSituacaoFinalEnum(idAluno);
        
        // Assert
        assertEquals(SituacaoAluno.APROVADO, situacao);
    }

    
    // aqui foi feito basicamente os teste em que, olham medias com e sem recuperacao, salvar nota em p2 e recuperacao, casos de limite de nota e se a situacao final bate certo

    @Test
    public void testCalcularMediaPosRecuperacao_SemRecuperacao() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 60.0);
        notas.put("P2", 70.0);
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        when(notasRepo.getRecuperacao(idAluno)).thenReturn(null);
        
        // Act
        double mediaFinal = service.calcularMediaPosRecuperacao(idAluno);
        
        // Assert
        assertEquals(66.0, mediaFinal, 0.01); // P1=60 (40%) + P2=70 (60%) = 66.0
    }
    
    @Test
    public void testCalcularMediaPosRecuperacao_ComRecuperacao() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 50.0); // média = 46.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        when(notasRepo.getRecuperacao(idAluno)).thenReturn(70.0);
        
        // Act
        double mediaFinal = service.calcularMediaPosRecuperacao(idAluno);
        
        // Assert
        assertEquals(58.0, mediaFinal, 0.01); // (46 + 70) / 2 = 58.0
    }
    
    @Test
    public void testSituacaoFinal_RecuperacaoReprovado() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 35.0);
        notas.put("P2", 45.0); // média = 41.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        when(notasRepo.getRecuperacao(idAluno)).thenReturn(30.0); // média final = (41+30)/2 = 35.5 < 50
        
        // Act
        String situacao = service.situacaoFinal(idAluno);
        
        // Assert
        assertEquals("REPROVADO", situacao);
    }
    
    @Test
    public void testSalvarNota_P2() {
        // Arrange
        String idAluno = "456";
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        
        // Act
        service.salvarNota(idAluno, Avaliacao.P2, 95.0);
        
        // Assert
        verify(alunoRepo).existsById(idAluno);
        verify(notasRepo).salvarNota(idAluno, "P2", 95.0);
    }
    
    @Test
    public void testSalvarNota_Recuperacao() {
        // Arrange
        String idAluno = "789";
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        
        // Act
        service.salvarNota(idAluno, Avaliacao.RECUPERACAO, 65.0);
        
        // Assert
        verify(alunoRepo).existsById(idAluno);
        verify(notasRepo).salvarNota(idAluno, "RECUPERACAO", 65.0);
    }
    
    @Test
    public void testCalcularMediaSemRecuperacao_NotasExtremas() {
        // Arrange
        String idAluno = "111";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 0.0);
        notas.put("P2", 100.0);
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        double media = service.calcularMediaSemRecuperacao(idAluno);
        
        // Assert
        assertEquals(60.0, media, 0.01); // P1=0 (40%) + P2=100 (60%) = 60.0
    }
    
    @Test
    public void testSituacaoParcial_LimiteInferior30() {
        // Arrange
        String idAluno = "222";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 0.0);
        notas.put("P2", 50.0); // média = 30.0 (exatamente no limite)
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        String situacao = service.situacaoParcial(idAluno);
        
        // Assert
        assertEquals("RECUPERACAO", situacao);
    }
    
    @Test
    public void testSituacaoParcial_LimiteSuperior60() {
        // Arrange
        String idAluno = "333";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 40.0);
        notas.put("P2", 73.33); // média = 60.0 (exatamente no limite)
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act
        String situacao = service.situacaoParcial(idAluno);
        
        // Assert
        assertEquals("APROVADO", situacao);
    }
    
    @Test
    public void testConfiguracao_PesosCustomizados() {
        // Arrange
        String idAluno = "444";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 100.0);
        notas.put("P2", 0.0);
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        
        // Act - configurar pesos invertidos
        service.configurarPesos(0.8, 0.2);
        double media = service.calcularMediaSemRecuperacao(idAluno);
        
        // Assert - P1=100 (80%) + P2=0 (20%) = 80.0
        assertEquals(80.0, media, 0.01);
    }
    
    @Test
    public void testGetSituacaoFinalEnum_RecuperacaoAprovado() {
        // Arrange
        String idAluno = "555";
        Map<String, Double> notas = new HashMap<>();
        notas.put("P1", 30.0);
        notas.put("P2", 45.0); // média = 39.0
        
        when(alunoRepo.existsById(idAluno)).thenReturn(true);
        when(notasRepo.getNotasDoAluno(idAluno)).thenReturn(notas);
        when(notasRepo.getRecuperacao(idAluno)).thenReturn(80.0); // média final = (39+80)/2 = 59.5 >= 50
        
        // Act
        SituacaoAluno situacao = service.getSituacaoFinalEnum(idAluno);
        
        // Assert
        assertEquals(SituacaoAluno.APROVADO, situacao);
    }
}
