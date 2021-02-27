package terfusti.mcextract;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import terfusti.mcextract.render.ExportToast;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.Set;


public class Extractor {


    private ExportToast toast;

    public Extractor(ExportToast toast) {
        this.toast = toast;
    }

    public void main() throws FileNotFoundException {

        // Sets up the location of folder for the skins to be exported to.
        File location = Minecraft.getInstance().gameDir;
        String skinFolder = location + "\\" + "skins";

        createFolder(skinFolder);
        exportSkins(skinFolder);
    }


    private void exportSkins(String skinFolder) throws FileNotFoundException {

        // Gets the skinFolder of the json container the skins
        File skinJson = new File(Minecraft.getInstance().gameDir + "\\launcher_skins.json");

        Gson gson = new Gson();

        Map json = gson.fromJson(new FileReader(skinJson), Map.class);
        Set keySet = json.keySet();

        keySet.iterator().forEachRemaining( (n) -> {
            Map skinMap = (Map) json.get(n);

            // For each skin id it creates a folder
            String path = skinFolder + "\\" + getFixedName(((String) skinMap.get("name")));
            createFolder(path);

            // Gets the encoded skin and model image
            String skinEncoded = extractEncode(((String) skinMap.get("skinImage")));
            String modelEncoded = extractEncode(((String) skinMap.get("modelImage")));

            // Sets up the extra information txt file
            File extra = new File(path + "\\info.txt");
            String data = "name : " + skinMap.get("name") + "\n" +
                    "created : " + skinMap.get("created") + "\n" +
                    "updated : " + skinMap.get("updated") + "\n" +
                    "id : " + skinMap.get("id") + "\n" +
                    "slim : " + skinMap.get("slim") + "\n";

            try {
                BufferedImage skinImage = decodeImage(skinEncoded);
                BufferedImage modelImage = decodeImage(modelEncoded);

                File skinFile = new File(path + "\\" + getFixedName((String) skinMap.get("name")) + ".png");
                File modelFile = new File(path + "\\" + getFixedName((String) skinMap.get("name")) + "_model.png");

                ImageIO.write(skinImage, "png", skinFile);
                ImageIO.write(modelImage, "png", modelFile);


                extra.createNewFile();
                FileWriter writer = new FileWriter(extra);
                writer.write(data);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        Minecraft.getInstance().getToastGui().add(toast);
        Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f));

    }

    private BufferedImage decodeImage(String encoded) throws IOException {

        byte[] imageByte = Base64.getDecoder().decode(encoded);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        BufferedImage image = ImageIO.read(bis);
        bis.close();

        return image;
    }

    public static String getFixedName(String name) {

        name = name.replace("\\", "");
        name = name.replace("/", "");
        name = name.replace(":", "");
        name = name.replace("*", "");
        name = name.replace("?", "");
        name = name.replace("\"", "");
        name = name.replace("<", "");
        name = name.replace(">", "");
        name = name.replace("|", "");

        return name;
    }


    private void createFolder(String location) {
        if (!Files.exists(Paths.get(location))) {
            File file = new File(location);
            file.mkdir();
        }
    }

    private String extractEncode(String base) {
        int index = base.indexOf("base64,") + 7;
        return base.substring(index);
    }





}
