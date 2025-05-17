package base;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

// or use
// https://github.com/primefaces/primefaces/blob/master/primefaces-integration-tests/src/test/java/org/primefaces/integrationtests/JUnit5Selenium4Strategy.java
// works for Java 11 and 17

// more at https://github.com/SeleniumHQ/selenium/issues/10113
// https://github.com/SeleniumHQ/selenium/issues/9359

/*
 junit.jupiter.execution.parallel.enabled = true
 junit.jupiter.execution.parallel.mode.default = concurrent
 junit.jupiter.execution.parallel.mode.classes.default = concurrent
 junit.jupiter.execution.parallel.config.strategy = custom
 junit.jupiter.execution.parallel.config.custom.class = base.CustomStrategy
*/

public class CustomStrategy
    implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {

  private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

  @Override
  public int getParallelism() {
    return NUMBER_OF_THREADS;
  }

  @Override
  public int getMinimumRunnable() {
    return 0;
  }

  @Override
  public int getMaxPoolSize() {
    return this.getParallelism();
  }

  @Override
  public int getCorePoolSize() {
    return this.getMaxPoolSize();
  }

  @Override
  public int getKeepAliveSeconds() {
    return 60;
  }

  @Override
  public ParallelExecutionConfiguration createConfiguration(
      final ConfigurationParameters configurationParameters) {
    return this;
  }
}
