package edu.hm.cs.tado.model;


import edu.hm.cs.tado.utils.UniqueConstrainException;

import java.util.*;
import java.util.stream.Collectors;

public class TaDoList {

    private String name;
    private List<TodoElement> elements;

    public TaDoList() {
        name = null;
        elements = new ArrayList<>();
    }

    public TaDoList(String name, List<TodoElement> elements) {
        this.name = name;
        this.elements = elements;
    }

    public List<TodoElement> getElements() {
        return elements;
    }

    /**
     * getter for for either checked or unchecked elements only
     *
     * @param checked which elements are to be returned
     * @return elements with the desired state of checked
     */
    public List<TodoElement> getElements(boolean checked) {
        return elements.stream().filter(e -> e.isCheckedToday() == checked).collect(Collectors.toList());
    }

    public void setElements(List<TodoElement> elements) {
        this.elements = elements;
    }

    public void addElement(TodoElement element) throws UniqueConstrainException {
        findElement(element.getName()).ifPresent(x -> {throw new UniqueConstrainException();});
        elements.add(element);
    }

    public void addAllElements(Collection<TodoElement> elements){
        this.elements.addAll(elements);
    }

    public Optional<TodoElement> findElement(String name){
        return elements.stream().filter(e -> e.getName().equals(name)).findFirst();
    }

    /**
     * removes a named element
     *
     * @param name of the element to be removed from this list
     * @throws NoSuchElementException
     */
    public void removeElement(String name) throws NoSuchElementException {
        TodoElement element = findElement(name).orElseThrow(NoSuchElementException::new);
        this.elements.remove(element);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaDoList taDoList = (TaDoList) o;
        return Objects.equals(name, taDoList.name) &&
                Objects.equals(elements, taDoList.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, elements);
    }
}
