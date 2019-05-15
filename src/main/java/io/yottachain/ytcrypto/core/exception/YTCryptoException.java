package io.yottachain.ytcrypto.core.exception;

public class YTCryptoException extends Exception {
    public YTCryptoException(String msg) {
        super(msg);
    }

    public YTCryptoException(String errmsg, Throwable t) {
        super(errmsg, t);
    }
}
