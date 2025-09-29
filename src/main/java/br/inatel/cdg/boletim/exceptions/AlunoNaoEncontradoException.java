package br.inatel.cdg.boletim.exceptions;

// excecao quando o aluno nao existir no repositorio
public class AlunoNaoEncontradoException extends RuntimeException {
    public AlunoNaoEncontradoException(String message) {
        super(message);
    }
}
