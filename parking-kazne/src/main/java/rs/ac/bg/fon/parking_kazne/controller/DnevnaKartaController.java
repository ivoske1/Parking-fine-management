package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaRequest;
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaResponse;
import rs.ac.bg.fon.parking_kazne.service.DnevnaKartaService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dnevnaKarta")
public class DnevnaKartaController {
    private final DnevnaKartaService dnevnaKartaService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid DnevnaKartaRequest dnevnaKartaRequest){
        try{
            DnevnaKartaResponse dp=dnevnaKartaService.create(dnevnaKartaRequest);
            return ResponseEntity.status(HttpStatus.OK).body(dp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<Object> findAll(){
        try{
            List<DnevnaKartaResponse>lista=dnevnaKartaService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        try{
            DnevnaKartaResponse dnevnaKartaResponse=dnevnaKartaService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(dnevnaKartaResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){
        try{
            dnevnaKartaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/registracija/{registracija}")
    public ResponseEntity<Object> findByRegistracija(@PathVariable String registracija){
        try{
            List<DnevnaKartaResponse>karte=dnevnaKartaService.findByRegistracija(registracija);
            if(karte.isEmpty()){
                return ResponseEntity.status(HttpStatus.OK).body("No tickets for this vehicle");
            }
            return ResponseEntity.status(HttpStatus.OK).body(karte);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
