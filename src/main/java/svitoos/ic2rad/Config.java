package svitoos.ic2rad;

import java.io.File;

import java.util.HashSet;
import java.util.Set;
import net.minecraftforge.common.config.Configuration;

public class Config {
  private static Configuration configuration;

  public static int tickFrequency;

  public static Set<String> radItems;
  public static int radItemsEffectDuration;
  public static int radItemsEffectAmplifier;

  public static Set<String> radBlocks;
  public static int radBlocksEffectDuration;
  public static int radBlocksEffectAmplifier;
  public static double radBlocksRadius;

  public static boolean findReactor;
  public static int reactorEffectDuration;
  public static int reactorEffectAmplifier;

  public static Set<String> antiRadArmor;
  // public static Set<String> antiRadPills;

  static void init(File file) {
    configuration = new Configuration(file, true);
    configuration.load();

    tickFrequency = configuration.getInt("TickFrequency", "general", 20, 1, Integer.MAX_VALUE, "");

    // Radioactive Items

    radItems = getNames("Radioactive Items", "List", true, new String[] {"IC2:blockOreUran"}, "");
    radItemsEffectDuration =
        configuration.getInt(
            "RadEffectDuration", "Radioactive Items", 10, 0, Integer.MAX_VALUE, "");
    radItemsEffectAmplifier =
        configuration.getInt(
            "RadEffectAmplifier", "Radioactive Items", 90, 0, Integer.MAX_VALUE, "");

    // Radioactive Blocks

    radBlocks = getNames("Radioactive Blocks", "List", true, new String[] {"IC2:blockOreUran"}, "");
    radBlocksEffectDuration =
        configuration.getInt(
            "RadEffectDuration", "Radioactive Blocks", 10, 0, Integer.MAX_VALUE, "");
    radBlocksEffectAmplifier =
        configuration.getInt(
            "RadEffectAmplifier", "Radioactive Blocks", 90, 0, Integer.MAX_VALUE, "");
    radBlocksRadius = configuration.get("Radioactive Blocks", "Radius", 1.0, "").getDouble();

    // Reactor Radiation

    findReactor = configuration.getBoolean("Enabled", "Reactor Radiation", true, "");
    reactorEffectDuration =
        configuration.getInt(
            "RadEffectDuration", "Reactor Radiation", 10, 0, Integer.MAX_VALUE, "");
    reactorEffectAmplifier =
        configuration.getInt(
            "RadEffectAmplifier", "Reactor Radiation", 90, 0, Integer.MAX_VALUE, "");

    // Radiation Protection

    antiRadArmor = getNames("Radiation Protection", "Armor", false, new String[] {}, "");
    // antiRadPills = getNames("Radiation Protection", "Pills", false, new String[] {}, "");

    configuration.save();
  }

  private static Set<String> getNames(
      String category, String key, boolean meta, String[] def, String comment) {
    final Set<String> names = new HashSet<>();
    String[] radBlocksArr = configuration.get(category, key, def, comment).getStringList();
    for (String name : radBlocksArr) {
      if (meta && !name.contains("@")) {
        name += "@0";
      }
      names.add(name);
    }
    return names;
  }
}
