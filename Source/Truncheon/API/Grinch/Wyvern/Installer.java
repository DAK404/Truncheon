package Truncheon.API.Grinch.Wyvern;

//Import the required Java IO classes
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//Import the required Java Util classes
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import Truncheon.API.Wraith.WraithEdit;

/**
*
*/
final class Installer
{
    List <String> fileList;
    private String curDir = System.getProperty("user.dir");
    private final String INPUT_ZIP_FILE = curDir + "/Update.zip";
    private final String OUTPUT_FOLDER = curDir;

    /**
    *
    * @return
    */
    protected boolean installPackage()
    {
        WraithEdit.logger("--- UPDATE LOG START ---", "/Update");
        WraithEdit.logger("Program Update Requested.", "/Update");
        WraithEdit.logger("Attempting to install the the update file...", "/Update");
        return new Truncheon.API.Grinch.Wyvern.Installer().installPackageLogic(INPUT_ZIP_FILE, OUTPUT_FOLDER);
    }

    /**
    *
    * @param zipFile
    * @param outputFolder
    * @return
    */
    private final boolean installPackageLogic(String zipFile, String outputFolder)
    {
        boolean status = false;
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

                Truncheon.API.IOStreams.printInfo("Installed - " + newFile.getAbsoluteFile());
                WraithEdit.logger("Installed : " + newFile.getAbsoluteFile(), "/Update");

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
            WraithEdit.logger("[ ATTENTION ] : UPDATE INSTALL SUCCESSFUL!", "/Update");
            status = true;
        }
        catch (Exception ex)
        {
            WraithEdit.logger("Update Failed! Installation Error.", "/Update");            
        }
        WraithEdit.logger("--- UPDATE LOG END ---", "/Update");
        System.gc();
        return status;
    }
}
