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
}
