package terfusti.mcextract.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CustomizeSkinScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.screen.ModListScreen;
import net.minecraftforge.fml.common.Mod;
import terfusti.mcextract.Extractor;

import java.io.FileNotFoundException;


@Mod.EventBusSubscriber(modid = "mcextract", value = Dist.CLIENT)
public class TitleScreen {


    @SubscribeEvent
    public static void onRender(GuiScreenEvent.InitGuiEvent event) {
        final Screen gui = event.getGui();
        if (gui instanceof CustomizeSkinScreen) {

            int x = Minecraft.getInstance().currentScreen.width;
            int y = Minecraft.getInstance().currentScreen.height;


            event.addWidget(new Button(x / 2 - 50, y / 6 + (24*5), 100, 20, new StringTextComponent( TextFormatting.GREEN + "Export Skins"), (button) -> {
                try {
                    ExportToast toast = new ExportToast();
                    Extractor extractor = new Extractor(toast);
                    extractor.main();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

}
