//package sardari.utils;
//
//import android.os.Environment;
//
//import java.io.File;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class AppLogger {
//    public static org.apache.log4j.Logger getLogger(Class clazz) {
//        final LogConfigurator logConfigurator = new LogConfigurator();
//        logConfigurator.setFileName(Environment.getExternalStorageDirectory().toString() + File.separator + "log/file.log");
//        logConfigurator.setRootLevel(Level.ALL);
//        logConfigurator.setLevel("org.apache", Level.ALL);
//        logConfigurator.setUseFileAppender(true);
//        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
//        logConfigurator.setMaxFileSize(1024 * 1024 * 5);
//        logConfigurator.setImmediateFlush(true);
//        logConfigurator.configure();
//        Logger log = Logger.getLogger(clazz);
//        return log;
//    }
//}
