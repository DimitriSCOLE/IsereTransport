package com.mazzone.isere.transport.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static com.google.common.truth.Truth.assertThat;

@RunWith(Parameterized.class)
public class StringsTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, null}, {"", ""}, {"TEST", "Test"}, {"tEST", "Test"}, {"tEST MULtI", "Test Multi"}
        });
    }

    @Parameter public String fInput;
    @Parameter(value = 1) public String fExpected;

    @Test
    public void test() {
        assertThat(Strings.capitalizeFirstLetterEachWord(fInput)).isEqualTo(fExpected);
    }
}