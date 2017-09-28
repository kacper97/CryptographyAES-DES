package AES;
import java.io.*;
import javax.crypto.*;
//import javax.crypto.spec.*;


public class AESECBEncrypt
{
    /**
     * Encrypts message provided on command line and writes to file
     * @param args
     */
    public static void main(String args[])
    {
        try
        {
            if (args.length != 1)
            {
                System.out.println("Please provide input as one argument. Use quotation marks if needed.");
                throw new Exception();
            }
            
            // File containing secret AES key
            FileInputStream keyFIS = new FileInputStream("AESKeyFile");
            ObjectInputStream keyOIS = new ObjectInputStream(keyFIS);
            
            // Read in the AES key
            SecretKey aesKey = (SecretKey) keyOIS.readObject();
            keyOIS.close();
            keyFIS.close();
            
            // set IV (required for CBC ONLY)
           /** 
            byte[] iv ={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
            IvParameterSpec ips = new IvParameterSpec(iv);
           */
            // Create AES cipher instance
            Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            
            // Initialize the cipher for encryption
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey/**,ips*/);
            
            // File for writing output
            FileOutputStream fos = new FileOutputStream("scrambled");
            
            // Read first command-line arg into a buffer.
            // This is the messge to be encrypted
            byte plaintext[] = args[0].getBytes();
            
            // Encrypt the plaintext
            byte[] ciphertext = aesCipher.doFinal(plaintext);
            
            // Write ciphertext to file
            fos.write(ciphertext);
            fos.close();
            
            // Also display it in Hex on stdout
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < ciphertext.length; i++)
            {
                hexString.append(Integer.toHexString(0xF & ciphertext[i]>>4));
                hexString.append(Integer.toHexString(0xF & ciphertext[i]));
                hexString.append(" ");
            }
            System.out.println("Plaintext: " + args[0]);
            System.out.println("Ciphertext: " + hexString.toString());
            
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}