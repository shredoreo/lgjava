package com.shred.spring.exception;

public class AmbiguousBeanException extends Exception{
    public AmbiguousBeanException(){
        super();
    }

    //用详细信息指定一个异常
    public AmbiguousBeanException(String message){
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public AmbiguousBeanException(String message, Throwable cause){
        super(message,cause);
    }

    //用指定原因构造一个新的异常
    public AmbiguousBeanException(Throwable cause) {
        super(cause);
    }
}
