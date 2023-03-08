package m09uf1.impl.safeupper2;


import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lib.CoDec;
import lib.CoDecException;

public class MyCoDecServer implements CoDec<String, String> {

	private Cipher cipherEnc, cipherDec, cipher;
	private SecretKey sKey;
	private KeyGenerator kg;

	public MyCoDecServer() {

	}
	
	public String MSGxifrat(String i) {
		byte[] arrPublicKey = Base64.getDecoder().decode(i);
		X509EncodedKeySpec publicKey = new X509EncodedKeySpec(arrPublicKey);
	
		
		try {
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pk = keyFactory.generatePublic(publicKey);
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			
			kg = KeyGenerator.getInstance("AES");
			kg.init(128);
			sKey = kg.generateKey();
			
			cipherEnc = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipherEnc.init(Cipher.ENCRYPT_MODE, sKey);
			cipherDec = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipherDec.init(Cipher.DECRYPT_MODE, sKey);
			
			byte[] arraySKey = sKey.getEncoded();
			byte[] sKeyEnviar = cipher.doFinal(arraySKey);
			
			String b64 = Base64.getEncoder().encodeToString(sKeyEnviar);
			return b64;
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
