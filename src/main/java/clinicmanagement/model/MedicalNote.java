package clinicmanagement.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medical_notes")
public class MedicalNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(nullable = false, length = 1000)
    private String diagnostic;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private boolean validated;

    public MedicalNote() {
    }

    public MedicalNote(
            Doctor doctor,
            Appointment appointment,
            String diagnostic,
            String content,
            LocalDateTime dateCreation,
            boolean validated) {

        this.doctor = doctor;
        this.appointment = appointment;
        this.diagnostic = diagnostic;
        this.content = content;
        this.dateCreation = dateCreation;
        this.validated = validated;
    }

    public UUID getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}