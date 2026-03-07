package br.com.sistema.equivalence.exception;

public class AlimentoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlimentoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public AlimentoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}