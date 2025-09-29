package br.inatel.cdg.boletim;

// enum para representar as possíveis situações do aluno no sistema
public enum SituacaoAluno {
    APROVADO("APP"),
    RECUPERACAO("REC"), 
    REPROVADO("REP"),
    REPROVADO_DIRETO("REP");
    
    private final String codigo;
    
    SituacaoAluno(String codigo) {
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    @Override
    public String toString() {
        return this.name();
    }
}