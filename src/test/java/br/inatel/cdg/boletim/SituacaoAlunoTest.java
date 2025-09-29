package br.inatel.cdg.boletim;

import org.junit.Test;

import static org.junit.Assert.*;

public class SituacaoAlunoTest {
    
    @Test
    public void testAprovado_Codigo() {
        // Act & Assert
        assertEquals("APP", SituacaoAluno.APROVADO.getCodigo());
    }
    
    @Test
    public void testToString_Aprovado() {
        // Act & Assert
        assertEquals("APROVADO", SituacaoAluno.APROVADO.toString());
    }
}