package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> create(@RequestBody @Valid LokacijaRequest lokacijaRequest){
        log.info("Creating location{}",lokacijaRequest);
        try{
            LokacijaResponse response=lokacijaService.create(lokacijaRequest);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<Object> getAll(){
        log.info("Finding all locations");
        try{
            List<LokacijaResponse>responses= lokacijaService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        log.info("Finding locatation with id: {}",id);
        try{
            LokacijaResponse response=lokacijaService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){
        log.info("Deleting location with id:{}",id);
        try{
            lokacijaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
