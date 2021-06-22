package Truncheon.API.Wyvern;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

final class UpdateInstaller
{
    List <String> fileList;
    private String curDir = System.getProperty("user.dir");
    private final String INPUT_ZIP_FILE = curDir + "/Update.zip";
    private final String OUTPUT_FOLDER = curDir;

    protected boolean install() 
    {
        return new Truncheon.API.Wyvern.UpdateInstaller().unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
    }

    private boolean unZipIt(String zipFile, String outputFolder)
    {
        byte[] buffer = new byte[1024];
        try
        {
            //create output directory is not exists
            File folder = new File(OUTPUT_FOLDER);
            if (!folder.exists()) 
                folder.mkdir();

            System.gc();
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            while (ze != null)
            {
                if (ze.isDirectory())
                {
                    ze = zis.getNextEntry();
                    continue;
                }
                
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                
                if (newFile.exists()) 
                {
                    newFile.delete();
                    continue;
                }

                System.out.println("[INFO] Installing : " + newFile.getAbsoluteFile()+"\r");
                
                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                
                int len;
                while ((len = zis.read(buffer)) > 0) 
                    fos.write(buffer, 0, len);

                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            new File(INPUT_ZIP_FILE).delete();
            System.gc();
            System.out.println("Install Successful.");
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}