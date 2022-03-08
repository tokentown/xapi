package com.pandora.exception;

/**
 * @ClassName FileIoException
 * @Deacription TODO
 * @Author liuqiang
 * @Date 2020-10-09 15:18
 **/
public class DdApiException extends RuntimeException {
    public DdApiException() {
        super();
    }


    public DdApiException(String msg) {
        super(msg);
    }

    public DdApiException(Throwable cause, String msg) {
        super(msg, cause);
    }
}
