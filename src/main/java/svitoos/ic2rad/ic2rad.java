package svitoos.ic2rad;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;

@Mod(
    modid = ic2rad.MOD_ID,
    name = ic2rad.MOD_NAME,
    version = ic2rad.VERSION /*@MCVERSIONDEP@*/,
    dependencies = "required-after:IC2")
public class ic2rad {

  public static final String MOD_ID = "@MODID@";
  public static final String MOD_NAME = "@MODNAME@";
  public static final String VERSION = "@MODVERSION@";

  @Mod.Instance public static ic2rad instance;

  static Logger logger;

  private final RadHandler radHandler = new RadHandler();
  public static ItemPill antiRadPill;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent e) {
    logger = LogManager.getLogger(MOD_ID, new StringFormatterMessageFactory());
    Config.init(e.getSuggestedConfigurationFile());
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent e) {
    MinecraftForge.EVENT_BUS.register(radHandler);
    FMLCommonHandler.instance().bus().register(radHandler);
    if (Config.antiRadPill) {
      antiRadPill = new ItemPill();
      GameRegistry.registerItem(antiRadPill, "antiRadPill");
    }
    RadHandler.initAntiRadPills();
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent e) {}

  @Mod.EventHandler
  public void serverLoad(FMLServerStartingEvent event) {}

  @Mod.EventHandler
  public void serverStart(FMLServerAboutToStartEvent event) {}

  @Mod.EventHandler
  public void serverStop(FMLServerStoppedEvent event) {}

  static void info(String format, Object... data) {
    logger.log(Level.INFO, format, data);
  }

  static void warn(String format, Object... data) {
    logger.log(Level.WARN, format, data);
  }
}
