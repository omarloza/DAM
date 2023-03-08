package m09uf1.impl.safeupper2;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lib.CoDec;
import lib.CoDecException;

public class MyCoDecClient implements CoDec<String, String> {
	private Cipher cipherEnc, cipherDec, cipher;
	private KeyPair kp;
	private KeyPairGenerator kpg;
	public MyCoDecClient() {
		
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
	        kpg.initialize(1028);
	        kp = kpg.generateKeyPair();
	      
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
	
	public String PublicKey() {
		
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, kp.getPrivate());
			byte[] publicKey = kp.getPublic().getEncoded();
			String b64 = Base64.getEncoder().encodeToString(publicKey);
			return b64;	
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public void readSecret(String sk) {

			try {
				byte[] secretKey = Base64.getDecoder().decode(sk);
				byte[] decryptedKey = cipher.doFinal(secretKey);
				
				SecretKey originalKey = new SecretKeySpec(decryptedKey,"AES");
				cipherEnc = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipherEnc.init(Cipher.ENCRYPT_MODE, originalKey);
				cipherDec = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipherDec.init(Cipher.DECRYPT_MODE, originalKey);
				
			} catch (GeneralSecurityException e) {
				throw new RuntimeException(e);
			}
			
		
	}

}
