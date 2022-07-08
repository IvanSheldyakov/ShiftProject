package ru.cft.freelanceservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cft.freelanceservice.service.YellowRubberDuckService;

@Api(description = "Actions for very very yellow duck!")
@RestController
@RequestMapping("api/duck")
public class YellowRubberDuckController {

    private final YellowRubberDuckService yellowRubberDuckService;

    @Autowired
    public YellowRubberDuckController(YellowRubberDuckService yellowRubberDuckService) {
        this.yellowRubberDuckService = yellowRubberDuckService;
    }

    @ApiOperation("Get information about the duck")
    @PostMapping("/{material}")
    public ResponseEntity<Properties> getDuckProperties(
            @ApiParam("Duck material")
            @RequestBody
            MaterialProperties materialProperties
    ) {
        return yellowRubberDuckService.getDuckProperties(materialProperties.material);
    }

    @ApiOperation("Quack quack!!!")
    @GetMapping("/sound")
    public ResponseEntity<?> makeTheQuack(
            @RequestParam
            Integer countRepetition,

            @RequestParam
            Integer countQuacks
    ) {
        return yellowRubberDuckService.makeTheQuack(countRepetition, countQuacks);
    }
}
