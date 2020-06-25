package edu.hm.cs.tado.model;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TaDoListTest {
    @Test
    public void validateSettersAndGetters() {


        PojoClass listPojo = PojoClassFactory.getPojoClass(TaDoList.class);

        Validator validator = ValidatorBuilder.create()
                // Lets make sure that we have a getter and a setter for every field defined.
                .with(new SetterMustExistRule()).with(new GetterMustExistRule())

                // Lets also validate that they are behaving as expected
                .with(new SetterTester()).with(new GetterTester()).build();

        // Start the Test
        validator.validate(listPojo);
    }

    @Test
    public void getCheckedElements(){

        TodoElement e1 = new TodoElement();
        TodoElement e2 = new TodoElement();
        TodoElement e3 = new TodoElement();
        TodoElement e4 = new TodoElement();

        e1.check();
        e3.check();

        TaDoList list1 = new TaDoList();
        List<TodoElement> specifiedElements;

        list1.getElements().add(e2);
        list1.getElements().add(e4);

        specifiedElements = list1.getElements(true);
        assertTrue(specifiedElements.isEmpty());

        list1.getElements().add(e1);
        list1.getElements().add(e3);

        specifiedElements = list1.getElements(true);

        assertFalse(specifiedElements.isEmpty());
        assertEquals(2, specifiedElements.size());
        assertEquals(e1, specifiedElements.get(0));
        assertEquals(e3, specifiedElements.get(1));

    }

    @Test
    public void getUncheckedElements(){

        TodoElement e1 = new TodoElement();
        TodoElement e2 = new TodoElement();
        TodoElement e3 = new TodoElement();
        TodoElement e4 = new TodoElement();

        e1.check();
        e3.check();

        TaDoList list1 = new TaDoList();
        List<TodoElement> specifiedElements;

        list1.getElements().add(e1);
        list1.getElements().add(e3);

        specifiedElements = list1.getElements(false);
        assertTrue(specifiedElements.isEmpty());

        list1.getElements().add(e2);
        list1.getElements().add(e4);

        specifiedElements = list1.getElements(false);

        assertFalse(specifiedElements.isEmpty());
        assertEquals(2, specifiedElements.size());
        assertEquals(e2, specifiedElements.get(0));
        assertEquals(e4, specifiedElements.get(1));

    }

}
