package ru.cft.freelanceservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class YellowRubberDuckService {

    public ResponseEntity<Properties> getDuckProperties(String materialDuck) {
         switch (materialDuck) {
            case ("rubber"):
                return ResponseEntity.ok(new  DuckProperties());
             default:
                return ResponseEntity.ok(new Properties());
         }
    }

    public ResponseEntity<?> makeTheQuack(Integer countRepetition, Integer countQuacks) {
        if (countQuacks < 0 || countRepetition < 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Message("Invalid parameter value"));
        } else if (countQuacks == 0 || countRepetition == 0) {
            return ResponseEntity.ok(new SoundProperties());
        } else {
            return ResponseEntity.ok(new SoundProperties(quackBuilder(countRepetition, countQuacks)));
        }
    }

    private String quackBuilder(Integer countRepetition, Integer countQuacks) {
        // TODO
        return "";
    }
}
