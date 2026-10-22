package br.com.myfinance.exeption;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
