package br.com.sistema.equivalence.exception;

public class QuantidadeInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }

    public QuantidadeInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}