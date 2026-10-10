package terfusti.mcextract.export;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

/** Exports the legacy Minecraft Launcher's launcher_skins.json skinImage/modelImage data. */
public final class SkinExporter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<String> RESERVED = Set.of("CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9");
    private SkinExporter() { }

    public static ExportResult exportFromGameDirectory(Path gameDirectory) {
        Path input = gameDirectory.resolve("launcher_skins.json");
        Path output = gameDirectory.resolve("skins");
        if (!Files.isRegularFile(input)) {
            LOGGER.error("Launcher skin data not found: {}", input);
            return new ExportResult(0, 0, "Missing launcher_skins.json", output);
        }
        int exported = 0;
        int failed = 0;
        try (Reader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonObject()) throw new IOException("Root is not a JSON object");
            Files.createDirectories(output);
            for (Map.Entry<String, JsonElement> entry : parsed.getAsJsonObject().entrySet()) {
                try {
                    if (!entry.getValue().isJsonObject()) throw new IOException("Entry is not an object");
                    JsonObject skin = entry.getValue().getAsJsonObject();
                    String name = getString(skin, "name", entry.getKey());
                    String id = getString(skin, "id", entry.getKey());
                    String directoryName = safeName(name) + "_" + safeName(id);
                    Path folder = output.resolve(directoryName);
                    Files.createDirectories(folder);
                    // Both image properties are data URIs containing Base64 PNGs in the legacy launcher format.
                    writeImage(skin, "skinImage", folder.resolve(safeName(name) + ".png"));
                    writeImage(skin, "modelImage", folder.resolve(safeName(name) + "_model.png"));
                    String info = "name : " + name + "\ncreated : " + getString(skin, "created", "")
                            + "\nupdated : " + getString(skin, "updated", "")
                            + "\nid : " + id + "\nslim : " + getString(skin, "slim", "") + "\n";
                    Files.writeString(folder.resolve("info.txt"), info, StandardCharsets.UTF_8);
                    exported++;
                } catch (Exception ex) {
                    failed++;
                    LOGGER.warn("Unable to export skin entry {}", entry.getKey(), ex);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to read or export launcher skin data from {}", input, ex);
            return new ExportResult(exported, failed, ex.getMessage(), output);
        }
        LOGGER.info("Export complete: {} saved, {} failed in {}", exported, failed, output);
        return new ExportResult(exported, failed, null, output);
    }

    private static void writeImage(JsonObject obj, String property, Path path) throws IOException {
        String raw = getString(obj, property, null);
        if (raw == null || raw.isBlank()) throw new IOException("Missing " + property);
        int marker = raw.indexOf("base64,");
        if (marker < 0) throw new IOException("Invalid data URI in " + property);
        byte[] data;
        try { data = Base64.getDecoder().decode(raw.substring(marker + 7)); }
        catch (IllegalArgumentException ex) { throw new IOException("Invalid Base64 in " + property, ex); }
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
        if (image == null) throw new IOException("Unsupported image in " + property);
        if (!ImageIO.write(image, "png", path.toFile())) throw new IOException("PNG writer unavailable");
    }

    private static String getString(JsonObject obj, String property, String fallback) {
        JsonElement value = obj.get(property);
        return value != null && !value.isJsonNull() && value.isJsonPrimitive() ? value.getAsString() : fallback;
    }

    private static String safeName(String value) {
        String sanitized = (value == null ? "unnamed" : value)
                .replaceAll("[\\\\/:*?\"<>|\\p{Cntrl}]", "_")
                .replaceAll("[. ]+$", "").trim();
        if (sanitized.isBlank()) sanitized = "unnamed";
        if (RESERVED.contains(sanitized.toUpperCase(java.util.Locale.ROOT))) sanitized = "_" + sanitized;
        return sanitized.length() > 90 ? sanitized.substring(0, 90) : sanitized;
    }
}
