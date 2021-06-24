/**
 * 
 * I WILL NOT BE DOING ANY DOCUMENTATION FOR THIS.
 * 
 * THIS IS A QUICK AND DIRTY PROGRAM.
 * IT IS YOUR HEADACHE IF YOU WANT TO USE IT OR NOT.
 * 
 */


import java.io.*;
import java.util.*;
import java.security.*;
import java.util.zip.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class ReleaseCreator 
{
    List<String> filesListInDir = new ArrayList<String>();
    private boolean encrypt = false;
    //private List<String> fileList = new ArrayList<>();

    private String pw, key;

    Console console=System.console();
    public static void main(String[] Args)throws Exception
    {
        new ReleaseCreator().logic();       
    }

    private void logic()throws Exception
    {
        System.gc();

        if(new File("../Releases").exists() == false)
            new File("../Releases").mkdir();

        while(isTestBuild() == false);
        //cleanup
        createBuild();
        checksumBuild();

        System.gc();
    }

    private boolean isTestBuild()throws Exception
    {
        String a = console.readLine("Is this a test build? [Y/N]    ");
        if(a.equalsIgnoreCase("Y"))
            encrypt = true;
        else if(a.equalsIgnoreCase("N"))
            encrypt = false;
        else
            return false;
        return true;
    }

   
    private void createBuild()throws Exception
    {
        File dir = new File("./");
        String zipDirName = "../Releases/Truncheon.zip";

        zipDirectory(dir, zipDirName);
        

        if(encrypt == true)
        {
            while(getPassword() == false);
            while(getKey() == false);

            pw =  hashString(pw + key,  "SHA3-256");
            key = "";

            encryptBuild();

            new File("../Releases/Truncheon.zip").delete();
        }

        checksumBuild();
    }

    private void zipDirectory(File dir, String zipDirName) {
        try {
            populateFilesList(dir);
            //now zip files one by one
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipDirName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for(String filePath : filesListInDir){
                System.out.println("Zipping : "+filePath);
                //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
                zos.putNextEntry(ze);
                //read the file and write to ZipOutputStream
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else populateFilesList(file);
        }
    }


    private void encryptBuild()throws Exception
    {
        String curDir = System.getProperty("user.dir");
		String newDir=curDir+"/";
		//File to be encrypted
		FileInputStream inFile = new FileInputStream("../Releases/Truncheon.zip");
		//Encrypted Output of the fractal file
		FileOutputStream outFile = new FileOutputStream("../Releases/Truncheon.LOCK");
		/* Description:
		* Password, iv and salt should be transferred to the other end securely
		* salt is for encoding, written to file and must be transferred to the recipient securely for decryption
		*/
		byte[] salt = new byte[8];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt);
		FileOutputStream saltOutFile = new FileOutputStream("../Releases/salt.enc");
		saltOutFile.write(salt);
		saltOutFile.close();
		SecretKeyFactory factory = SecretKeyFactory
		.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keySpec = new PBEKeySpec(pw.toCharArray(), salt, 65536,
		256);
		SecretKey secretKey = factory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();
		/*Description
		* iv adds randomness to the text and just makes the mechanism more secure
		* used while initializing the cipher
		*/
		FileOutputStream ivOutFile = new FileOutputStream(newDir+"../Releases/iv.enc");
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		ivOutFile.write(iv);
		ivOutFile.close();

		//file encryption
		byte[] input = new byte[64];
		int bytesRead;

		while ((bytesRead = inFile.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, bytesRead);
			if (output != null)
			outFile.write(output);
		}

		byte[] output = cipher.doFinal();
		if (output != null)
		outFile.write(output);

		inFile.close();
		outFile.flush();
		outFile.close();

        System.gc();
        
    }

    private boolean getPassword()throws Exception
    {
        pw=String.valueOf(console.readPassword("Build Password: "));
        String cpw = String.valueOf(console.readPassword("Confirm Password: "));
        if(cpw.equals(pw) == true)
            return true;
        return false;
    }

    private boolean getKey()throws Exception
    {
        key=String.valueOf(console.readPassword("Build Key: "));
        String ckey = String.valueOf(console.readPassword("Confirm Key: "));
        if(ckey.equals(key) == true)
            return true;
        return false;
    }

    private void checksumBuild()throws Exception
    {
        if(encrypt ==  true)
        {
            new File("../Releases/Truncheon.LOCK_" + hashFile(new File("../Releases/Truncheon.LOCK"), "MD5") + ".md5").createNewFile();
            new File("../Releases/salt.enc_" + hashFile(new File("../Releases/salt.enc"), "MD5") + ".md5").createNewFile();
            new File("../Releases/iv.enc_" + hashFile(new File("../Releases/iv.enc"), "MD5") + ".md5").createNewFile();


            new File("../Releases/Truncheon.LOCK_"  + hashFile(new File("../Releases/Truncheon.LOCK"), "SHA3-256") +".sha").createNewFile();
            new File("../Releases/salt.enc_" + hashFile(new File("../Releases/salt.enc"), "SHA3-256") +".sha").createNewFile();
            new File("../Releases/iv.enc_" + hashFile(new File("../Releases/iv.enc"), "SHA3-256") +".sha").createNewFile();

            return;
        }

        String BuildHashMD5 = hashFile(new File("../Releases/Truncheon.zip"), "MD5");
        String BuildHashSHA = hashFile(new File("../Releases/Truncheon.zip"), "SHA3-256");

        new File("../Releases/Truncheon.zip_" + BuildHashMD5+".md5").createNewFile();
        new File("../Releases/Truncheon.zip_" + BuildHashSHA+".sha").createNewFile();
    }



    private void checksumFiles()throws Exception
    {
        //Work in progress
    }

    private final String convertByteArrayToHexString(byte[] arrayBytes)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++)
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        return stringBuffer.toString();
    }

    private final String hashFile(File file, String algorithm)throws Exception
    {
        try (FileInputStream inputStream = new FileInputStream(file))
        {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1)
                digest.update(bytesBuffer, 0, bytesRead);

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        }
        catch (NoSuchAlgorithmException E)
        {
            
        }
        return null;
    }

    private final String hashString(String message, String algorithm)throws Exception
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException E)
        {
        }
        return null;
    }
}
