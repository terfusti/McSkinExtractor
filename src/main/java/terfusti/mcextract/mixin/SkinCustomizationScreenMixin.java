package terfusti.mcextract.mixin;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import terfusti.mcextract.export.ExportResult;
import terfusti.mcextract.export.SkinExporter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mixin(SkinCustomizationScreen.class)
public abstract class SkinCustomizationScreenMixin {

    @ModifyArg(
            method = "addOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall(Ljava/util/List;)V"
            ),
            index = 0
    )
    private List<AbstractWidget> mcextract$addExportButton(List<AbstractWidget> original) {
        List<AbstractWidget> widgets = new ArrayList<>(original);
        Button exportButton = Button.builder(
                Component.translatable("mcextract.export"),
                clicked -> {
                        Path gameDirectory = Path.of("")
                                .toAbsolutePath()
                                .normalize();

                        ExportResult result =
                                SkinExporter.exportFromGameDirectory(gameDirectory);

                        clicked.setMessage(Component.literal(result.summary()));
                    }
        ).build();

        widgets.add(exportButton);
        return widgets;
    }
}
