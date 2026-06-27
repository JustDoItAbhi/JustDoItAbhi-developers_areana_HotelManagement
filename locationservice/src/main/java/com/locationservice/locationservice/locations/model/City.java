package com.locationservice.locationservice.locations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "city", uniqueConstraints = {@UniqueConstraint(columnNames = {"states_id", "city_name"})}
)
@Getter
@Setter
public class City extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "states_id")
    private States states;
    private String cityName;
    private String cityImage;
}
