package br.inatel.cdg.boletim;

import br.inatel.cdg.boletim.exceptions.NotaInvalidaException;
import br.inatel.cdg.boletim.exceptions.AlunoNaoEncontradoException;
import br.inatel.cdg.boletim.repository.AlunoRepository;
import br.inatel.cdg.boletim.repository.NotasRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class BoletimService {

    private final AlunoRepository alunoRepo;
    private final NotasRepository notasRepo;

    private double pesoP1 = 0.4; // quarenta por cento
    private double pesoP2 = 0.6; // sessenta por cento

    // politica de recuperacao em classe separada
    private final PoliticaRecuperacao politicaRec = new PoliticaRecuperacao();

    // construtor recebe os repositorios de aluno e notas
    public BoletimService(AlunoRepository alunoRepo, NotasRepository notasRepo) {
        this.alunoRepo = alunoRepo;
        this.notasRepo = notasRepo;
    }

    // valida se a nota esta entre 0 e 100
    private void validarNota(double n) {
        if (n < 0.0 || n > 100.0) {
            throw new NotaInvalidaException("nota fora do intervalo 0 a 100");
        }
    }

    // arredonda valor para uma casa decimal
    private double arredondar1(double valor) {
        return new BigDecimal(valor).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    // calcula a media ponderada usando p1 e p2 sem considerar recuperacao
    public double calcularMediaSemRecuperacao(String idAluno) {
        if (!alunoRepo.existsById(idAluno)) {
            throw new AlunoNaoEncontradoException("aluno nao encontrado");
        }
        Map<String, Double> notas = notasRepo.getNotasDoAluno(idAluno);
        if (notas == null || !notas.containsKey("P1") || !notas.containsKey("P2")) {
            throw new IllegalStateException("notas P1 e P2 sao obrigatorias");
        }
        double p1 = notas.get("P1");
        double p2 = notas.get("P2");
        validarNota(p1);
        validarNota(p2);

        double media = p1 * pesoP1 + p2 * pesoP2; // combina pesos de p1 e p2
        return arredondar1(media); // retorna com uma casa decimal
    }

    // define a situacao parcial do aluno
    // aprovado se media >= 60
    // recuperacao se 30 <= media < 60
    // reprovado direto se media < 30
    public String situacaoParcial(String idAluno) {
        double m = calcularMediaSemRecuperacao(idAluno);
        if (m >= 60.0) return "APROVADO";
        if (m >= 30.0) return "RECUPERACAO";
        return "REPROVADO_DIRETO";
    }

    // calcula a media final apos considerar recuperacao
    // usa media simples entre media parcial e nota de recuperacao
    public double calcularMediaPosRecuperacao(String idAluno) {
        double mediaParcial = calcularMediaSemRecuperacao(idAluno);
        Double notaRec = notasRepo.getRecuperacao(idAluno); // pode ser nula
        if (notaRec != null) validarNota(notaRec);
        double mf = politicaRec.aplicar(mediaParcial, notaRec); // regra simples
        return arredondar1(mf);
    }

    // situacao final apos recuperacao
    // aprovado se media final >= 50 senao reprovado
    public String situacaoFinal(String idAluno) {
        String parcial = situacaoParcial(idAluno);
        if ("APROVADO".equals(parcial)) return "APROVADO";
        if ("REPROVADO_DIRETO".equals(parcial)) return "REPROVADO";
        double mf = calcularMediaPosRecuperacao(idAluno);
        return mf >= 50.0 ? "APROVADO" : "REPROVADO";
    }

    // permite alterar os pesos de p1 e p2
    public void configurarPesos(double pesoP1, double pesoP2) {
        this.pesoP1 = pesoP1;
        this.pesoP2 = pesoP2;
    }

    // metodo utilitario para obter situacao usando enum
    public SituacaoAluno getSituacaoFinalEnum(String idAluno) {
        String situacao = situacaoFinal(idAluno);
        switch (situacao) {
            case "APROVADO":
                return SituacaoAluno.APROVADO;
            case "REPROVADO":
                return SituacaoAluno.REPROVADO;
            default:
                return SituacaoAluno.REPROVADO;
        }
    }

    // metodo para salvar nota de um aluno
    public void salvarNota(String idAluno, Avaliacao avaliacao, double nota) {
        if (!alunoRepo.existsById(idAluno)) {
            throw new AlunoNaoEncontradoException("aluno nao encontrado");
        }
        validarNota(nota);
        notasRepo.salvarNota(idAluno, avaliacao.name(), nota);
    }

    // metodo para verificar se aluno precisa de recuperacao
    public boolean precisaRecuperacao(String idAluno) {
        return "RECUPERACAO".equals(situacaoParcial(idAluno));
    }

    // metodo para verificar se aluno foi aprovado direto
    public boolean aprovadoDireto(String idAluno) {
        return "APROVADO".equals(situacaoParcial(idAluno));
    }

    // metodo para verificar se aluno foi reprovado direto
    public boolean reprovadoDireto(String idAluno) {
        return "REPROVADO_DIRETO".equals(situacaoParcial(idAluno));
    }
}
