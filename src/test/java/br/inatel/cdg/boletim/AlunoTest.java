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
}