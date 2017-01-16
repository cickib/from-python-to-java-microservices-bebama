package analyticsService;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SaltHasher {

    private static String saltParam = "bebamanalytics";

    public static String hashString(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] digest = null;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((key + saltParam).getBytes("UTF-8"));
        digest = md.digest();
        return DatatypeConverter.printHexBinary(digest);
    }
}
