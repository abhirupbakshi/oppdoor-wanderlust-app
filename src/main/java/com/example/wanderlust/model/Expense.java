package com.example.wanderlust.model;

import com.example.wanderlust.annotation.NullOrNotBlank;
import com.example.wanderlust.model.validation.group.Create;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "expense")
@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
public class Expense {

    @JsonProperty(value = "uuid", access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @JsonProperty(value = "description")
    @NullOrNotBlank(message = "Given description is invalid", groups = Create.class)
    @Column(name = "description")
    private String description;

    @JsonProperty("amount")
    @NotNull(message = "Given amount is invalid", groups = Create.class)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @JsonProperty("category")
    @NotBlank(message = "Given category is invalid", groups = Create.class)
    @Column(name = "category", nullable = false)
    private String category;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "itinerary_uuid", nullable = false)
    private Itinerary itinerary;
}
