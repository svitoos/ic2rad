package svitoos.ic2rad;

import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPill extends Item {

  public ItemPill() {
    setMaxStackSize(1);
    setCreativeTab(IC2.tabIC2);
    setUnlocalizedName("AntiRadPill");
    setTextureName(ic2rad.MOD_ID + ":" + "AntiRadPill");
  }

  public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
    if (!p_77654_3_.capabilities.isCreativeMode) {
      --p_77654_1_.stackSize;
    }

    if (!p_77654_2_.isRemote) {
      p_77654_3_.curePotionEffects(p_77654_1_);
    }

    return p_77654_1_.stackSize <= 0 ? null : p_77654_1_;
  }

  /** How long it takes to use or consume an item */
  public int getMaxItemUseDuration(ItemStack p_77626_1_) {
    return 32;
  }

  /** returns the action that specifies what animation to play when the items is being used */
  public EnumAction getItemUseAction(ItemStack p_77661_1_) {
    return EnumAction.drink;
  }

  /**
   * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
   * world, entityPlayer
   */
  public ItemStack onItemRightClick(
      ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
    p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
    return p_77659_1_;
  }
}
