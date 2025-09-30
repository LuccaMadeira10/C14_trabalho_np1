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
        double valor = 75.56;
        double resultado = politica.paraUmaCasa(valor);
        assertEquals(75.6, resultado, 0.01);
    }
    
    @Test
    public void testParaUmaCasa_ArredondaParaBaixo() {
        double valor = 75.54;
        double resultado = politica.paraUmaCasa(valor);
        assertEquals(75.5, resultado, 0.01);
    }
    
    @Test
    public void testParaUmaCasa_HalfUp() {
        double valor = 75.65;
        double resultado = politica.paraUmaCasa(valor);
        assertEquals(75.7, resultado, 0.01); // HALF_UP arredonda 0.65 para cima
    }

    //Nessa parte eu fiz testes para validar que o arredondamento funciona certo em duas casas decimais, em zero casas e tambem quando o numero ja e inteiro

    @Test
    public void testParaCasas_DuasCasas() {
        double valor = 75.555;
        double resultado = politica.paraCasas(valor, 2);
        assertEquals(75.56, resultado, 0.01);
    }
    
    @Test
    public void testParaCasas_ZeroCasas() {
        double valor = 75.6;
        double resultado = politica.paraCasas(valor, 0);
        assertEquals(76.0, resultado, 0.01);
    }
    
    @Test
    public void testParaUmaCasa_NumeroInteiro() {
        double valor = 75.0;
        double resultado = politica.paraUmaCasa(valor);
        assertEquals(75.0, resultado, 0.01);
    }
}
