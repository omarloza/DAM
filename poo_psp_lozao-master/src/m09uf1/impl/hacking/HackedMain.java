package m09uf1.impl.hacking;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lib.CryptoKeys;
import m09uf1.prob.hacking.HackedSystem;

public class HackedMain {
	static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	static final String AES_ALGORITHM = "AES";
	static final int AES_BITS = 128;
	static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	static final String RSA_ALGORITHM = "RSA";
	static final int RSA_BITS = 1024;
	static final Charset UTF8 = StandardCharsets.UTF_8;
	private SecretKey secretKey;
	private HackedSystem hs;
	private PublicKey pk;
	private static Cipher msgDecCipher;
	private static Cipher decCipher;
	private static byte[] message2Bytes ;
	
	
	public HackedMain(HackedSystem hs) {
		this.hs = hs;

	}

	public static void Descifrar(String clavePrivada , String msg2 ) {
		
		byte[] arrPrivateKey = Base64.getDecoder().decode(clavePrivada);
		
		System.out.println(CryptoKeys.pretty(arrPrivateKey));

		try {
			 KeyFactory kf = KeyFactory.getInstance(RSA_ALGORITHM);
			 
			 PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(arrPrivateKey);
		     PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);
			
			decCipher = Cipher.getInstance(RSA_TRANSFORMATION);
			decCipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			byte[] llaveDecifrada = Base64.getDecoder().decode(msg2);
			message2Bytes = decCipher.doFinal(llaveDecifrada);
			
			System.out.println(CryptoKeys.pretty(message2Bytes));
			
		
			
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public static void LeerMensaje(String msg3) {
		byte[] mCifrado = Base64.getDecoder().decode(msg3);
		try {
			 SecretKeySpec key =new SecretKeySpec(message2Bytes, AES_ALGORITHM);
			 
				msgDecCipher = Cipher.getInstance(AES_TRANSFORMATION);
				msgDecCipher.init(Cipher.DECRYPT_MODE, key);
	
				byte[] message = msgDecCipher.doFinal(mCifrado);
				String x = new String(message, UTF8);
		
				System.out.println(CryptoKeys.pretty(message));
				System.out.println(x);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}	
	}
	
	public static void main(String[] args) {

		HackedSystem hs = new HackedSystem();
		HackedMain hm = new HackedMain(hs);

	Descifrar(hs.RSA_PRIVATE,hs.MSG2);
	LeerMensaje( hs.MSG3);
	}

}