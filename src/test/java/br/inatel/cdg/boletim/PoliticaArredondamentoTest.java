package br.inatel.cdg.boletim;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PoliticaArredondamentoTest {
    
    private PoliticaArredondamento politica;
    
    @Before
    public void setUp() {
        politica = new PoliticaArredondamento();
    }
    
    @Test
    public void testParaUmaCasa_ArredondaParaCima() {
        // Arrange
        double valor = 75.56;
        
        // Act
        double resultado = politica.paraUmaCasa(valor);
        
        // Assert
        assertEquals(75.6, resultado, 0.01);
    }
    
    @Test
    public void testParaUmaCasa_ArredondaParaBaixo() {
        // Arrange
        double valor = 75.54;
        
        // Act
        double resultado = politica.paraUmaCasa(valor);
        
        // Assert
        assertEquals(75.5, resultado, 0.01);
    }
    
    @Test
    public void testParaUmaCasa_HalfUp() {
        // Arrange
        double valor = 75.65;
        
        // Act
        double resultado = politica.paraUmaCasa(valor);
        
        // Assert
        assertEquals(75.7, resultado, 0.01); // HALF_UP arredonda 0.65 para cima
    }
}