package com.example.avaliacaoservice.controller;

import com.example.avaliacaoservice.model.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final Map<Long, Avaliacao> avaliacoes = new ConcurrentHashMap<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${filme.service.url}")
    private String filmeServiceUrl;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Avaliacao avaliacao) {
        try {
            restTemplate.getForObject(filmeServiceUrl + "/filmes/" + avaliacao.getFilmeId(), Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Filme com id " + avaliacao.getFilmeId() + " não existe.");
        }

        Long id = proximoId.getAndIncrement();
        avaliacao.setId(id);
        avaliacoes.put(id, avaliacao);
        return ResponseEntity.ok(avaliacao);
    }

    @GetMapping
    public List<Avaliacao> listarTodas() {
        return List.copyOf(avaliacoes.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
        Avaliacao avaliacao = avaliacoes.get(id);
        if (avaliacao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avaliacao);
    }
}