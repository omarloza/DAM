package m09uf1.impl.passmgr;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.json.JSONObject;
import lib.db.DuplicateException;
import lib.db.NotFoundException;
import m09uf1.prob.passmgr.PassManager;

public class PassManagerImpl implements PassManager {
	static final Logger LOGGER = Logger.getLogger(PassManagerImpl.class.getName());
	private JSONObject entries;

	public PassManagerImpl(JSONObject entries) {
		this.entries = entries;

	}

	@Override
	public void addEntry(String user, char[] pass) throws DuplicateException {

		if (!entries.isNull(user)) {
			throw new DuplicateException(user + "ja existeix");
		} else {
			try {
				String i = new String(pass);
				byte[] rSalt = salt(16);
				byte[] hash = generateSecretHash(i, rSalt);

				entries.put(user, new JSONObject().put("pass", Base64.getEncoder().encodeToString(hash)).put("salt",
						Base64.getEncoder().encodeToString(rSalt)));

			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		}

	}

	private byte[] salt(int size) {

		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	private byte[] generateSecretHash(String i, byte[] salt2) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec ks = new PBEKeySpec(i.toCharArray(), salt2, 16384, 256);
		SecretKey s = f.generateSecret(ks);
		return s.getEncoded();
	}

	@Override
	public void deleteEntry(String user) throws NotFoundException {
		if (entries.isNull(user)) {
			throw new NotFoundException(user + "no existeix");
		} else {
			entries.remove(user);
		}

	}

	@Override
	public boolean checkPassword(String user, char[] pass) throws NotFoundException {
		if (entries.isNull(user)) {
			throw new NotFoundException(user + "no existeix");
		} else {
			try {
				JSONObject keyValue = entries.getJSONObject(user);
				String password = keyValue.getString("pass");
				String salt = keyValue.getString("salt");
				String i = new String(pass);

				byte[] rSalt = Base64.getDecoder().decode(salt.getBytes("UTF-8"));
				byte[] hash = generateSecretHash(i, rSalt);

				String b64 = Base64.getEncoder().encodeToString(hash);
				if (b64.equals(password)) {
					return true;
				}

			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			} catch (InvalidKeySpecException e) {

				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

		return false;

	}

	@Override
	public boolean setPassword(String user, char[] oldpass, char[] newpass) throws NotFoundException {
		if (entries.isNull(user) == true) {
			throw new NotFoundException(user + "no existeix");
		} else {
			try {
				if (checkPassword(user, oldpass)==true) {
					JSONObject keyValue = entries.getJSONObject(user);
					String j = new String(newpass);
					byte[] rSaltNew = salt(16);
					byte[] hashNew = generateSecretHash(j, rSaltNew);

					keyValue.put("pass", Base64.getEncoder().encodeToString(hashNew));
					keyValue.put("salt", Base64.getEncoder().encodeToString(rSaltNew));
					return true;
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}

		}

		return false;
	}

}
