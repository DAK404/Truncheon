import java.io.*;
import java.util.*;
import java.security.*;
import java.util.zip.*;

public class ReleaseCreator 
{
    private boolean encrypt = false;
    private List<String> fileList = new ArrayList<>();

    Console console=System.console();
    public static void main(String[] Args)throws Exception
    {
        new ReleaseCreator().logic();       
    }

    private void logic()throws Exception
    {
        while(isTestBuild() == false);
        //cleanup
        compressDirectory("./", "./Truncheon.zip");
        checksumBuild();
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

    private void compressDirectory(String dir, String zipFile)throws Exception
    {
        File directory = new File(dir);
        getFileList(directory);

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) 
        {
            for (String filePath : fileList) {
                System.out.println("Compressing: " + filePath);

                

                // Creates a zip entry.
                String name = filePath.substring(
                    directory.getAbsolutePath().length() + 1,
                    filePath.length());

                ZipEntry zipEntry = new ZipEntry(name);
                zos.putNextEntry(zipEntry);

                // Read file content and write to zip output stream.
                try (FileInputStream fis = new FileInputStream(filePath)) 
                {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) 
                        zos.write(buffer, 0, length);

                    // Close the zip entry.
                    zos.closeEntry();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private void getFileList(File directory)
    { 
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) 
        {
            for (File file : files)
            {
                if (file.isFile()) 
                    fileList.add(file.getAbsolutePath());
                else
                    getFileList(file);
            }
        }
    }


    private void checksumBuild()throws Exception
    {
        String BuildHashMD5 = hashFile(new File("./Truncheon.zip"), "MD5");
        String BuildHashSHA = hashFile(new File("./Truncheon.zip"), "SHA3-256");

        new File(BuildHashMD5+".md5").createNewFile();
        new File(BuildHashSHA+".sha").createNewFile();
    }

    private void checksumFiles()throws Exception
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
                digest.update(bytesBuffer, 0, bytesRead);

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        }
        catch (NoSuchAlgorithmException E)
        {
            
        }
        return null;
    }
}
