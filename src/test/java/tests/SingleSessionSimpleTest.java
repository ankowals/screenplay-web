package tests;

import base.BaseTest;
import io.github.bonigarcia.seljup.SingleSession;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;

/**
 * In parallel execution each method requires separate driver instance and with @SingleSession enabled we have a single instance shared for all tests.
 * To run test configure parallel execution so that top-level classes run in parallel but methods in same thread.
 *
 * junit.jupiter.execution.parallel.enabled=true
 * junit.jupiter.execution.parallel.mode.default=same_thread
 * junit.jupiter.execution.parallel.mode.classes.default=concurrent
 */
@SingleSession
public class SingleSessionSimpleTest extends BaseTest {

    @AfterEach
    void storageCleanup() {
        cleanWebStorage(browser);
    }
}
