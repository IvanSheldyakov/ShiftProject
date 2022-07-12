package ru.cft.freelanceservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

@Data
public class ExecutorRegisterDTO  {
    private String username;
    private String firstName;
    private String secondName;
    private String password;
    private String email;


    @JsonProperty("specializations and prices")
    private ArrayList<SpecializationPriceDTO> specializationsAndPrices = new ArrayList<>();
}
