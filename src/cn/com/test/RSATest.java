package cn.com.test;

public class RSATest {

	public static void main(String[] args) {
		/*//用于字符串和byte[]之间的互转  
	    UTF8Encoding utf8encoder = new UTF8Encoding();  
	  
	    //产生一对公钥私钥  
	    RSACryptoServiceProvider rsaKeyGenerator = new RSACryptoServiceProvider(1024);  
	    string publickey = rsaKeyGenerator.ToXmlString(false);  
	    string privatekey = rsaKeyGenerator.ToXmlString(true);  
	              
	    //使用公钥加密密码  
	    RSACryptoServiceProvider rsaToEncrypt = new RSACryptoServiceProvider();  
	    rsaToEncrypt.FromXmlString(publickey);  
	    string strPassword = "@123#abc$";  
	    Console.WriteLine("The original password is: {0}", strPassword);  
	    byte[] byEncrypted = rsaToEncrypt.Encrypt(utf8encoder.GetBytes(strPassword), false);  
	    Console.Write("Encoded bytes: ");  
	    foreach (Byte b in byEncrypted)  
	    {  
	        Console.Write("{0}", b.ToString("X"));  
	    }  
	    Console.Write("\n");  
	    Console.WriteLine("The encrypted code length is: {0}", byEncrypted.Length);  
	  
	    //解密  
	    RSACryptoServiceProvider rsaToDecrypt = new RSACryptoServiceProvider();  
	    rsaToDecrypt.FromXmlString(privatekey);  
	    byte[] byDecrypted = rsaToDecrypt.Decrypt(byEncrypted, false);  
	    string strDecryptedPwd = utf8encoder.GetString(byDecrypted);  
	    Console.WriteLine("Decrypted Password is: {0}", strDecryptedPwd);*/

	}

}
