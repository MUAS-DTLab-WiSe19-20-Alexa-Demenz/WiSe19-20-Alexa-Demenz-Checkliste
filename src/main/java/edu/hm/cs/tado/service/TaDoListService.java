package edu.hm.cs.tado.service;

import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TodoElement;

import java.util.NoSuchElementException;

public class TaDoListService {

    private TaDoList list;

    public TaDoListService(TaDoList list) {
        this.list = list;
    }

    public void checkElement(String name) {
        getTodoElement(name).check();
    }

    private TodoElement getTodoElement(String name) {
        return list.findElement(name).orElseThrow(NoSuchElementException::new);
    }
}
