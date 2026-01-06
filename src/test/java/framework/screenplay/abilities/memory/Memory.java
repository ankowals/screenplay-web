package framework.screenplay.abilities.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limitations you can't use collections types like List<String> as your value type. As a workaround
 * create your own wrapper type and use in place of collection type.
 */
public class Memory {
  private final Map<Key<?>, Object> cache;

  public Memory() {
    this.cache = new ConcurrentHashMap<>();
  }

  <T> void remember(String name, T value) {
    this.cache.put(new Key<>(name, value.getClass()), value);
  }

  <T> T recall(String name, Class<T> type) {
    return type.cast(this.cache.get(new Key<>(name, type)));
  }

  <T> void forget(String name, Class<T> type) {
    this.cache.remove(new Key<>(name, type));
  }

  void clear() {
    this.cache.clear();
  }

  record Key<T>(String name, Class<T> type) {}
}
