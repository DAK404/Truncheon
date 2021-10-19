package Truncheon.API.Wyvern;

//Import the required Java IO classes
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//Import the required Java Util classes
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
*
*/
final class UpdateInstaller
{
    List <String> fileList;
    private String curDir = System.getProperty("user.dir");
    private final String INPUT_ZIP_FILE = curDir + "/Update.zip";
    private final String OUTPUT_FOLDER = curDir;

    /**
    *
    * @return
    */
    protected boolean install()
    {
        new Truncheon.API.Wraith.WriteFile().logToFile("--- UPDATE LOG START ---", "Logs/Update");
        new Truncheon.API.Wraith.WriteFile().logToFile("Program Update Requested.", "Logs/Update");
        new Truncheon.API.Wraith.WriteFile().logToFile("Attempting to install the the update file...", "Logs/Update");
        return new Truncheon.API.Wyvern.UpdateInstaller().unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
    }

    /**
    *
    * @param zipFile
    * @param outputFolder
    * @return
    */
    private final boolean unZipIt(String zipFile, String outputFolder)
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

                System.out.println("[INFO] Installed : " + newFile.getAbsoluteFile());
                new Truncheon.API.Wraith.WriteFile().logToFile("Installed : " + newFile.getAbsoluteFile(), "Logs/Update");

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
            new Truncheon.API.Wraith.WriteFile().logToFile("[ ATTENTION ] : UPDATE INSTALL SUCCESSFUL!", "Logs/Update");
            new Truncheon.API.Wraith.WriteFile().logToFile("--- UPDATE LOG END ---", "Logs/Update");
            return true;
        }
        catch (Exception ex)
        {
            new Truncheon.API.Wraith.WriteFile().logToFile("[ ATTENTION ] : UPDATE INSTALL FAILED!", "Logs/Update");
            new Truncheon.API.Wraith.WriteFile().logToFile("--- UPDATE LOG END ---", "Logs/Update");
            return false;
        }
    }
}