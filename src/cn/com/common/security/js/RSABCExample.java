package cn.com.common.security.js;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;


public class RSABCExample {

	/** 可以先注册到虚拟机中,再通过名称使用;也可以不注册,直接传入使用 */
	public static final Provider pro = new BouncyCastleProvider();
	/** 种子,改变后,生成的密钥对会发生变化 */
	private static final String seedKey = "random";

	private static final String charSet = "UTF-8";
	
//	private static String publicKeyStr = null;
//	private static String privateKeyStr = null;
	private static PrivateKey privateKey = null;
	private static PublicKey publicKey = null;
	/*private static RSAPrivateKey privateKey = null;
	private static RSAPublicKey publicKey = null;*/
	private static String jsPublicKey = null;
	
	static{
		try {
			generateKeyPair();
			getJsPublicKey();
		} catch (Exception e) {
			throw new RuntimeException("生成密钥对失败");
		}
	}

	/**
	 * 生成密钥对
	 * @throws Exception
	 */
	private static void generateKeyPair() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", pro);
		kpg.initialize(1024, new SecureRandom(seedKey.getBytes()));
		KeyPair kp = kpg.generateKeyPair();

		privateKey = kp.getPrivate();
		publicKey =kp.getPublic();
		System.out.println("......."+((RSAPublicKey)publicKey).getModulus().toString(16));
/*		System.out.println("公钥系数："+((RSAPublicKey) publicKey).getModulus()+"\r\n公钥公用指数："+((RSAPublicKey) publicKey).getPublicExponent());
		 System.out.println("私钥系数："+((RSAPrivateKey) privateKey).getModulus()+"\r\n私钥公用指数："+((RSAPrivateKey) privateKey).getPrivateExponent());*/
		 /*System.out.println("\r\n公钥："+Base64Utils.encode(publicKey.getEncoded()));
		 System.out.println("私钥："+Base64Utils.encode(privateKey.getEncoded()));*/
		 /*
		 System.out.println("\r\n公钥："+new String(Hex.encode(publicKey.getEncoded())));
		 System.out.println("私钥："+new String(Hex.encode(privateKey.getEncoded())));*/
//		privateKeyStr = new String(Base64.encode(privateKey.getEncoded()));
//		publicKeyStr = new String(Base64.encode(publicKey.getEncoded()));

//		System.out.println("PrivateKey:" + privateKey);
//		System.out.println("PublicKey:" + publicKey);

//		System.out.println(privateKeyStr);
//		System.out.println(publicKeyStr);
	}

	/**
	 * 解密
	 */
	public static byte[] decrypt(byte[] encrypted) throws Exception {
		long start = System.currentTimeMillis();
		Cipher cipher = Cipher.getInstance("RSA", pro);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		//MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKyGTm/IplnhIpDyd6L7P1SwJHKWo74sgHDR3La0qJZMg9+XYe0Km1nT4K018hAZ3PgMb7sijUUuTF5J2cfvVGB+eBhnu1R6l4T1BjMAhs9N5crH/MWzZSgdSEbEoW8BNuyh8y5lOPmAb26zLuGb2Zypy8xTm9IZUol3bSIUGO+TAgMBAAECgYAHoEL8uo9T3yZCgJB2PvBGA47Q4kTBpfQiSIlR9pSSWpzkd/h6dfx3Xey+a0+6xqt96wec64JegchOM91LDHkbw85w91oNwydUTsT99z/aJFkDekQD9Xow+i5DQD10fupYowkLlfwArxc+iMV3X0v62mXH4ujRr5lRYfv+RmlhqQJBAN8y4Xpqxc67+/vo+iFxs3CV9eo5pId/rXnoBwRD82RSIabk3ObKEcmTtG5iGZSlnuvEwlXZh/BdiLM1lGG7jCkCQQDF4Pp//7J8tnVR1EKviaJ85Rh4rQ+ziBWf4b597YX49EeTXSAHx4aUBS4PGyc0zC2bDCp6Hzzt6p8AjvJOUNVbAkAfWJH6E0y8gAIfmtSmJcXBpg3nWzkUHoZKfJ+fpbtk93PqcHHlp7Nfz/KjHfvhuHDdQ8DXNbm1tC0inf+8yg4hAkBgyDb9H9z2rm8XRNAQ9ypoF80uSMbjMm4RMuJuyRu7tg7D/spJw8cI4reyX79/TVYI/ZxQ+rJcU2LNbR0KPPL9AkEAnjBGUz+dVZLYrgezjqxZvbR02vjjlZOli5PgvE/3B2obi5m9Qty9ZFDVdrLyA2VdG7xEe1WyO6/T0ZtlzQqf7A==
//		cipher.init(Cipher.DECRYPT_MODE, getPrivateRSAKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKyGTm/IplnhIpDyd6L7P1SwJHKWo74sgHDR3La0qJZMg9+XYe0Km1nT4K018hAZ3PgMb7sijUUuTF5J2cfvVGB+eBhnu1R6l4T1BjMAhs9N5crH/MWzZSgdSEbEoW8BNuyh8y5lOPmAb26zLuGb2Zypy8xTm9IZUol3bSIUGO+TAgMBAAECgYAHoEL8uo9T3yZCgJB2PvBGA47Q4kTBpfQiSIlR9pSSWpzkd/h6dfx3Xey+a0+6xqt96wec64JegchOM91LDHkbw85w91oNwydUTsT99z/aJFkDekQD9Xow+i5DQD10fupYowkLlfwArxc+iMV3X0v62mXH4ujRr5lRYfv+RmlhqQJBAN8y4Xpqxc67+/vo+iFxs3CV9eo5pId/rXnoBwRD82RSIabk3ObKEcmTtG5iGZSlnuvEwlXZh/BdiLM1lGG7jCkCQQDF4Pp//7J8tnVR1EKviaJ85Rh4rQ+ziBWf4b597YX49EeTXSAHx4aUBS4PGyc0zC2bDCp6Hzzt6p8AjvJOUNVbAkAfWJH6E0y8gAIfmtSmJcXBpg3nWzkUHoZKfJ+fpbtk93PqcHHlp7Nfz/KjHfvhuHDdQ8DXNbm1tC0inf+8yg4hAkBgyDb9H9z2rm8XRNAQ9ypoF80uSMbjMm4RMuJuyRu7tg7D/spJw8cI4reyX79/TVYI/ZxQ+rJcU2LNbR0KPPL9AkEAnjBGUz+dVZLYrgezjqxZvbR02vjjlZOli5PgvE/3B2obi5m9Qty9ZFDVdrLyA2VdG7xEe1WyO6/T0ZtlzQqf7A=="));
		
		byte[] re = cipher.doFinal(encrypted);
		long end = System.currentTimeMillis();
		System.out.println("decrypt use time " + (end - start) + "");
		return re;
	}

	/**
	 * 解密js加密后的值
	 */
	public static String decodeJsValue(String jsValue) throws Exception {
		byte[] input = Hex.decode(jsValue);
		byte[] raw = decrypt(input);

		// 标志位为0之后的是输入的有效字节
		int i = raw.length - 1;
		while (i > 0 && raw[i] != 0) {
			i--;
		}
		i++;
		byte[] data = new byte[raw.length - i];
		for (int j = i; j < raw.length; j++) {
			data[j - i] = raw[j];
		}

		return new String(data, charSet);
	}
	
	/**
	 * js加密时使用的公钥字符串
	 * <p><b>注意：</b>
	 * 生成的密钥对的值与 种子（seedKey）、虚拟机实现等都有关系，不同的机器生成的密钥值可能不同。
	 * 在实际测试时发现，同样的环境，有些机器每次生成的密钥值也不同，比如每次重启服务器后值不同。
	 * 所以在实际生产环境中使用时，该值需要通过服务器端输出到客户端。
	 * 如果有多台服务器，可能每台服务器的值不同，所以需要有类似F5的策略，保证多次请求路由到一台服务器上。
	 */
	public static String getJsPublicKey(){
		if(jsPublicKey == null){
//			JCERSAPublicKey jce = (JCERSAPublicKey) publicKey;
			BCRSAPublicKey jce = (BCRSAPublicKey) publicKey;
			jsPublicKey = jce.getModulus().toString(16);
		}
		System.out.println("jsPublicKey:"+jsPublicKey);
		String jr=Base64.toBase64String(((BCRSAPrivateKey)privateKey).getEncoded());
		if(jr.equals(jsPublicKey)){
			System.out.println("True");
		}else{
			System.out.println("False");
		}
		System.out.println("jsPrivateKey:"+jr);
		return jsPublicKey;
	}
		

	public static void main(String[] args) throws Exception {
	
		//注意：需要使用该值替换test.html中的公钥值
		System.out.println("js中使用的公钥字符串" + getJsPublicKey());
		
		// js加密后的值
		String de = "08f7e292ccb4c73a981569a9c2dbf2b9c0c2cf615967282863e6e358432af288f1f026ed91a8ff5f6579ac246af9ce1f94f85e92b8a926627b95e6bd05b00b80a5548e9ce1a9bb2a20073cce629936ab9e27021af7370c2664065107a702c1805a4ec131a3573007213da3e390221053867074a427ffc28aa642fe2099ad7332";
		/*de="a28cde15d3aea77593810292ee48659e2eadc43193eedcc72773a0df41b902a0e07abd723d3f7179612507eced9398403e90f490a3518bb925b408a9f827aa1e995037fd5fd53ac3f19de4868f59f65ffc7c5b11ed681e35d4373fbc903bbda5dcece40797b1fc9e39c65749d64ad42e7ba44a9955b8d2af57abf83ad7e23e76";
		de="42b97ddd45578c6ac0651fe4eb9382e2d401b2b8a55c3546bbd13aee60900ca53db8b2d792880c6ac9782f096c69d3beeef717aa08a90565171032ab023da9d2fb85e219b80d8d77e8626930a806b4eaf72dc3dbae77fdfe65748ec5097a648f797faca7cfed97386651be4ac21049e72b698089bb8aa352c4a33f57123f1bd5";
		de="025686a65bec6286cf4191dd971bb86bc3208faa0060314c72f0218f0007ebf53c18d5fb1f5c40e0fe0ee80c519f370b916429e2b7f4123f5daafc8baec3c09fff462950b789fc5bdafd3de015a873bf601db04811fe85eaa46187d36290b50594c2c87a5edd51a4e161";
		de="1497b54eb8088d3c59d3b9f3bdb818b077ee22f734c364f88436680e86b19b6d35930823639474afffefb1b922883e1ca989531e08153bcf323b0d51c45da932cab395dba3a9d623c47c07af634253fe3af66e270183439f2b890969df1510f8d2cc698e4045634ceb007fd46cc841baf257cc8817726c27263332f834595733b4c6e9aa35e5efa511cd53eb0bb7f6e4a4b8ea387f20c3eb2129c3bb54c9a0646db0";
		*/
		de="e9aad388fcfc2e130097cc09586dd6d32633562ee3e31f0d5b62f7cbb7f094c48da9a3ca54731a75a12905d53b94116537acc91d8426c15c6a057af79eb13c99473abc03f2dc711191c0385404859d8c929f9c865a1076beb32309649aad8552fa4e09b16ee778ac2f45fd37e39efb8ffdf21622bc860c7ea7b3c94830edb4bf3ec3edbd9fc1e1a559c509eff9fc1240e0fef92ae066be9dba8b";
		de="880ebc0b3a09b727af23c5374c984cd6616bf07bc429b7083a7e2fe58c4400a448690c05d7abab9721a159e410d83a7147d2739951175778309270d5e9b6ea3f4a7bee4c4abfed15975bc331675cfee5e88f8d88ce2bc86f8eb42c3bceb804e9b61a13e9870ea5c6248325ee21e6c40af9d01ffb22fb715ef8e154b577fb6f5c";
		de="6a32f3a20383733cf4da12f728cecfc261a9a5e0e0afe4db722ab1ef8ffaf5450f1942f76ee5f1322cb8cba24a480f322be5488b67440c5681ccac9aa4e31e325d63a9a6e1ed4c4fd6e52efaac09376e89e990d71ca9d91a196d65e4b1547a1cd557757815cae9d64cd76711e9b2409740d486d9522f24d19e7488d1032ba268";
		de="7c0b260989c4029bc98b68a58537e89015f40e6edb4423dd3b720ba1556b6ced1e1fa1f475389e6192a574ae46a8a7b634dba83728cdf02cd4d39add8c9f80e2ab5aae662d39aae3154fc5556e501c11c1882354ce786618686be646f8491fbd0b88e2932dfa065fcdfd74cce001ec357751ec268879b4058449dacd760afe17";
		System.out.println(decodeJsValue(de));
		/*String test=Base64.toBase64String(Hex.decode("ac864e6fc8a659e12290f277a2fb3f54b0247296a3be2c8070d1dcb6b4a8964c83df9761ed0a9b59d3e0ad35f21019dcf80c6fbb228d452e4c5e49d9c7ef54607e781867bb547a9784f506330086cf4de5cac7fcc5b365281d4846c4a16f0136eca1f32e6538f9806f6eb32ee19bd99ca9cbcc539bd2195289776d221418ef93"));
		
		System.out.println("..."+test);
		System.out.println("..."+new String(Hex.encode(Base64.decode(test))));*/
	}
	
	//-------------------------下面方法可以不用
	
	private void test(){
		// check equals
//		PublicKey pb = getPublicRSAKey(publicKeyStr);
//		System.out.println(pb.equals(publicKey));
//		PrivateKey ppk = getPrivateRSAKey(privateKeyStr);
//		System.out.println(ppk.equals(privateKey));
//
//		String input = "测试abcABC123";
//		byte[] en = encrypt(input);
//		System.out.println(new String(Hex.encode(en)));
//
//		byte[] re = decrypt(en);
//		System.out.println(new String(re, charSet));
	}
	
	/**
	 * 根据Base64编码的公钥值生成公钥对象
	 * <p>
	 * 测试时使用，可以用于从证书文件中的公钥生成公钥对象。如果不涉及到证书操作，可以忽略该方法。
	 */
	public static PublicKey getPublicRSAKey(String key) throws Exception {
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(Base64.decode(key));
		KeyFactory kf = KeyFactory.getInstance("RSA", pro);
		return kf.generatePublic(x509);
	}

	/**
	 * 根据Base64编码的私钥值生成私钥对象
	 * <p>
	 * 测试时使用，可以用于从证书文件中的私钥生成私钥对象。如果不涉及到证书操作，可以忽略该方法。
	 */
	public static PrivateKey getPrivateRSAKey(String key) throws Exception {
		PKCS8EncodedKeySpec pkgs8 = new PKCS8EncodedKeySpec(Base64.decode(key));
		KeyFactory kf = KeyFactory.getInstance("RSA", pro);
		return kf.generatePrivate(pkgs8);
	}
	
	/**
	 * 加密
	 */
	public static byte[] encrypt(String input) throws Exception {
		long start = System.currentTimeMillis();
		Cipher cipher = Cipher.getInstance("RSA", pro);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] re = cipher.doFinal(input.getBytes(charSet));
		long end = System.currentTimeMillis();
		System.out.println("encrypt use time " + (end - start) + "");
		return re;
	}	

}
