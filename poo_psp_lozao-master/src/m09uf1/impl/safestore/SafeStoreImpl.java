package m09uf1.impl.safestore;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import m09uf1.prob.safestore.SafeStore;
import m09uf1.prob.safestore.WrongKeyException;
import lib.CoDecException;
import lib.TextFileStorage;

public class SafeStoreImpl implements SafeStore {
	static final Logger LOGGER = Logger.getLogger(SafeStoreImpl.class.getName());
	private String storageName;
	private JSONObject obj;
	private TextFileStorage tf;
	private KeyGenerator kg;
	private Cipher cipher;
	private byte[] encrypted;
	private SecretKey keyKEK;
	private boolean cerrado;


	public SafeStoreImpl(String storageName) {
		this.storageName = storageName;
		tf = new TextFileStorage();
		cerrado = false;
	}

	@Override
	public File getPath() {
		return new File(tf.getWriteAbsolutePath(storageName));
	}

	@Override
	public void init(char[] password) throws IOException {
		try {

			String i = new String(password);
			byte[] salt = genSecureRandom(16);
			byte[] kiv = genSecureRandom(16);
			byte[] div = genSecureRandom(16);

			byte[] kek = generateKEK(i, salt);
			keyKEK = new SecretKeySpec(kek, "AES");

			kg = KeyGenerator.getInstance("AES");
			kg.init(256);
			SecretKey myKey = kg.generateKey();
			byte[] uncdDEK = myKey.getEncoded();

			IvParameterSpec paramSpec = new IvParameterSpec(kiv);

			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, keyKEK, paramSpec);

			encrypted = cipher.doFinal(uncdDEK);

			obj = new JSONObject().put("SALT", Base64.getEncoder().encodeToString(salt))
					.put("DIV", Base64.getEncoder().encodeToString(kiv))
					.put("KIV", Base64.getEncoder().encodeToString(div))
					.put("DEK", Base64.getEncoder().encodeToString(encrypted));

			tf.writeToFile(storageName, obj.toString());

		} catch (GeneralSecurityException e) {
			throw new CoDecException(e);
		}

		close();
	}

	@Override
	public void open(char[] password) throws IOException {
		try {
			
			String texto = tf.readFromFile(storageName);
			LOGGER.info(texto);
		} catch (IOException e) {
			throw new IOException("problemas");
		}
	}

	@Override
	public void close() {

		cerrado = true;

	}

	@Override
	public boolean isClosed() {

		return cerrado;
	}

	@Override
	public String get(String key) throws WrongKeyException {
		if (this.isClosed() == true) {
			LOGGER.info("Cerrado");
		}

		try {
			String jsonFile = tf.readFromFile(storageName);
			JSONObject json = new JSONObject(jsonFile);
			String div = json.getString("DIV");
			byte[] rDiv = Base64.getDecoder().decode(div.getBytes("UTF-8"));
			IvParameterSpec paramSpec = new IvParameterSpec(rDiv);
			byte[] decrypted = cipher.doFinal(encrypted);
			String decriptedKey = new String(decrypted, StandardCharsets.UTF_8);
			cipher.init(Cipher.DECRYPT_MODE, keyKEK, paramSpec);

			return decriptedKey;

		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {
			throw new WrongKeyException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public void set(String key, String value) throws WrongKeyException {

		if (isClosed() == true) {
			LOGGER.info("Cerrado");
		}

		try {
			String jsonFile = tf.readFromFile(storageName);
			LOGGER.info("este es el json" + jsonFile);
			JSONObject json = new JSONObject(jsonFile);

			if (json.isNull(key)) {
				json.put(key, value);
			} else {

				if (json.getString(key) == null) {
					json.remove(key);
				}

				String newKey = json.getString(key);
				String div = json.getString("DIV");
				byte[] rDiv = Base64.getDecoder().decode(div.getBytes("UTF-8"));
				IvParameterSpec paramSpec = new IvParameterSpec(rDiv);
				cipher.init(Cipher.DECRYPT_MODE, keyKEK, paramSpec);

				byte[] decrypted = cipher.doFinal(encrypted);
				String decriptedKey = new String(decrypted, StandardCharsets.UTF_8);
				
				
				json.put(key, value);
				
				
				tf.writeToFile(storageName, json.toString());

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			throw new WrongKeyException(e);
		}

	}

	@Override
	public void setPassword(char[] usepass) throws WrongKeyException {

	}

	private byte[] genSecureRandom(int size) {
		SecureRandom random = new SecureRandom();
		byte[] sRandom = new byte[16];
		random.nextBytes(sRandom);
		return sRandom;
	}

	private byte[] generateKEK(String i, byte[] salt2) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec ks = new PBEKeySpec(i.toCharArray(), salt2, 16384, 256);
		SecretKey s = f.generateSecret(ks);
		return s.getEncoded();
	}

}
