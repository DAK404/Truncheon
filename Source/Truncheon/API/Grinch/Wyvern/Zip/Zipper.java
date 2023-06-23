package Truncheon.API.Grinch.Wyvern.Zip;

import java.io.*;
import java.util.zip.*;

public class ZipDirectory
{
    public void zipFiles(String fileName, String targetDirectory)
    {
        FileOutputStream fos = new FileOutputStream(targetDirectory);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File fileToZip = new File(fileName);
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException
    {
        if (fileToZip.isHidden())
        {
            return;
        }
        if (fileToZip.isDirectory())
        {
            if (fileName.endsWith("/"))
            {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0)
        {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}