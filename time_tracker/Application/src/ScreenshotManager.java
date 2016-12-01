import org.apache.log4j.Logger;
import oscommons.IOSType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by admin on 11/22/16.
 */
public class ScreenshotManager implements Runnable {

    private AppConfig configuration = null;
    private static long timeWhenNoClickWasReceived = 0;

    public static volatile boolean isStopped;

    private final static Logger logger = Logger.getLogger(ScreenshotManager.class);

    public ScreenshotManager(AppConfig configuration)
    {
        this.configuration = configuration;
        isStopped = false;
    }

    public void run()
    {

        while(!isStopped)
        {

            long startTime = System.currentTimeMillis();

            try
            {
                takeScreenshot(this.configuration.getImagesLocalRootFolder());
            }
            catch(Exception e)
            {
                System.out.println("Exception: "  + e);
                logger.error("Exception: ",e);
            }

            try
            {
                Thread.sleep(3000);
            }
            catch(Exception e)
            {
                System.out.println("Exception: "  + e);
                logger.error("Exception: ",e);
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            System.out.println(elapsedTime);
        }
    }


    public static byte[] takeScreenshot(String localFolder)
    {
        byte[] screenshotInBytes = null;
        //make if not already exists the root images folder
        File current_date_folder =
                new File(localFolder);

        if(!current_date_folder.exists())
        {
            current_date_folder.mkdir();
        }

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String screenshotName = "screenshot" + "_" +
                (dateFormat.format(date)).toString() + ".jpg";

        String localFilePath = current_date_folder.getAbsolutePath() + "/" + screenshotName;

        //make a screenshot and save it at the local path with screenshot name
        BufferedImage image = takeScreenshotAndCompress(current_date_folder.getAbsolutePath(),
                screenshotName);
        screenshotInBytes = imageToByte(image);

        return screenshotInBytes;
    }

    public static BufferedImage takeScreenshotAndCompress(String rootPath,
                                          String screenshotName)
    {
        BufferedImage image = null;

        try
        {
            //take screenshot
            image = new Robot().createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                //compress taken screenshot
                CompressJPEGFIle.compress(image,rootPath,screenshotName);

        }
        catch(Exception ex)
        {
            logger.error("Exception in MainApplication->takeScreenshotAndCompress -> ",ex);
        }

        return image;
    }



    public static byte[] imageToByte(BufferedImage image)
    {
        byte[] result = null;
        try{

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "jpg", baos );
            baos.flush();
            result = baos.toByteArray();
            baos.close();

        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        return result;
    }


    public static void stop()
    {
        isStopped = true;
    }

}


