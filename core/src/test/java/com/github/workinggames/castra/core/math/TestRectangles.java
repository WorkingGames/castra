package com.github.workinggames.castra.core.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.badlogic.gdx.math.Rectangle;

public class TestRectangles
{
    @Test(dataProvider = "rectangleIntersection")
    public void testIntersects(String testCase, Rectangle a, Rectangle b, boolean expected)
    {
        boolean actual = Rectangles.intersects(a, b);
        assertThat(actual).isEqualTo(expected);
    }

    @DataProvider
    private Object[][] rectangleIntersection()
    {
        return new Object[][]{
            {
                "b in a, positive coordinates", new Rectangle(10, 10, 10, 10), new Rectangle(12, 12, 5, 5), true
            }, {
                "b in a, negative coordinates", new Rectangle(-10, -10, 10, 10), new Rectangle(-8, -8, 5, 5), true
            }, {
                "a in b", new Rectangle(10, 10, 10, 10), new Rectangle(8, 8, 30, 30), true
            }, {
                "a in b, negative coordinates", new Rectangle(-10, -10, 10, 10), new Rectangle(-12, -12, 30, 30), true
            }, {
                "a left of b, positive coordinates", new Rectangle(10, 10, 10, 10), new Rectangle(50, 10, 10, 10), false
            }, {
                "a right of b, positive coordinates",
                new Rectangle(100, 30, 10, 10),
                new Rectangle(50, 30, 10, 10),
                false
            }, {
                "a top of b, positive coordinates", new Rectangle(10, 100, 10, 10), new Rectangle(10, 10, 10, 10), false
            }, {
                "a below b, positive coordinates", new Rectangle(20, 20, 10, 10), new Rectangle(20, 70, 10, 10), false
            }, {
                "a left of b, negative coordinates",
                new Rectangle(-100, -20, 10, 10),
                new Rectangle(-50, -20, 10, 10),
                false
            }, {
                "a right of b, negative coordinates",
                new Rectangle(-100, -30, 10, 10),
                new Rectangle(-150, -30, 10, 10),
                false
            }, {
                "a top of b, negative coordinates",
                new Rectangle(-30, -20, 10, 10),
                new Rectangle(-30, -100, 10, 10),
                false
            }, {
                "a below b, negative coordinates",
                new Rectangle(-20, -70, 10, 10),
                new Rectangle(-20, -30, 10, 10),
                false
            }, {
                "a intersects left side of b, positive coordinates",
                new Rectangle(10, 10, 10, 10),
                new Rectangle(8, 10, 10, 10),
                true
            }, {
                "a intersects left side of b, positive coordinates",
                new Rectangle(10, 10, 10, 10),
                new Rectangle(8, 15, 5, 3),
                true
            }, {
                "a directly left b, positive coordinates",
                new Rectangle(10, 5, 10, 10),
                new Rectangle(20, 10, 10, 40),
                true
            }, {
                "a intersects right side of b, positive coordinates",
                new Rectangle(10, 10, 10, 10),
                new Rectangle(18, 10, 10, 10),
                true
            }, {
                "a intersects right side of b, positive coordinates",
                new Rectangle(10, 10, 10, 10),
                new Rectangle(18, 15, 5, 3),
                true
            }, {
                "a directly right b, positive coordinates",
                new Rectangle(30, 8, 10, 10),
                new Rectangle(20, 10, 10, 40),
                true
            }, {
                "a intersects top side of b, positive coordinates",
                new Rectangle(10, 10, 10, 10),
                new Rectangle(10, 8, 10, 10),
                true
            }, {
                "a intersects top side of b, positive coordinates",
                new Rectangle(10, 10, 10, 10),
                new Rectangle(18, 15, 5, 12),
                true
            }, {
                "a directly top b, positive coordinates",
                new Rectangle(30, 10, 10, 10),
                new Rectangle(20, 20, 10, 40),
                true
            }
        };
    }
}