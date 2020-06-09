package com.es.demo.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Amenity {
    private UUID id;
    private String name;
}
