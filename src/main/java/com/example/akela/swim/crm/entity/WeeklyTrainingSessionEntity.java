package com.example.akela.swim.crm.entity;

import com.example.akela.swim.crm.utils.LongListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "weekly_training_session")
public class WeeklyTrainingSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = LongListConverter.class)
    @Column(name = "child_ids", columnDefinition = "TEXT")
    private List<Long> childIds;

    @Column(name = "coach_id", nullable = false)
    private Long coachId;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "notes")
    private String notes;

    @Column(name = "source_standard_id")
    private Long sourceStandardId;

    @Column(name = "manual_override")
    private boolean manualOverride = false;

    @Column(name = "week_start_date", nullable = false)
    private LocalDate weekStartDate;

    @Column(name = "confirmed", nullable = false)
    private boolean confirmed = false;

}

