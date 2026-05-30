package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.KontrolorRequest;
import rs.ac.bg.fon.parking_kazne.dto.KontrolorResponse;
import rs.ac.bg.fon.parking_kazne.service.KontrolorService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/kontrolor")
@RestController
public class KontrolorController {
    private final KontrolorService kontrolorService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid KontrolorRequest kontrolorRequest){
        try{
            KontrolorResponse kp=kontrolorService.create(kontrolorRequest);
            return ResponseEntity.status(HttpStatus.OK).body(kp);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Create kontrolor bad req");
        }
    }
    @GetMapping
    public ResponseEntity<Object> getAll(){
        log.info("Getting all Kontrolors");
        try{
            List<KontrolorResponse>responses=kontrolorService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<Object>findById(@PathVariable long id){
        log.info("Finding kontrolor with id : {}",id);
        try{
            KontrolorResponse response=kontrolorService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id){
        log.info("Deleting kontrolor with id: {}",id);
        try{
            kontrolorService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
