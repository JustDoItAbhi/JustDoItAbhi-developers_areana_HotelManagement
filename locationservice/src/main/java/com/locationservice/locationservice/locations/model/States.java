package com.locationservice.locationservice.locations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"country_id", "state_name"})})
@Getter
@Setter
public class States extends BaseModel {

private String stateName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;
    @OneToMany(mappedBy = "states",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<City> cityList;
}
