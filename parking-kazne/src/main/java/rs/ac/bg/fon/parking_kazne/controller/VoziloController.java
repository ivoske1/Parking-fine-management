package rs.ac.bg.fon.parking_kazne.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.VoziloRequest;
import rs.ac.bg.fon.parking_kazne.dto.VoziloResponse;
import rs.ac.bg.fon.parking_kazne.service.VozacService;
import rs.ac.bg.fon.parking_kazne.service.VoziloService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/vozilo")
@RequiredArgsConstructor
public class VoziloController {
    private final VoziloService voziloService;
    private final VozacService vozacService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid VoziloRequest voziloRequest){
        try{
            VoziloResponse voziloResponse=voziloService.create(voziloRequest);
            return ResponseEntity.status(HttpStatus.OK).body(voziloResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<Object> findAll(){
        try{
            List<VoziloResponse>voziloResponses=voziloService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(voziloResponses);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        try{
            VoziloResponse vozilo=voziloService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(vozilo);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){
        try{
            voziloService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
