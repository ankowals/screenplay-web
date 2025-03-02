package tests.screenplay;

import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScreenplayTestBase {}
