package rs.ac.bg.fon.parking_kazne.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.parking_kazne.dto.VozacRequest;
import rs.ac.bg.fon.parking_kazne.dto.VozacResponse;
import rs.ac.bg.fon.parking_kazne.service.VozacService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vozac")
public class VozacController {
    private final VozacService vozacService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid VozacRequest vozacRequest){
        try{
            VozacResponse vozacResponse=vozacService.create(vozacRequest);
            return ResponseEntity.status(HttpStatus.OK).body(vozacResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<Object> findAll(){
        try{
            List<VozacResponse> vozacResponses=vozacService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(vozacResponses);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable long id){
        try{
            VozacResponse vozacResponse=vozacService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(vozacResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id){
        try{
            vozacService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted Driver");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
