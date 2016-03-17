
package utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class PerformanceLogger
{
    public Logger logMessage() {  
        File yourFile = new File( System.getProperty("user.dir") + "/MyLogFile.log" );
        Logger logger = Logger.getLogger("chillMaster");
        if(!yourFile.exists()) {
            try
            {
                yourFile.createNewFile();
            }
            catch (IOException ex)
            {
                Logger.getLogger(PerformanceLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        FileHandler fh;  

        try {  

            // This block configure the logger with handler and formatter 
             /*
            * The below line is the syntax for the file handler which has the capability of 
            * appending the logs in the file. The second argument decides the appending.
            * FileHandler fileTxt = new FileHandler("eLog.txt", true);
            */
            fh = new FileHandler(System.getProperty("user.dir") + "/MyLogFile.log", true);  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter(); 
            fh.setFormatter(formatter);  

        } catch (SecurityException | IOException e) { 
            System.out.println("SecurityException or IOException while trying to create and add a handler to the logger :  " + e );
            Logger.getLogger(PerformanceLogger.class.getName()).log(Level.SEVERE, null, e);
        }
        logger.info("\n***** NEW SESSION *****\n");
        logger.info(logger.getName() + " Logger started!");
        return logger;

    }
}
