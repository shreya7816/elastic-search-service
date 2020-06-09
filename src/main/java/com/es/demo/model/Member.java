package com.es.demo.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Member {

    private UUID id;
    private String firstName;
    private String lastName;
    private String membershipType;
    private List<Amenity> amenities;

}
