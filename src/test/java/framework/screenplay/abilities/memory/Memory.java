package framework.screenplay.abilities.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limitations you can't use collections types like List<String> as your value type. As a workaround
 * create your own wrapper type and use in place of collection type.
 */
public class Memory {
  private final Map<Key<?>, Object> memory;

  public Memory() {
    this.memory = new ConcurrentHashMap<>();
  }

  public <T> void remember(String name, T value) {
    this.memory.put(new Key<>(name, value.getClass()), value);
  }

  public <T> T recall(String name, Class<T> type) {
    Object object = this.memory.get(new Key<>(name, type));

    if (object == null) {
      throw new NoObjectToRecallException(name);
    }

    return type.cast(object);
  }

  public <T> void forget(String name, Class<T> type) {
    this.memory.remove(new Key<>(name, type));
  }

  public void clear() {
    this.memory.clear();
  }

  record Key<T>(String name, Class<T> type) {}
}
