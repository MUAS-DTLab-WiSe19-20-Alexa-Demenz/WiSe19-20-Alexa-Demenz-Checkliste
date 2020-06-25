package edu.hm.cs.tado.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaDoManager {

    private List<TaDoList> lists = new ArrayList<>();

    public List<TaDoList> getLists() {
        return lists;
    }

    public void setLists(List<TaDoList> lists) {
        this.lists = lists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaDoManager that = (TaDoManager) o;
        return Objects.equals(lists, that.lists);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lists);
    }
}
