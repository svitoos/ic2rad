package svitoos.ic2rad;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import ic2.api.reactor.IReactor;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RadHandler {

  private int radDuration;
  private int radAmplifier;

  @SubscribeEvent
  public void onPlayerTickEvent(PlayerTickEvent e) {
    if (!e.player.worldObj.isRemote && e.phase == Phase.START && !hasAntiRadArmor(e.player)) {
      if (hasRadItem(e.player)) {
        addRadiation(e.player, 1);
      } else if (e.player.worldObj.getTotalWorldTime() % Config.tickFrequency == 0
          && nearRadBlock(e.player)) {
        addRadiation(e.player, Config.tickFrequency);
      }
    }
  }

  private void addRadiation(EntityLivingBase living, int tickFrequency) {
    PotionEffect effect = living.getActivePotionEffect(IC2Potion.radiation);
    if (effect == null) {
      IC2Potion.radiation.applyTo(living, radDuration * 20, radAmplifier);
    } else {
      IC2Potion.radiation.applyTo(
          living, effect.getDuration() + radDuration * tickFrequency, radAmplifier);
    }
  }

  private boolean hasRadItem(EntityPlayer player) {
    if (Config.radItems.size() > 0) {
      for (int i = 0; i < player.inventory.mainInventory.length; i++) {
        ItemStack stack = player.inventory.mainInventory[i];
        if (stack != null) {
          String name = Item.itemRegistry.getNameForObject(stack.getItem());
          name += stack.getMaxDamage() == 0 ? "@" + stack.getItemDamage() : "@0";
          if (isRadItem(name)) {
            radDuration = Config.radItemsEffectDuration;
            radAmplifier = Config.radItemsEffectAmplifier;
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean nearRadBlock(EntityLivingBase living) {
    final boolean findRadBlocks = Config.radBlocks.size() > 0;
    if (findRadBlocks || Config.findReactor) {
      final World world = living.worldObj;
      final int x1 = (int) Math.floor(living.posX - Config.radBlocksRadius);
      final int y1 = (int) Math.floor(living.posY - Config.radBlocksRadius);
      final int z1 = (int) Math.floor(living.posZ - Config.radBlocksRadius);
      final int x2 = (int) Math.floor(living.posX + 1 + Config.radBlocksRadius);
      final int y2 = (int) Math.floor(living.posY + 2 + Config.radBlocksRadius);
      final int z2 = (int) Math.floor(living.posZ + 1 + Config.radBlocksRadius);
      for (int x = x1; x < x2; x++) {
        for (int y = y1; y < y2; y++) {
          for (int z = z1; z < z2; z++) {
            if (world.blockExists(x, y, z) && !world.isAirBlock(x, y, z)) {
              Block block = world.getBlock(x, y, z);
              int meta = world.getBlockMetadata(x, y, z);
              if (findRadBlocks) {
                final String name = Block.blockRegistry.getNameForObject(block) + "@" + meta;
                if (isRadBlock(name)) {
                  radDuration = Config.radBlocksEffectDuration;
                  radAmplifier = Config.radBlocksEffectAmplifier;
                  return true;
                }
              }
              if (Config.findReactor && block.hasTileEntity(meta)) {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te instanceof IReactor && ((IReactor) te).produceEnergy()) {
                  radDuration = Config.reactorEffectDuration;
                  radAmplifier = Config.reactorEffectAmplifier;
                  return true;
                }
              }
            }
          }
        }
      }
    }
    return false;
  }

  private boolean hasAntiRadArmor(EntityLivingBase living) {
    if (!ItemArmorHazmat.hasCompleteHazmat(living)) {
      for (int i = 1; i < 5; ++i) {
        ItemStack stack = living.getEquipmentInSlot(i);
        if (stack == null || !isAntiRadArmor(Item.itemRegistry.getNameForObject(stack.getItem()))) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isRadBlock(String name) {
    return Config.radBlocks.contains(name);
  }

  private boolean isRadItem(String name) {
    return Config.radItems.contains(name);
  }

  private boolean isAntiRadArmor(String name) {
    return Config.antiRadArmor.contains(name);
  }

  static void initAntiRadPills() {
    //    final List<ItemStack> curativeItems;
    //    for (String name : Config.antiRadPills) {
    //      final String[] parts = name.split("@");
    //      curativeItems.add(
    //          new ItemStack(
    //              (Item) Item.itemRegistry.getObject(parts[0]),
    //              1,
    //              parts.length > 1 ? Integer.parseInt(parts[1]) : 0));
    //    }
  }
}
