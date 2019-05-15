package io.yottachain.ytcrypto.core.wrapper;

import com.sun.jna.*;

import java.util.Arrays;
import java.util.List;

public class YTCryptoWrapper {
    public static class Keyret extends Structure {
        public Pointer privatekey;
        public Pointer publickey;

        public Keyret(Pointer ptr) {
            super(ptr);
            read();
        }

        @Override
        protected List getFieldOrder() {
            return Arrays.asList(new String[]{"privatekey", "publickey"});
        }
    }

    public static class Stringwitherror extends Structure {
        public Pointer str;
        public Pointer error;

        public Stringwitherror(Pointer ptr) {
            super(ptr);
            read();
        }


        @Override
        protected List getFieldOrder() {
            return Arrays.asList(new String[]{"str", "error"});
        }
    }

    public static class Byteswitherror extends Structure {
        public Pointer data;
        public long size;
        public Pointer error;

        public Byteswitherror(Pointer ptr) {
            super(ptr);
            read();
        }

        @Override
        protected List getFieldOrder() {
            return Arrays.asList(new String[]{"data", "size", "error"});
        }
    }


    public interface YTCryptoLib extends Library {
        YTCryptoLib INSTANCE = (YTCryptoLib)
                Native.load(Platform.isWindows()?"ytcrypto.dll":"ytcrypto.so",
                        YTCryptoLib.class);

        Pointer CreateKey();
        Pointer Sign(String privateKey, Pointer data, long size);
        int Verify(String publicKey, Pointer data, long size, String signature);
        Pointer ECCEncrypt(Pointer data, long size, String publicKey);
        Pointer ECCDecrypt(Pointer data, long size, String privateKey);

        void FreeKeyret(Pointer ptr);
        void FreeStringwitherror(Pointer ptr);
        void FreeByteswitherror(Pointer ptr);
    }
}
