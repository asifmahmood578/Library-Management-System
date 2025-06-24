package com.example.lib.controller;

import com.example.lib.model.Fine;
import com.example.lib.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping("/{id}/fines")
    public ResponseEntity<List<Fine>> getFinesByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.getFinesByCustomerId(id));
    }
}

