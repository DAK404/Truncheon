package Truncheon.API.Minotaur;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HAlgos
{

    // ------------------------------------------------------------------------------------ //
    //                                  PUBLIC API METHODS                                  //
    // ------------------------------------------------------------------------------------ //

    public final String stringToMD5(String input)throws Exception
    {
        return hashString(input, "MD5");
    }

    public final String stringToSHA3_256(String input) throws Exception
    {
        return hashString(input, "SHA3-256");
    }

    public final String fileToMD5(String fileName) throws Exception
    {
        return hashFile(new File(fileName), "MD5");
    }

    public final String fileToSHA3_256(String fileName) throws Exception
    {
        return hashFile(new File(fileName), "SHA3-256");
    }

    // ------------------------------------------------------------------------------------ //
    //                                   API BACKEND CODE                                   //
    // ------------------------------------------------------------------------------------ //


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
            {
            digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        }
        catch (NoSuchAlgorithmException E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
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
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return null;
    }
}