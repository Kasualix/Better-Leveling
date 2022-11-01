package darkorg.betterleveling.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import darkorg.betterleveling.api.ISpecialization;
import darkorg.betterleveling.gui.widget.ChooseSpecButton;
import darkorg.betterleveling.network.NetworkHandler;
import darkorg.betterleveling.network.packets.AddSpecC2SPacket;
import darkorg.betterleveling.registry.SpecRegistry;
import darkorg.betterleveling.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import javax.annotation.Nonnull;

import static darkorg.betterleveling.network.chat.ModTextComponents.*;

@OnlyIn(Dist.CLIENT)
public class ChooseSpecScreen extends Screen {

    private int leftPos, topPos;
    private ISpecialization playerSpecialization;
    private final int imageWidth = 176;
    private final int imageHeight = 166;

    public ChooseSpecScreen() {
        super(GUI_CHOOSE);
        this.playerSpecialization = SpecRegistry.getSpecRegistry().get(0);
    }

    @Override
    protected void init() {
        this.leftPos = (width - imageWidth) / 2;
        this.topPos = (height - imageHeight) / 2;

        ChooseSpecButton chooseSpecButton = new ChooseSpecButton((this.width - 64) / 2, (this.height - 64) / 2 - 32, this.playerSpecialization, this::onValueChange);
        addButton(chooseSpecButton);

        ExtendedButton selectButton = new ExtendedButton((this.width - 75) / 2, this.topPos + 116, 75, 25, SELECT_BUTTON, pButton -> this.onPress());
        addButton(selectButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@Nonnull MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pMatrixStack);
        drawCenteredString(pMatrixStack, font, title, (width / 2), this.topPos - 10, 16777215);
        RenderUtil.setShaderTexture();
        blit(pMatrixStack, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTick);
    }

    private void onPress() {
        Minecraft.getInstance().displayGuiScreen(new ConfirmScreen(this::onCallback, this.playerSpecialization.getTranslation(), CHOOSE_CONFIRM));
    }

    private void onCallback(boolean pCallback) {
        if (pCallback) {
            NetworkHandler.sendToServer(new AddSpecC2SPacket(new Pair<>(this.playerSpecialization, true)));
            Minecraft.getInstance().popGuiLayer();
        } else {
            Minecraft.getInstance().displayGuiScreen(this);
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    private void onValueChange(ISpecialization pPlayerSpec) {
        this.playerSpecialization = pPlayerSpec;
    }
}
