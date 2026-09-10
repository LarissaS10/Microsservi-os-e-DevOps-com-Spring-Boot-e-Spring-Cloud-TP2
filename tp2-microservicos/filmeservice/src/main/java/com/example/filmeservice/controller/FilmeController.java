package com.example.filmeservice.controller;

import com.example.filmeservice.model.Filme;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/filmes")
public class FilmeController {

    private final Map<Long, Filme> filmes = new ConcurrentHashMap<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    @PostMapping
    public Filme criar(@RequestBody Filme filme) {
        Long id = proximoId.getAndIncrement();
        filme.setId(id);
        filmes.put(id, filme);
        return filme;
    }

    @GetMapping
    public List<Filme> listarTodos() {
        return List.copyOf(filmes.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filme> buscarPorId(@PathVariable Long id) {
        Filme filme = filmes.get(id);
        if (filme == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(filme);
    }
}
