package org.example;

import org.junit.Assert;
import org.junit.Test;

public class Hello11Test {

    @Test
    public void getMessage() {
        Hello hello = new Hello();
        Assert.assertEquals(hello.getMessage(), "Hello Java 11");
    }
}