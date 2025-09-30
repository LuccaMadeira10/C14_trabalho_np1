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
        double mediaParcial = 45.0;
        Double notaRecuperacao = 60.0;
        double mediaFinal = politica.aplicar(mediaParcial, notaRecuperacao);
        assertEquals(52.5, mediaFinal, 0.01); // (45 + 60) / 2 = 52.5
    }
    
    @Test
    public void testAplicar_SemNotaRecuperacao() {
        double mediaParcial = 45.0;
        Double notaRecuperacao = null;
        double mediaFinal = politica.aplicar(mediaParcial, notaRecuperacao);
        assertEquals(45.0, mediaFinal, 0.01); // mantem a media parcial
    }

    //foi feito testes para casos extremos como nota de recuperacao zero e nota de recuperacao 100

    @Test
    public void testAplicar_NotaRecuperacaoZero() {
        double mediaParcial = 50.0;
        Double notaRecuperacao = 0.0;
        double mediaFinal = politica.aplicar(mediaParcial, notaRecuperacao);
        assertEquals(25.0, mediaFinal, 0.01); // (50 + 0) / 2 = 25.0
    }
    
    @Test
    public void testAplicar_NotaRecuperacao100() {
        double mediaParcial = 30.0;
        Double notaRecuperacao = 100.0;
        double mediaFinal = politica.aplicar(mediaParcial, notaRecuperacao);
        assertEquals(65.0, mediaFinal, 0.01); // (30 + 100) / 2 = 65.0
    }
}
