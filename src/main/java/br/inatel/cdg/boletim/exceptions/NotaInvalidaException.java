package br.inatel.cdg.boletim.exceptions;

// excecao simples para nota fora de 0 a 100
public class NotaInvalidaException extends RuntimeException {
    public NotaInvalidaException(String message) {
        super(message);
    }
}
