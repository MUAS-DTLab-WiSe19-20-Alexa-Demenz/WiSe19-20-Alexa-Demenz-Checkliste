package edu.hm.cs.tado.service;

import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TodoElement;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaDoListServiceTest {

    @Test
    public void checkNamedElement() {
        TaDoList list = new TaDoList();
        list.getElements().add(new TodoElement("duschen"));
        list.getElements().add(new TodoElement("waschen"));

        TaDoListService service = new TaDoListService(list);
        service.checkElement("duschen");

        assertTrue("element should be checked", list.getElements().get(0).isCheckedToday());
        assertFalse("list should not be checked", list.getElements().get(1).isCheckedToday());
    }
}
