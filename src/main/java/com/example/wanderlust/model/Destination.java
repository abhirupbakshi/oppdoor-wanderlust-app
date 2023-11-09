package com.example.wanderlust.model;

import com.example.wanderlust.annotation.NullOrNotBlank;
import com.example.wanderlust.model.validation.group.Create;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "destination")
@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
public class Destination {

    @JsonProperty(value = "uuid", access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @JsonProperty(value = "name")
    @NullOrNotBlank(message = "Given name is invalid", groups = Create.class)
    @Column(name = "name")
    private String name;

    @JsonProperty(value = "description")
    @NullOrNotBlank(message = "Given description is invalid", groups = Create.class)
    @Column(name = "description")
    private String description;

    @JsonProperty(value = "location")
    @NotBlank(message = "Location cannot be empty", groups = Create.class)
    @Column(name = "location", nullable = false)
    private String location;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destination")
    private List<Itinerary> itineraries;
}
