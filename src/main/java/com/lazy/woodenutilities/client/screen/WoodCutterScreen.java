package com.lazy.woodenutilities.client.screen;

import com.lazy.woodenutilities.Configs;
import com.lazy.woodenutilities.client.widget.SlotWidget;
import com.lazy.woodenutilities.inventory.containers.WoodCutterContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class WoodCutterScreen extends ContainerScreen<WoodCutterContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("woodenutilities", "textures/gui/container/woodcutter.png");
    private float sliderProgress;
    private boolean clickedOnScroll;
    private int recipeIndexOffset;
    private boolean hasItemsInInputSlot;

    public WoodCutterScreen(WoodCutterContainer containerIn, PlayerInventory playerInv, ITextComponent titleIn) {
        super(containerIn, playerInv, titleIn);
        containerIn.setInventoryUpdateListener(this::onInventoryUpdate);
    }

    @Override
    protected void func_231160_c_() { //init
        super.func_231160_c_();
        if (Configs.NEED_AXE.get())
            this.func_230480_a_(new SlotWidget(this.guiLeft + 17, this.guiTop + 47)); //addButton
    }

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) { //render
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY); //renderHoveredToolTip
        if (this.hoveredSlot == this.container.axeSlot) {
            if (this.container.axeSlot.getStack().isEmpty()) {
                //renderTooltip
                this.func_243308_b(matrixStack, Collections.singletonList(new StringTextComponent("Needs an axe with the minimum tier being " + ItemTier.values()[Configs.MINIMUM_AXE_TIER.get()].name())), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) { //drawGuiContainerBackgroundLayer
        this.func_230446_a_(stack); //renderBackground
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.func_238474_b_(stack, i, j, 0, 0, this.xSize, this.ySize); //blit
        int k = (int) (41.0F * this.sliderProgress);
        this.func_238474_b_(stack, i + 119, j + 15 + k, 176 + (this.canScroll() ? 0 : 12), 0, 12, 15);
        int l = this.guiLeft + 48;
        int i1 = this.guiTop + 14;
        int j1 = this.recipeIndexOffset + 12;
        this.drawRecipesBackground(stack, mouseX, mouseY, l, i1, j1);
        this.drawRecipesItems(l, i1, j1);
    }

    private void drawRecipesBackground(MatrixStack stack, int mouseX, int mouseY, int left, int top, int recipeIndexOffsetMax) {
        for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getRecipeListSize(); ++i) {
            int j = i - this.recipeIndexOffset;
            int k = left + j % 4 * 17;
            int l = j / 4;
            int i1 = top + l * 19 + 2;
            int j1 = this.ySize;
            if (i == this.container.getSelectedRecipe()) {
                j1 += 19;
            } else if (mouseX >= k && mouseY >= i1 && mouseX < k + 17 && mouseY < i1 + 19) {
                j1 += 38;
            }

            this.func_238474_b_(stack, k, i1 - 1, 0, j1, 17, 19);
        }

    }

    private void drawRecipesItems(int left, int top, int recipeIndexOffsetMax) {
        RenderHelper.enableStandardItemLighting();
        List<Item> list = this.container.getRecipeList();

        if (this.field_230706_i_ != null) {
            for (int i = this.recipeIndexOffset; i < recipeIndexOffsetMax && i < this.container.getRecipeListSize(); ++i) {
                int j = i - this.recipeIndexOffset;
                int k = left + j % 4 * 17;
                int l = j / 4;
                int i1 = top + l * 19 + 2;
                this.field_230706_i_.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(list.get(i)), k, i1);
            }
        }

        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public boolean func_231044_a_(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        this.clickedOnScroll = false;
        if (this.hasItemsInInputSlot) {
            int i = this.guiLeft + 52;
            int j = this.guiTop + 14;
            int k = this.recipeIndexOffset + 12;

            for (int l = this.recipeIndexOffset; l < k; ++l) {
                int i1 = l - this.recipeIndexOffset;
                double d0 = p_mouseClicked_1_ - (double) (i + i1 % 4 * 16);
                double d1 = p_mouseClicked_3_ - (double) (j + i1 / 4 * 18);
                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.container.enchantItem(this.field_230706_i_.player, l)) {
                    Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.field_230706_i_.playerController.sendEnchantPacket((this.container).windowId, l);
                    return true;
                }
            }

            i = this.guiLeft + 119;
            j = this.guiTop + 9;
            if (p_mouseClicked_1_ >= (double) i && p_mouseClicked_1_ < (double) (i + 12) && p_mouseClicked_3_ >= (double) j && p_mouseClicked_3_ < (double) (j + 54)) {
                this.clickedOnScroll = true;
            }
        }

        return super.func_231044_a_(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    public boolean func_231045_a_(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (this.clickedOnScroll && this.canScroll()) {
            int i = this.guiTop + 14;
            int j = i + 54;
            this.sliderProgress = ((float) p_mouseDragged_3_ - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) this.getHiddenRows()) + 0.5D) * 4;
            return true;
        } else {
            return super.func_231045_a_(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
        }
    }

    @Override
    public boolean func_231043_a_(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        if (this.canScroll()) {
            int i = this.getHiddenRows();
            this.sliderProgress = (float) ((double) this.sliderProgress - p_mouseScrolled_5_ / (double) i);
            this.sliderProgress = MathHelper.clamp(this.sliderProgress, 0.0F, 1.0F);
            this.recipeIndexOffset = (int) ((double) (this.sliderProgress * (float) i) + 0.5D) * 4;
        }

        return true;
    }

    private boolean canScroll() {
        return this.hasItemsInInputSlot && this.container.getRecipeListSize() > 12;
    }

    protected int getHiddenRows() {
        return (this.container.getRecipeListSize() + 4 - 1) / 4 - 3;
    }

    private void onInventoryUpdate() {
        this.hasItemsInInputSlot = this.container.hasItemsInInputSlot();
        if (!this.hasItemsInInputSlot) {
            this.sliderProgress = 0.0F;
            this.recipeIndexOffset = 0;
        }

    }
}