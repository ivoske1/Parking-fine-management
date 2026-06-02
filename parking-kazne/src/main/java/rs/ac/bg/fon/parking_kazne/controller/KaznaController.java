package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.KaznaRequest;
import rs.ac.bg.fon.parking_kazne.dto.KaznaResponse;
import rs.ac.bg.fon.parking_kazne.dto.VozacResponse;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;
import rs.ac.bg.fon.parking_kazne.service.KaznaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/kazna")
@RequiredArgsConstructor
public class KaznaController {
    private final KaznaService kazneService;


    @PostMapping
    public ResponseEntity<Object> create (@RequestBody @Valid KaznaRequest kaznaRequest){
        try{
            KaznaResponse kaznaResponse=kazneService.create(kaznaRequest);
            return ResponseEntity.status(HttpStatus.OK).body(kaznaResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity<Object> findAll(){
        try{
            List<KaznaResponse> kaznaResponses=kazneService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(kaznaResponses);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable long id){
        try{
            KaznaResponse kaznaResponse=kazneService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(kaznaResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id){
        try{
            kazneService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted ticket");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @PatchMapping("/{id}/status")
    public KaznaResponse updateStatus(@PathVariable Long id,
                                      @RequestParam StatusKazne statusKazne) {
        log.info("Updating status kazne {}", id);
        return kazneService.updateStatus(id, statusKazne);
    }
    @GetMapping("/status/{status}")
    public List<KaznaResponse> findByStatus(@PathVariable StatusKazne status) {
        log.info("Finding kazne by status {}", status);
        return kazneService.findByStatus(status);
    }

    @GetMapping("/vozilo/{registracija}")
    public List<KaznaResponse> findByVozilo(@PathVariable String registracija) {
        log.info("Finding kazne by vozilo {}", registracija);
        return kazneService.findByVozilo(registracija);
    }

}
