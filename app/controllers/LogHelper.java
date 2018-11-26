package controllers;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogHelper {
    static private FileHandler fileTxt;
        static public void setup()
                throws java.io.IOException
        {
            Logger logger = Logger.getLogger("testLogger");
            fileTxt = new FileHandler("Logging.txt");
        }
}
