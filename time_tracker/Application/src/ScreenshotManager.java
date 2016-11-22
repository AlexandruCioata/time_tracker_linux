import org.apache.log4j.Logger;
import oscommons.IOSType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        List<String> localFiles = new ArrayList<String>();

        int index = 1;

        while(!isStopped)
        {

            long startTime = System.currentTimeMillis();
            try
            {

                //make if not already exists the root images folder
                File current_date_folder =
                        new File(this.configuration.getImagesLocalRootFolder());

                if(!current_date_folder.exists())
                {
                    current_date_folder.mkdir();
                }

                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String screenshotName = "screenshot" + "_" + index + "_" +
                        (dateFormat.format(date)).toString() + ".jpg";

                String localFilePath = current_date_folder.getAbsolutePath() + "/" + screenshotName;
                localFiles.add(localFilePath);

                //make a screenshot and save it at the local path with screenshot name
                takeScreenshotAndCompress(current_date_folder.getAbsolutePath(),screenshotName);

                index++;
                Thread.sleep(3000);
            }
            catch(Exception e)
            {
                System.out.println("Exceptie: "  + e);
                logger.error("Exception in MainApplication->run->(While(true)) -> ",e);
            }

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            System.out.println(elapsedTime);

/*            //compute the time with no interaction with the pc
            timeWhenNoClickWasReceived = timeWhenNoClickWasReceived *1000 + elapsedTime;
            timeWhenNoClickWasReceived = timeWhenNoClickWasReceived/1000;*/
        }
    }

    public void takeScreenshotAndCompress(String rootPath,
                                          String screenshotName)
    {

        try
        {
            //take screenshot
            BufferedImage image = new Robot().createScreenCapture(
                    new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

            if(timeWhenNoClickWasReceived <
                    this.configuration.getInteractionWithPCTimeOut())
            {
                //compress taken screenshot
                CompressJPEGFIle.compress(image,rootPath,screenshotName);

                //crop image and write it on disk
                BufferedImage bufferedImage = ImageIO.read(
                        Files.newInputStream(Paths.get(rootPath + "/" + screenshotName)));

                String[] tokens = screenshotName.split("\\.(?=[^\\.]+$)");
                CropImage.cropImage(bufferedImage,
                        new Rectangle(0, 0, bufferedImage.getWidth(), 130),
                        rootPath, tokens[0] + "_cropped.jpg");
            }
            else
            {
                logger.info("Inactivity! -> no screenshots taken any more: " +
                        "timeWhenNoClickWasReceived= " +
                        timeWhenNoClickWasReceived);
            }
        }
        catch(Exception ex)
        {
            logger.error("Exception in MainApplication->takeScreenshotAndCompress -> ",ex);
        }

    }

    public static void stop()
    {
        isStopped = true;
    }

}


