package ru.cft.freelanceservice.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TaskDTO {
    private String name;
    private String description;
    private ArrayList<String> specializations = new ArrayList<>();
}
