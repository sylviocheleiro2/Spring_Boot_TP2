package br.com.infnet.tp2_legado.controller.aventura;

import br.com.infnet.tp2_legado.dto.aventura.AventureiroRequest;
import br.com.infnet.tp2_legado.dto.aventura.AventureiroResponse;
import br.com.infnet.tp2_legado.service.aventura.AventureiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aventureiros")
@RequiredArgsConstructor
public class AventureiroController {

    private final AventureiroService service;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AventureiroRequest request)
    {
        try {
            AventureiroResponse response = service.cadastrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}