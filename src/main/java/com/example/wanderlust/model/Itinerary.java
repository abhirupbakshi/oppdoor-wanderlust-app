package com.example.wanderlust.model;

import com.example.wanderlust.annotation.NullOrNotBlank;
import com.example.wanderlust.model.validation.group.Create;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "itinerary")
@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
public class Itinerary {

    @JsonProperty(value = "uuid", access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @JsonProperty(value = "activities")
    @NullOrNotBlank(message = "Given activities is invalid", groups = Create.class)
    @Column(name = "activities")
    private String activities;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_uuid", nullable = false)
    private Destination destination;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itinerary")
    private List<Expense> expenses;
}
