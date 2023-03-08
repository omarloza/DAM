package m09uf1.impl.safeupper;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lib.CoDec;
import lib.CoDecException;

public class MyCoDec implements CoDec<String, String> {
	private Cipher cipherEnc, cipherDec;
	static final Charset CHARSET = MyCoDec.CHARSET;

	public MyCoDec(byte[] keyBytes) {

		SecretKey myKey = new SecretKeySpec(keyBytes, "AES");

		try {
			cipherEnc = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipherEnc.init(Cipher.ENCRYPT_MODE, myKey);
			cipherDec = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipherDec.init(Cipher.DECRYPT_MODE, myKey);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String encode(String i) throws CoDecException {
		byte[] input = i.getBytes(StandardCharsets.UTF_8);
		try {

			byte[] encode = cipherEnc.doFinal(input);
			String b64 = Base64.getEncoder().encodeToString(encode);
			return b64;
		} catch (GeneralSecurityException e) {
			throw new CoDecException(e);
		}

	}

	@Override
	public String decode(String o) throws CoDecException {

		try {
			byte[] decode = cipherDec.doFinal(Base64.getDecoder().decode(o));
			return new String(decode, StandardCharsets.UTF_8);
		} catch (GeneralSecurityException e) {
			throw new CoDecException(e);
		}
	}

}
