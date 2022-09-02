package com.assignsecurities.app.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.RandomStringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public final class SessionIdentifierGenerator {
	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		String randomStr = new BigInteger(130, random).toString(32);
		randomStr = randomStr + System.currentTimeMillis();
		return randomStr;
	}

	public static String getOTP(int length) {
		Random r = new Random();
		String otp = new String();
		for (int i = 0; i < length; i++) {
			otp += r.nextInt(10);
		}
		return otp;
	}

	private static final Random RANDOM = new SecureRandom();
	/** Length of password. @see #generateRandomPassword() */
	public static final int PASSWORD_LENGTH = 8;

	/**
	 * Generate a random String suitable for use as a temporary password.
	 *
	 * @return String suitable for use as a temporary password
	 * @since 2.4
	 */
	public static String generateRandomPassword() {
		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

		String pw = "";
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (RANDOM.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		//TODO will remove on email implementation
//		pw="Pdms@123";
		return pw;
	}
	
	

	public static String generateUniqueId(Integer uniqueIdSize) {
	    return RandomStringUtils.randomAlphanumeric(uniqueIdSize);
	}
	public static String leftPadString(String strinToPaleftPad,Integer uniqueIdSize) {
		Integer prefixLength=uniqueIdSize-strinToPaleftPad.length();
		String finalString =generateUniqueId(prefixLength)+ strinToPaleftPad;
//	    return StringUtils.leftPad(strinToPaleftPad, uniqueIdSize,"x");
		return finalString;  
	}
//	String sendID = "AABB";
//	String output = String.format("%0"+(32-sendID.length())+"d%s", 0, sendID);
//	StringUtils.leftPad(sendID, 32 - sendID.length(), '0');
	public static String generateUniqueKeyUsingUUID() {
		// Static factory to retrieve a type 4 (pseudo randomly generated) UUID
		String crunchifyUUID = UUID.randomUUID().toString();
		return crunchifyUUID;
	}
	
	private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
	
	
	//Sample method to construct a JWT
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
    
	
	public static void main(String[] args) {
		for(int i=0;i<15;i++) {
			String uuId=generateUniqueKeyUsingUUID();
			System.out.println(uuId+"<==uuId==>"+uuId.length());
			String generateUniqueId=generateUniqueId(30);
			System.out.println(generateUniqueId+"<==generateUniqueId==>"+generateUniqueId.length());
		}
	}
	
}