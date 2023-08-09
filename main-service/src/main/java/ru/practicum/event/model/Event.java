package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String title;
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    Boolean paid;
    @Column(name = "event_date")
    @DateTimeFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    User initiator;
    String description;
    @Column(name = "participant_limit")
    Integer participantLimit;
    @Enumerated(EnumType.STRING)
    EventState state;
    @Column(name = "created_on")
    @DateTimeFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;
    @Column(name = "published_on")
    @DateTimeFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;
    @AttributeOverride(name = "lat", column = @Column(name = "latitude"))
    @AttributeOverride(name = "lon", column = @Column(name = "longitude"))
    Location location;
    @Column(name = "request_moderation")
    Boolean requestModeration;
}
