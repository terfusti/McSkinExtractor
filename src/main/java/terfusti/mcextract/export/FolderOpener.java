package terfusti.mcextract.export;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

public final class FolderOpener {
    private static final Logger LOGGER = LogManager.getLogger();

    private FolderOpener() { }

    public static boolean open(Path folder) {
        Path absoluteFolder = folder.toAbsolutePath().normalize();
        if (openWithDesktop(absoluteFolder)) return true;
        return openWithPlatformCommand(absoluteFolder);
    }

    private static boolean openWithDesktop(Path folder) {
        if (!Desktop.isDesktopSupported()) return false;
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) return false;
        try {
            desktop.open(folder.toFile());
            return true;
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to open export folder with Desktop API: {}", folder, ex);
            return false;
        }
    }

    private static boolean openWithPlatformCommand(Path folder) {
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        String command;
        if (osName.contains("win")) {
            command = "explorer";
        } else if (osName.contains("mac")) {
            command = "open";
        } else {
            command = "xdg-open";
        }

        try {
            new ProcessBuilder(command, folder.toString()).start();
            return true;
        } catch (IOException | RuntimeException ex) {
            LOGGER.warn("Unable to open export folder with {}: {}", command, folder, ex);
            return false;
        }
    }
}
