package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaRequest;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaResponse;
import rs.ac.bg.fon.parking_kazne.service.LokacijaService;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/lokacije")
@RestController
public class LokacijaController {
    private final LokacijaService lokacijaService;

    @PostMapping
    public LokacijaResponse create(@RequestBody @Valid LokacijaRequest lokacijaRequest){
        log.info("Creating location{}",lokacijaRequest);
        System.out.println(lokacijaRequest);
        return lokacijaService.create(lokacijaRequest);
    }
    @GetMapping
    public List<LokacijaResponse> getAll(){
        log.info("Finding all locations");
        return lokacijaService.findAll();
    }
    @GetMapping("/{id}")
    public LokacijaResponse findById(@PathVariable Long id){
        log.info("Finding locatation with id: {}",id);
        return lokacijaService.findById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        log.info("Deleting location with id:{}",id);
        lokacijaService.deleteById(id);
    }

}
