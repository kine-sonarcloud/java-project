package come.kine.email.verifications;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

import com.kine.email.constants.KineTenantEmailConstants;
import com.kine.email.constants.KineTenantEmailConstants.KineVerificationErrorCode;
import com.kine.email.exceptions.KineVerificationException;

public class KineVerificationToken {

	private static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	private static final String SIMPLE_FORMAT = "EE MMM dd HH:mm:ss z yyyy";

	private KeySpec keySpec;
	private Cipher cipher;
	private SecretKey key;
	private byte[] arrayBytes;
	private SecretKeyFactory secretKeyFactory;
	private String apiCode;

	/**
	 * 
	 */
	public KineVerificationToken() {
	}

	/**
	 * 
	 * @param kineTokenKey
	 * @throws KineTenantRegisterException
	 */
	public KineVerificationToken(JSONObject kineTokenKey) throws KineVerificationException {

		try {
			arrayBytes = kineTokenKey.get("value").toString().getBytes(StandardCharsets.UTF_8);

			keySpec = new DESedeKeySpec(arrayBytes);
			secretKeyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
			cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
			key = secretKeyFactory.generateSecret(keySpec);
			apiCode = kineTokenKey.get("code").toString();
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
			throw new KineVerificationException(KineVerificationErrorCode.TOKEN_VERIFICATION_FAILED.getCode(),
					"Invalid Token");
		}

	}

	/**
	 * 
	 * @param unencryptedString
	 * @return
	 * @throws KineTenantRegisterException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public String encrypt(String userEmail, String pwd, Object kineTokenKey) throws KineVerificationException {
		String encryptedString = null;
		try {
			String kineVerificationToken = userEmail + KineTenantEmailConstants.TOKEN_SEP + pwd
					+ KineTenantEmailConstants.TOKEN_SEP + kineTokenKey + KineTenantEmailConstants.TOKEN_SEP
					+ new Date();
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = kineVerificationToken.getBytes(StandardCharsets.UTF_8);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64URLSafe(encryptedText));

		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new KineVerificationException(0, e.getMessage());
		}
		return encryptedString + apiCode;
	}

	/**
	 * 
	 * @param encryptedString
	 * @return
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws ParseException
	 * @throws KineTenantRegisterException
	 */
	public Optional<String> decrypt(String encryptedString, boolean checkExpiry) throws KineVerificationException {
		String decryptedText = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] encryptedText = Base64.decodeBase64(encryptedString.substring(0, encryptedString.length() - 8));
			byte[] plainText = cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);

			if (checkExpiry && checkExpiry(
					decryptedText.substring(decryptedText.lastIndexOf(KineTenantEmailConstants.TOKEN_SEP) + 1))) {
				throw new KineVerificationException(KineVerificationErrorCode.TOKEN_EXPIRED.getCode(), "Token Expired");

			}

		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | ParseException e) {
			throw new KineVerificationException(KineVerificationErrorCode.TOKEN_VERIFICATION_FAILED.getCode(),
					"Invalid Token");
		}
		return Optional.ofNullable(decryptedText);
	}

	/**
	 * 
	 * @param decryptedText
	 * @return
	 * @throws ParseException
	 */
	public boolean checkExpiry(String expiryDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_FORMAT, Locale.ENGLISH);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(expiryDate));
		calendar.add(Calendar.DATE, 3);

		return calendar.compareTo(Calendar.getInstance()) < 0;

	}
}