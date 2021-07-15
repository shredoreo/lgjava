package com.shred.spring.exception;

public class BeanNoDefException extends Exception{
    public BeanNoDefException(){
        super();
    }

    //用详细信息指定一个异常
    public BeanNoDefException(String message){
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public BeanNoDefException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public BeanNoDefException(Throwable cause) {
        super(cause);
    }
}
