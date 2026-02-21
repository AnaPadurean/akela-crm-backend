package com.example.akela.swim.crm.entity;

import com.example.akela.swim.crm.utils.LongListConverter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    // ---- GETTERS & SETTERS ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public List<Long> getChildIds() { return childIds; }
    public void setChildIds(List<Long> childIds) { this.childIds = childIds; }

    public Long getCoachId() { return coachId; }
    public void setCoachId(Long coachId) { this.coachId = coachId; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getSourceStandardId() { return sourceStandardId; }
    public void setSourceStandardId(Long sourceStandardId) { this.sourceStandardId = sourceStandardId; }

    public boolean isManualOverride() { return manualOverride; }
    public void setManualOverride(boolean manualOverride) { this.manualOverride = manualOverride; }

    public LocalDate getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }

    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

}

