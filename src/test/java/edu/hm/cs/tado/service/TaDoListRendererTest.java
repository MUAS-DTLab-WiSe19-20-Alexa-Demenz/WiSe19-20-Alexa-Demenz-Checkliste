package edu.hm.cs.tado.service;

import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TodoElement;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaDoListRendererTest {
    private static final String DAY_LIST_EXPECTED_HTML = "<!DOCTYPE HTML> " +
            "<html> <head><meta charset=\"UTF-8\"> </head> " +
            "<body><h1>Ihre angeforderte TaDo Liste</h1><table><tbody>" +
            "<tr><td>test1</td><td>✓</td></tr><tr><td>unchecked2</td><td></td></tr>" +
            "<tr><td>test3</td><td>✓</td></tr></tbody></table></body> </html>";

    private static final String WEEK_LIST_EXPECTED_HTML_HEAD = "<!DOCTYPE HTML> " +
            "<html> <head><meta charset=\"UTF-8\"> " +
            "</head> <body><h1>Ihr Wochenbericht</h1>" +
            "<table style='border-collapse: collapse;'>" +
            "<tr><th" + TaDoListRenderer.ELEMENT_STYLE + ">Element</th>";
    private static final String WEEK_LIST_EXPECTED_HTML_CONTENT = "</tr><tbody>" +
            "<tr><td" + TaDoListRenderer.ELEMENT_STYLE + ">test1</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + "></td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + "></td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + "></td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td></tr>" +
            "<tr><td" + TaDoListRenderer.ELEMENT_STYLE + ">test2</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + "></td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + "></td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + "></td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td></tr>" +
            "<tr><td" + TaDoListRenderer.ELEMENT_STYLE + ">test3</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + ">✓</td>" +
            "<td" + TaDoListRenderer.ELEMENT_STYLE + "></td>" +
            "</tr></tbody></table></body> </html>";

    @Test
    public void renderList() {
        TaDoList listToRender = new TaDoList();
        List<TodoElement> elements = new ArrayList<>();
        elements.add(createElement("test1", true));
        elements.add(createElement("unchecked2", false));
        elements.add(createElement("test3", true));
        listToRender.setElements(elements);
        TaDoListRenderer renderer = new TaDoListRenderer(listToRender);
        assertEquals(DAY_LIST_EXPECTED_HTML, renderer.render());
    }

    @Test
    public void renderWeekList() {
        TaDoList listToRender = new TaDoList();
        List<TodoElement> elements = new ArrayList<>();
        elements.add(createElement("test1", new boolean[]{true, false, true, true, true, false, false}));
        elements.add(createElement("test2", new boolean[]{true, true, true, false, true, false, false}));
        elements.add(createElement("test3", new boolean[]{false, true, true, true, true, true, true}));
        listToRender.setElements(elements);
        TaDoListRenderer renderer = new TaDoListRenderer(listToRender);
        String[] parts = renderer.renderWeek()
                .split("<th" + TaDoListRenderer.ELEMENT_STYLE + ">[0-9]+[.][0-9]+[.][0-9]+</th>");

        assertEquals(WEEK_LIST_EXPECTED_HTML_HEAD, parts[0]);
        assertEquals(WEEK_LIST_EXPECTED_HTML_CONTENT, parts[parts.length - 1]);
    }

    private TodoElement createElement(String name, boolean checked) {
        TodoElement element = new TodoElement(name);
        if (checked) {
            element.check();
        }
        return element;
    }

    private TodoElement createElement(String name, boolean[] checks) {
        TodoElement element = new TodoElement(name);
        List<String> timestampsList = new ArrayList<>();
        for (int i = checks.length - 1; i >= 0; i--) {
            if (checks[i]) {
                timestampsList.add(getTimestampForDaysAgo(i));
            }
        }
        element.setChecksList(timestampsList);
        return element;
    }

    private String getTimestampForDaysAgo(int daysAgo) {
        LocalDateTime dateTime = LocalDateTime.now().minus(daysAgo, ChronoUnit.DAYS);
        return Timestamp.valueOf(dateTime).toString();
    }
}
