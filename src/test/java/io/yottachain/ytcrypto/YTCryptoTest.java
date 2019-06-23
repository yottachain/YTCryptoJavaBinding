package io.yottachain.ytcrypto;

import io.yottachain.ytcrypto.core.exception.YTCryptoException;
import io.yottachain.ytcrypto.core.vo.KeyPair;
import org.junit.Test;

import static org.junit.Assert.*;

public class YTCryptoTest {

    @Test
    public void createKeyTest() throws YTCryptoException  {
        KeyPair keyPair = YTCrypto.createKey();
        assertNotNull(keyPair);
    }

    @Test
    public void ecrecoverTest() throws YTCryptoException {
        String pubkey = YTCrypto.ecrecover("123456".getBytes(), "SIG_K1_KUNpoQZM1zDkWi5g93u8S4cWjWfpfHmh3aC2iZHBFZWbeipRWRVVDvnevEoEBuGSLYExHdQHkP6MWr2dhQ8FThNjQEuEon");
        assertEquals("8goL8rkhDAyLkVzwAhYUcHMNfHTAqFgJvw3W3LKu6ibXQ7pv2F", pubkey);
    }

    @Test
    public void signTest() throws YTCryptoException {
        String sig = YTCrypto.sign("5J2XGbuK9L35VtxMKjEMWoCB3Aw7RdAVKuPizgqzwSgKLseXSYs", "123456".getBytes());
        assertNotNull(sig);
    }

    @Test
    public void verifyTest() throws YTCryptoException {
        assertTrue(YTCrypto.verify("8goL8rkhDAyLkVzwAhYUcHMNfHTAqFgJvw3W3LKu6ibXQ7pv2F", "123456".getBytes(), "SIG_K1_KUNpoQZM1zDkWi5g93u8S4cWjWfpfHmh3aC2iZHBFZWbeipRWRVVDvnevEoEBuGSLYExHdQHkP6MWr2dhQ8FThNjQEuEon"));
    }

    @Test
    public void encryptAndDecryptTest() throws YTCryptoException {
        byte[] ecData = YTCrypto.eccEncrypt("123456".getBytes(), "5aXTqsNsHdhLEeBRsf4ikwM2PtjzGCi8vnmBWhbiR7LpwDyph3");
        byte[] data = YTCrypto.eccDecrypt(ecData, "5HrS4QMMmBbStmAeeay25LG4hUxdnH44JoVNBLdNuf4aXwwBsMe");
        assertArrayEquals("123456".getBytes(), data);
    }
}
