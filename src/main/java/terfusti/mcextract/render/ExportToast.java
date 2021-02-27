package terfusti.mcextract.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import terfusti.mcextract.Extractor;

public class ExportToast implements IToast {

    private Visibility visibility = Visibility.SHOW;


    // Draw function
    @Override
    public Visibility func_230444_a_(MatrixStack matrixStack, ToastGui toastGui, long p_230444_3_) {

        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        toastGui.blit(matrixStack, 0, 0, 0, 0, this.func_230445_a_(), this.func_238540_d_());

        toastGui.getMinecraft().fontRenderer.drawString(matrixStack, "Exported to", 30, 7, 0xD8E45A);
        toastGui.getMinecraft().fontRenderer.drawString(matrixStack,  "\\.minecraft" + "\\skins\\", 30, 17, 0xD8E45A);


        toastGui.getMinecraft().getItemRenderer().renderItemAndEffectIntoGuiWithoutEntity(new ItemStack(Items.EXPERIENCE_BOTTLE), 8, 8);
        return p_230444_3_ >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }


}
