package m09uf1.impl.speerb;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import m09uf1.prob.speerb.SafePeer;

public class SafePeerImpl implements SafePeer {
	static final Logger LOGGER = Logger.getLogger(SafePeerImpl.class.getName());
	private Consumer<String> consumer;
	private KeyPairGenerator kpg;
	private KeyPair kp;
	private SecretKey sKey;
	private KeyGenerator kg;
	private Cipher cipherEnc, cipherDec, cipher , cipherd;
	SafePeer conectado;
	
	
	public SafePeerImpl(Consumer<String> consumer) {
		this.consumer=consumer;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		kpg.initialize(1024);
	    kp = kpg.generateKeyPair();
	}
	
	@Override
	public PublicKey getPublicKey() {
		    return kp.getPublic();
		    
	}

	@Override
	public void connectTo(SafePeer to) {
		conectado=to;
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(256);
			sKey = kg.generateKey();
		
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE,to.getPublicKey());
			
			byte[] arraySKey = sKey.getEncoded();
			byte[] sKeyEnviar = cipher.doFinal(arraySKey);

			connectFrom(this, sKeyEnviar);
			
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void connectFrom(SafePeer from, byte[] cipheredSecret) {
		try {
			
//			LOGGER.info(kp.getPrivate().toString());
			cipherd = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipherd.init(Cipher.DECRYPT_MODE, kp.getPrivate());
			
			SecretKey key = new SecretKeySpec(cipheredSecret, "RSA");
			byte[] decryptedKey = cipherd.doFinal(key.getEncoded());
			LOGGER.info(decryptedKey.toString());

		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void send(String message) {
		try {
			cipherEnc = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipherEnc.init(Cipher.ENCRYPT_MODE, sKey);
			
			byte[] input = message.getBytes(StandardCharsets.UTF_8);
			byte[] encode = cipherEnc.doFinal(input);
			conectado.receive(encode);
			
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	@Override
	public void receive(byte[] cipheredBytes) {
		
		try {
			SecretKey originalKey = new SecretKeySpec(cipheredBytes,"AES");
			
			cipherEnc = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipherEnc.init(Cipher.ENCRYPT_MODE, sKey);
			byte[] decode = cipherDec.doFinal(originalKey.getEncoded());
			
			consumer.accept(decode.toString());
		
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
		
		
		
	}

}
