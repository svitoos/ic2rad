package svitoos.ic2rad;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Reflection {

  public static void setInternal(
      final Object instance, final String fieldName, final Object value) {
    try {
      final Field field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
      field.set(instance, value);
    } catch (final IllegalAccessException | NoSuchFieldException ignored) {
    }
  }
}
