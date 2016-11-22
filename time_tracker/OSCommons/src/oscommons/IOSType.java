package oscommons;

import java.util.List;

/**
 * Created by mihai on 8/27/2016.
 */
public interface IOSType {

    void startApplication(String filename, List<String> params);

    boolean executeCommandsFromScript(String filename,
                                      List<String> params,
                                      boolean waitFor,
                                      boolean isOutputEnabled);

    boolean shutdownApplication(String filename, String mainApplicationName);

    String getActiveApplicationName(String scriptPath, List<String> params);
    String getActiveWindowTitle(String scriptPath, List<String> params);

    String getActiveURL(String scriptPath, List<String> params);

    String executeCommandsFromScriptAndPrintOutput(String filename,
                                                          List<String> params);

    String getUserIdleTime(String scriptPath, String outputPath, String outputFilename);

    void shutdownSystem();


}
