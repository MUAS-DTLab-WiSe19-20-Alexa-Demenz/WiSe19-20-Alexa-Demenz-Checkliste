package edu.hm.cs.tado.model;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TodoElementTest {
    @Test
    public void checkElementsTest() {

        TodoElement element = new TodoElement();

        Assert.assertFalse(element.isCheckedToday());

        element.check();
        Assert.assertTrue(element.isCheckedToday());
        element.check();
        Assert.assertTrue(element.isCheckedToday());
    }

    @Test
    public void timestampConversion() {
        TodoElement element = new TodoElement();
        List<String> timestamps = new ArrayList<>();
        timestamps.add(Timestamp.from(Instant.now()).toString());
        timestamps.add(Timestamp.from(Instant.now()).toString());

        element.setChecksList(timestamps);
        Assert.assertEquals(timestamps, element.getChecksList());
    }

}
