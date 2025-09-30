package br.inatel.cdg.boletim;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlunoTest {

    @Test
    public void testConstrutor_ComParametrosValidos() {
        // Arrange & Act
        Aluno aluno = new Aluno("123", "João Silva");
        
        // Assert
        assertEquals("123", aluno.getId());
        assertEquals("João Silva", aluno.getNome());
    }
    
    @Test
    public void testConstrutor_NomeVazio() {
        // Arrange & Act - nome vazio é permitido
        Aluno aluno = new Aluno("123", "");
        
        // Assert
        assertEquals("123", aluno.getId());
        assertEquals("", aluno.getNome());
    }

    //Nessa parte eu validei o construtor do aluno com casos validos e de borda: id nulo e vazio devem falhar, enquanto nome vazio ou nulo sao aceitos, garantindo que os campos ficam corretos

    @Test(expected = IllegalArgumentException.class)
    public void testConstrutor_IdNulo() {
        // Act
        new Aluno(null, "João Silva");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutor_IdVazio() {
        // Act
        new Aluno("", "João Silva");
    }
    
    @Test
    public void testConstrutor_NomeNulo() {
        // Arrange & Act - nome nulo é permitido
        Aluno aluno = new Aluno("123", null);
        
        // Assert
        assertEquals("123", aluno.getId());
        assertNull(aluno.getNome());
    }
}
