package framework.screenplay.abilities.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Memory {
  private final Map<String, Object> memory;

  public Memory() {
    this.memory = new ConcurrentHashMap<>();
  }

  public <T> void remember(String name, T value) {
    this.memory.put(name, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T recall(String name) {
    Object object = this.memory.get(name);

    if (object == null) {
      throw new NoObjectToRecallException(name);
    }

    return (T) object;
  }

  public void forget(String key) {
    this.memory.remove(key);
  }

  public void clear() {
    this.memory.clear();
  }
}
