package com.locationservice.locationservice.locations.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "country_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country extends BaseModel {
    private String countryName;
    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<States> stateList;
}
