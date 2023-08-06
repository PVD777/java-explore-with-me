package ru.practicum.event.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Embeddable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {
    float lat;
    float lon;
}
