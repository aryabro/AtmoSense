package edu.uiuc.cs427app;

import org.junit.Test;
import static org.junit.Assert.*;

public class LocalCityValidatorTest {

    @Test
    public void testValidCities() {
        // Test major cities
        assertTrue("New York should be valid", LocalCityValidator.isValidCity("New York"));
        assertTrue("London should be valid", LocalCityValidator.isValidCity("London"));
        assertTrue("Tokyo should be valid", LocalCityValidator.isValidCity("Tokyo"));
        assertTrue("Paris should be valid", LocalCityValidator.isValidCity("Paris"));
        assertTrue("Beijing should be valid", LocalCityValidator.isValidCity("Beijing"));
    }

    @Test
    public void testCaseInsensitive() {
        // Test case insensitive validation
        assertTrue("new york should be valid", LocalCityValidator.isValidCity("new york"));
        assertTrue("LONDON should be valid", LocalCityValidator.isValidCity("LONDON"));
        assertTrue("tOkYo should be valid", LocalCityValidator.isValidCity("tOkYo"));
        assertTrue("pArIs should be valid", LocalCityValidator.isValidCity("pArIs"));
    }

    @Test
    public void testInvalidCities() {
        // Test invalid cities
        assertFalse("FakeCity123 should be invalid", LocalCityValidator.isValidCity("FakeCity123"));
        assertFalse("NonExistentCity should be invalid", LocalCityValidator.isValidCity("NonExistentCity"));
        assertFalse("RandomText should be invalid", LocalCityValidator.isValidCity("RandomText"));
    }

    @Test
    public void testEdgeCases() {
        // Test edge cases
        assertFalse("Empty string should be invalid", LocalCityValidator.isValidCity(""));
        assertFalse("Null should be invalid", LocalCityValidator.isValidCity(null));
        assertFalse("Only spaces should be invalid", LocalCityValidator.isValidCity("   "));
    }

    @Test
    public void testValidationMessages() {
        // Test validation messages
        String validMessage = LocalCityValidator.getValidationMessage("New York");
        assertTrue("Valid city message should contain 'Valid city'", validMessage.contains("Valid city"));

        String invalidMessage = LocalCityValidator.getValidationMessage("FakeCity");
        assertTrue("Invalid city message should contain 'not found'", invalidMessage.contains("not found"));
    }

    @Test
    public void testSimilarCities() {
        // Test similar city suggestions
        List<String> suggestions = LocalCityValidator.getSimilarCities("New Yrok", 3);
        assertTrue("Should suggest 'New York' for 'New Yrok'", suggestions.contains("New York"));

        List<String> londonSuggestions = LocalCityValidator.getSimilarCities("Londn", 3);
        assertTrue("Should suggest 'London' for 'Londn'", londonSuggestions.contains("London"));

        List<String> noSuggestions = LocalCityValidator.getSimilarCities("xyz123", 3);
        assertTrue("Should return empty list for completely different input", noSuggestions.isEmpty());
    }

    @Test
    public void testCityNormalization() {
        // Test city name normalization
        assertEquals("NEW YORK should be normalized to New York", "New York",
                LocalCityValidator.normalizeCityName("NEW YORK"));
        assertEquals("new york should be normalized to New York", "New York",
                LocalCityValidator.normalizeCityName("new york"));
        assertEquals("nEw YoRk should be normalized to New York", "New York",
                LocalCityValidator.normalizeCityName("nEw YoRk"));
    }

    @Test
    public void testGetNormalizedCityName() {
        // Test getting normalized city name for valid cities
        assertEquals("Should return 'New York' for 'new york'", "New York",
                LocalCityValidator.getNormalizedCityName("new york"));
        assertEquals("Should return 'London' for 'LONDON'", "London",
                LocalCityValidator.getNormalizedCityName("LONDON"));
        assertEquals("Should return 'Tokyo' for 'tOkYo'", "Tokyo",
                LocalCityValidator.getNormalizedCityName("tOkYo"));

        // Test invalid cities
        assertNull("Should return null for invalid city",
                LocalCityValidator.getNormalizedCityName("FakeCity123"));
        assertNull("Should return null for null input",
                LocalCityValidator.getNormalizedCityName(null));
        assertNull("Should return null for empty input",
                LocalCityValidator.getNormalizedCityName(""));
    }

    @Test
    public void testCaseInsensitiveValidation() {
        // Test that validation is case-insensitive
        assertTrue("'new york' should be valid", LocalCityValidator.isValidCity("new york"));
        assertTrue("'NEW YORK' should be valid", LocalCityValidator.isValidCity("NEW YORK"));
        assertTrue("'New York' should be valid", LocalCityValidator.isValidCity("New York"));
        assertTrue("'nEw YoRk' should be valid", LocalCityValidator.isValidCity("nEw YoRk"));

        assertTrue("'london' should be valid", LocalCityValidator.isValidCity("london"));
        assertTrue("'LONDON' should be valid", LocalCityValidator.isValidCity("LONDON"));
        assertTrue("'London' should be valid", LocalCityValidator.isValidCity("London"));
    }
}
