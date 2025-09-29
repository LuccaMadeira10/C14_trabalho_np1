package br.inatel.cdg.boletim;

import br.inatel.cdg.boletim.repository.NotasRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NotasRepositoryMockTest {

    @Mock
    private NotasRepository notasRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetNotasDoAluno_ComNotas() {
        // Arrange
        String idAluno = "123";
        Map<String, Double> notasEsperadas = new HashMap<>();
        notasEsperadas.put("P1", 75.0);
        notasEsperadas.put("P2", 80.0);
        
        when(notasRepository.getNotasDoAluno(idAluno)).thenReturn(notasEsperadas);
        
        // Act
        Map<String, Double> notas = notasRepository.getNotasDoAluno(idAluno);
        
        // Assert
        assertNotNull(notas);
        assertEquals(2, notas.size());
        assertEquals(75.0, notas.get("P1"), 0.01);
        assertEquals(80.0, notas.get("P2"), 0.01);
        verify(notasRepository).getNotasDoAluno(idAluno);
    }
    
    @Test
    public void testGetNotasDoAluno_SemNotas() {
        // Arrange
        String idAluno = "999";
        when(notasRepository.getNotasDoAluno(idAluno)).thenReturn(null);
        
        // Act
        Map<String, Double> notas = notasRepository.getNotasDoAluno(idAluno);
        
        // Assert
        assertNull(notas);
        verify(notasRepository).getNotasDoAluno(idAluno);
    }
    
    @Test
    public void testGetRecuperacao_ComNotaRecuperacao() {
        // Arrange
        String idAluno = "123";
        Double notaRecuperacao = 70.0;
        when(notasRepository.getRecuperacao(idAluno)).thenReturn(notaRecuperacao);
        
        // Act
        Double nota = notasRepository.getRecuperacao(idAluno);
        
        // Assert
        assertNotNull(nota);
        assertEquals(70.0, nota, 0.01);
        verify(notasRepository).getRecuperacao(idAluno);
    }
    
    @Test
    public void testGetRecuperacao_SemNotaRecuperacao() {
        // Arrange
        String idAluno = "123";
        when(notasRepository.getRecuperacao(idAluno)).thenReturn(null);
        
        // Act
        Double nota = notasRepository.getRecuperacao(idAluno);
        
        // Assert
        assertNull(nota);
        verify(notasRepository).getRecuperacao(idAluno);
    }
    
    @Test
    public void testSalvarNota() {
        // Arrange
        String idAluno = "123";
        String avaliacao = "P1";
        Double nota = 85.0;
        
        // Act
        notasRepository.salvarNota(idAluno, avaliacao, nota);
        
        // Assert
        verify(notasRepository).salvarNota(idAluno, avaliacao, nota);
    }
}
