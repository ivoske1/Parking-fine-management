package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.KaznaResponse;
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeRequest;
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeResponse;
import rs.ac.bg.fon.parking_kazne.service.PlacanjaService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/placanje")
public class PlacanjeController {
    private final PlacanjaService placanjaService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid PlacanjeRequest placanjeRequest){
        try{
            PlacanjeResponse placanjeResponse=placanjaService.create(placanjeRequest);
            return ResponseEntity.status(HttpStatus.OK).body(placanjeResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<Object> findAll(){
        try {
            List<PlacanjeResponse> svaPLacanja=placanjaService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(svaPLacanja);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object>findById(@PathVariable long id){
        try{
            PlacanjeResponse placanjeResponse=placanjaService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(placanjeResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
