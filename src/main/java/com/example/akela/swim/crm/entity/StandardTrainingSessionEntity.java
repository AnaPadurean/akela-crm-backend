package com.example.akela.swim.crm.entity;

import com.example.akela.swim.crm.utils.LongListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "standard_training_session")
public class StandardTrainingSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = LongListConverter.class)
    @Column(name = "child_ids", columnDefinition = "TEXT")
    private List<Long> childIds;

    @Column(name = "coach_id", nullable = false)
    private Long coachId;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;


    @Column(name = "notes")
    private String notes;

}
