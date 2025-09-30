package br.inatel.cdg.boletim;

import org.junit.Test;

import static org.junit.Assert.*;

public class SituacaoAlunoTest {
    
    //Teste do gustavo 

    @Test
    public void testAprovado_Codigo() {
        assertEquals("APP", SituacaoAluno.APROVADO.getCodigo());
    }
    
    @Test
    public void testToString_Aprovado() {
        assertEquals("APROVADO", SituacaoAluno.APROVADO.toString());
    }

    //Nessa parte eu fiz testes para garantir que cada situacao de aluno retorna o codigo correto e a representacao em texto certa, cobrindo aprovado, recuperacao, reprovado e reprovado direto

    @Test
    public void testRecuperacao_Codigo() {
        assertEquals("REC", SituacaoAluno.RECUPERACAO.getCodigo());
    }
    
    @Test
    public void testReprovado_Codigo() {
        assertEquals("REP", SituacaoAluno.REPROVADO.getCodigo());
    }
    
    @Test
    public void testReprovadoDireto_Codigo() {
        assertEquals("REP", SituacaoAluno.REPROVADO_DIRETO.getCodigo());
    }
    
    @Test
    public void testToString_Recuperacao() {
        assertEquals("RECUPERACAO", SituacaoAluno.RECUPERACAO.toString());
    }
    
    @Test
    public void testToString_Reprovado() {
        assertEquals("REPROVADO", SituacaoAluno.REPROVADO.toString());
    }
    
    @Test
    public void testToString_ReprovadoDireto() {
        assertEquals("REPROVADO_DIRETO", SituacaoAluno.REPROVADO_DIRETO.toString());
    }
}
