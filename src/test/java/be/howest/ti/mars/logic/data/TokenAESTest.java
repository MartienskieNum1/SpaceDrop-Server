package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.util.TokenAES;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenAESTest {

    @Test
    void encryption() {
        String toEncrypt = "This needs to be encrypted!";
        String encrypted = TokenAES.encrypt(toEncrypt);
        String decrypted = TokenAES.decrypt(encrypted);

        assertEquals(toEncrypt, decrypted);
    }
}
