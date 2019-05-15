package io.yottachain.ytcrypto;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import io.yottachain.ytcrypto.core.exception.YTCryptoException;
import io.yottachain.ytcrypto.core.vo.KeyPair;
import io.yottachain.ytcrypto.core.wrapper.YTCryptoWrapper;

public class YTCrypto {

    public static KeyPair createKey() throws YTCryptoException {
        Pointer ptr = YTCryptoWrapper.YTCryptoLib.INSTANCE.CreateKey();
        if (ptr != null) {
            try {
                YTCryptoWrapper.Keyret keyret = new YTCryptoWrapper.Keyret(ptr);
                String privateKey = keyret.privatekey.getString(0);
                String publicKey = keyret.publickey.getString(0);
                return new KeyPair(privateKey, publicKey);
            } finally {
                YTCryptoWrapper.YTCryptoLib.INSTANCE.FreeKeyret(ptr);
            }
        } else {
            throw new YTCryptoException("unknown exception");
        }
    }

    public static String sign(String privateKey, byte[] data) throws YTCryptoException {
        Pointer dataPtr = new Memory(Native.getNativeSize(Byte.TYPE) * data.length);
        for (int i=0; i<data.length; i++) {
            dataPtr.setByte(i, data[i]);
        }
        Pointer ptr = YTCryptoWrapper.YTCryptoLib.INSTANCE.Sign(privateKey, dataPtr, data.length);
        Native.free(Pointer.nativeValue(dataPtr));
        Pointer.nativeValue(dataPtr, 0);
        if (ptr != null) {
            try {
                YTCryptoWrapper.Stringwitherror swe = new YTCryptoWrapper.Stringwitherror(ptr);
                if (swe.error != null) {
                    String error = swe.error.getString(0);
                    throw new YTCryptoException(error);
                }
                return swe.str.getString(0);
            } finally {
                YTCryptoWrapper.YTCryptoLib.INSTANCE.FreeStringwitherror(ptr);
            }
        } else {
            throw new YTCryptoException("unknown exception");
        }
    }

    public static boolean verify(String publicKey, byte[] data, String signature) throws YTCryptoException {
        Pointer dataPtr = new Memory(Native.getNativeSize(Byte.TYPE) * data.length);
        for (int i=0; i<data.length; i++) {
            dataPtr.setByte(i, data[i]);
        }
        int ret = YTCryptoWrapper.YTCryptoLib.INSTANCE.Verify(publicKey, dataPtr, data.length, signature);
        Native.free(Pointer.nativeValue(dataPtr));
        Pointer.nativeValue(dataPtr, 0);
        return ret==1;
    }

    public static byte[] eccEncrypt(byte[] data, String publicKey) throws YTCryptoException {
        Pointer dataPtr = new Memory(Native.getNativeSize(Byte.TYPE) * data.length);
        for (int i=0; i<data.length; i++) {
            dataPtr.setByte(i, data[i]);
        }
        Pointer ptr = YTCryptoWrapper.YTCryptoLib.INSTANCE.ECCEncrypt(dataPtr, data.length, publicKey);
        Native.free(Pointer.nativeValue(dataPtr));
        Pointer.nativeValue(dataPtr, 0);
        if (ptr != null) {
            try {
                YTCryptoWrapper.Byteswitherror bwe = new YTCryptoWrapper.Byteswitherror(ptr);
                if (bwe.error != null) {
                    String error = bwe.error.getString(0);
                    throw new YTCryptoException(error);
                }
                byte[] dataRet = bwe.data.getByteArray(0, (int)bwe.size);
                return dataRet;
            } finally {
                YTCryptoWrapper.YTCryptoLib.INSTANCE.FreeByteswitherror(ptr);
            }
        } else {
            throw new YTCryptoException("unknown exception");
        }
    }

    public static byte[] eccDecrypt(byte[] data, String privateKey) throws YTCryptoException {
        Pointer dataPtr = new Memory(Native.getNativeSize(Byte.TYPE) * data.length);
        for (int i=0; i<data.length; i++) {
            dataPtr.setByte(i, data[i]);
        }
        Pointer ptr = YTCryptoWrapper.YTCryptoLib.INSTANCE.ECCDecrypt(dataPtr, data.length, privateKey);
        Native.free(Pointer.nativeValue(dataPtr));
        Pointer.nativeValue(dataPtr, 0);
        if (ptr != null) {
            try {
                YTCryptoWrapper.Byteswitherror bwe = new YTCryptoWrapper.Byteswitherror(ptr);
                if (bwe.error != null) {
                    String error = bwe.error.getString(0);
                    throw new YTCryptoException(error);
                }
                byte[] dataRet = bwe.data.getByteArray(0, (int)bwe.size);
                return dataRet;
            } finally {
                YTCryptoWrapper.YTCryptoLib.INSTANCE.FreeByteswitherror(ptr);
            }
        } else {
            throw new YTCryptoException("unknown exception");
        }
    }

    public static void main(String[] args) throws YTCryptoException {
        KeyPair kp = createKey();
        System.out.println(kp.getPrivateKey());
        System.out.println(kp.getPublicKey());
        String sig = sign(kp.getPrivateKey(), "123456".getBytes());
        System.out.println(verify(kp.getPublicKey(), "123456".getBytes(), sig));
        byte[] ecdata = eccEncrypt("123hahaha".getBytes(), kp.getPublicKey());
        System.out.println(new String(ecdata));
        byte[] data = eccDecrypt(ecdata, kp.getPrivateKey());
        System.out.println(new String(data));
    }
}
