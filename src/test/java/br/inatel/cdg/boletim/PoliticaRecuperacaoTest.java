package br.inatel.cdg.boletim;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PoliticaRecuperacaoTest {
    
    private PoliticaRecuperacao politica;
    
    @Before
    public void setUp() {
        politica = new PoliticaRecuperacao();
    }
    
    @Test
    public void testAplicar_ComNotaRecuperacao() {
        // Arrange
        double mediaParcial = 45.0;
        Double notaRecuperacao = 60.0;
        
        // Act
        double mediaFinal = politica.aplicar(mediaParcial, notaRecuperacao);
        
        // Assert
        assertEquals(52.5, mediaFinal, 0.01); // (45 + 60) / 2 = 52.5
    }
    
    @Test
    public void testAplicar_SemNotaRecuperacao() {
        // Arrange
        double mediaParcial = 45.0;
        Double notaRecuperacao = null;
        
        // Act
        double mediaFinal = politica.aplicar(mediaParcial, notaRecuperacao);
        
        // Assert
        assertEquals(45.0, mediaFinal, 0.01); // mantém a média parcial
    }
}