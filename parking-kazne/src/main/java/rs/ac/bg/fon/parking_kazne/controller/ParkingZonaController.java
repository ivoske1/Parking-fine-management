package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.ParkingZonaRequest;
import rs.ac.bg.fon.parking_kazne.dto.ParkingZonaResponse;
import rs.ac.bg.fon.parking_kazne.service.ParkingZonaService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/parkingZona")
@RestController
public class ParkingZonaController {
    private final ParkingZonaService parkingZonaService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ParkingZonaRequest parkingZonaRequest){
        try{
            ParkingZonaResponse parkingZonaResponse=parkingZonaService.create(parkingZonaRequest);
            return ResponseEntity.status(HttpStatus.OK).body(parkingZonaResponse);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        try{
            List<ParkingZonaResponse>parkingZonaResponseList=parkingZonaService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(parkingZonaResponseList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id){
        try{
            ParkingZonaResponse parkingZonaResponse=parkingZonaService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(parkingZonaResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id){
        try{
            parkingZonaService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted ");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
