import java.io.*;
import java.util.*;

public class ReleaseCreator 
{
    private boolean encrypt = false;

    
    public static void main(String[] Args)throws Exception
    {
        new ReleaseCreator().logic();       
    }

    private void logic()
    {
        Console console=System.console();
        while(isTestBuild() == false);

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

    private void createRelease(String parentDirectoryName)throws Exception
    {
        File fileToZip = new File("./");
        FileOutputStream fos = new FileOutputStream("./Truncheon.zip");
        ZipOutputStream zos = new ZipOutputStream(fos);
        if (fileToZip == null || !fileToZip.exists()) 
        {
            return;
        }

        String zipEntryName = fileToZip.getName();
        if (parentDirectoryName!=null && !parentDirectoryName.isEmpty()) 
        {
            zipEntryName = parentDirectoryName + "/" + fileToZip.getName();
        }

        if (fileToZip.isDirectory()) 
        {
            System.out.println("+" + zipEntryName);
            for (File file : fileToZip.listFiles()) {
                createRelease(zos, file, zipEntryName);
            }
        } 
        else 
        {
            System.out.println("   " + zipEntryName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(fileToZip);
            zos.putNextEntry(new ZipEntry(zipEntryName));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
    }

    private void checksumBuild()throws Exception
    {
        String BuildHashMD5 = hashFile(new File("./Truncheon.zip"), "MD5");
        String BuildHashSHA = hashFile(new File("./Truncheon.zip"), "SHA3-256");
    }

    private void checksumFiles()throws Exception
    {

    }

    private void checksumAlgorithms()throws Exception
    {
        
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
}
