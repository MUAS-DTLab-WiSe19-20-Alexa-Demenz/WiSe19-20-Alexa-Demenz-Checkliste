package edu.hm.cs.tado.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TodoElement {

    private String name;
    private List<LocalDateTime> checksList = new ArrayList<>();

    public TodoElement() {
    }

    public TodoElement(String name) {
        this.name = name;
    }

    public void check() {
        checksList.add(LocalDateTime.now());
    }

    @JsonIgnore
    public boolean isCheckedToday() {
        if (checksList.size() > 0) {
            LocalDateTime lastCheckEntry = checksList.get(checksList.size() - 1);
            return lastCheckEntry.isAfter(LocalDate.now().atStartOfDay());
        }
        return false;
    }

    @JsonIgnore
    public boolean isChecked(int daysAgo) {
        return checksList.stream()
                .anyMatch(c ->
                        c.isAfter(LocalDate.now().atStartOfDay()
                                .minus(daysAgo, ChronoUnit.DAYS))
                                && c.isBefore(LocalDate.now().atStartOfDay()
                                .minus(daysAgo - 1L, ChronoUnit.DAYS)));
    }

    public List<String> getChecksList() {
        return checksList.stream().map(x -> Timestamp.valueOf(x).toString()).collect(Collectors.toList());
    }

    public void setChecksList(List<String> timestamps) {
        checksList = timestamps.stream().map(s ->
                Timestamp.valueOf(s).toLocalDateTime()
        ).collect(Collectors.toList());
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
        TodoElement that = (TodoElement) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(checksList, that.checksList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, checksList);
    }
}
