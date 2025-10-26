package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic sanity tests for the application.
 * Detailed algorithm tests are in MSTTest.
 */
@DisplayName("Basic Application Tests")
public class AppTest {

    @Test
    @DisplayName("Application main method runs without errors")
    public void testAppMainRuns() {
        assertDoesNotThrow(() -> App.main(new String[]{}),
            "App.main() should run without throwing exceptions");
    }

    @Test
    @DisplayName("MST class and its nested classes are accessible")
    public void testMSTClassExists() {
        assertNotNull(MST.class, "MST class should exist");
        assertNotNull(MST.DSU.class, "MST.DSU class should exist");
        assertNotNull(MST.Result.class, "MST.Result class should exist");
        assertNotNull(MST.GraphInput.class, "MST.GraphInput class should exist");
        assertNotNull(MST.OutputRecord.class, "MST.OutputRecord class should exist");
    }
}


