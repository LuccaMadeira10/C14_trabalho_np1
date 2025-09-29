package br.inatel.cdg.boletim;

import br.inatel.cdg.boletim.repository.AlunoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AlunoRepositoryMockTest {

    @Mock
    private AlunoRepository alunoRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testExistsById_AlunoExiste() {
        // Arrange
        String idAluno = "123";
        when(alunoRepository.existsById(idAluno)).thenReturn(true);
        
        // Act
        boolean existe = alunoRepository.existsById(idAluno);
        
        // Assert
        assertTrue(existe);
        verify(alunoRepository).existsById(idAluno);
    }
    
    @Test
    public void testExistsById_AlunoNaoExiste() {
        // Arrange
        String idAluno = "999";
        when(alunoRepository.existsById(idAluno)).thenReturn(false);
        
        // Act
        boolean existe = alunoRepository.existsById(idAluno);
        
        // Assert
        assertFalse(existe);
        verify(alunoRepository).existsById(idAluno);
    }
    
    @Test
    public void testFindNomeById_AlunoExiste() {
        // Arrange
        String idAluno = "123";
        String nomeEsperado = "João Silva";
        when(alunoRepository.findNomeById(idAluno)).thenReturn(Optional.of(nomeEsperado));
        
        // Act
        Optional<String> nome = alunoRepository.findNomeById(idAluno);
        
        // Assert
        assertTrue(nome.isPresent());
        assertEquals(nomeEsperado, nome.get());
        verify(alunoRepository).findNomeById(idAluno);
    }
    
    @Test
    public void testFindNomeById_AlunoNaoExiste() {
        // Arrange
        String idAluno = "999";
        when(alunoRepository.findNomeById(idAluno)).thenReturn(Optional.empty());
        
        // Act
        Optional<String> nome = alunoRepository.findNomeById(idAluno);
        
        // Assert
        assertFalse(nome.isPresent());
        verify(alunoRepository).findNomeById(idAluno);
    }
}
