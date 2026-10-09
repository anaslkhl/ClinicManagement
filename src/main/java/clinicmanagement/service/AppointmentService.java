
package clinicmanagement.service;

import clinicmanagement.model.Appointment;
import clinicmanagement.model.AppointmentStatus;
import clinicmanagement.model.Doctor;
import clinicmanagement.model.Patient;
import clinicmanagement.repository.AppointmentRepository;
import clinicmanagement.repository.DoctorRepository;
import clinicmanagement.repository.PatientRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment createAppointment(
            UUID patientId,
            UUID doctorId,
            LocalDateTime dateHeure,
            String motif,
            AppointmentStatus status) {

        if (patientId == null) {
            throw new IllegalArgumentException("Patient is required.");
        }

        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor is required.");
        }

        if (dateHeure == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }

        if (dateHeure.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past."
            );
        }

        if (motif != null && motif.length() > 500) {
            throw new IllegalArgumentException(
                    "Appointment reason cannot exceed 500 characters."
            );
        }

        if (status == null) {
            throw new IllegalArgumentException("Appointment status is required.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found.")
                );

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Doctor not found.")
                );

        Appointment appointment = new Appointment(
                patient,
                doctor,
                dateHeure,
                motif,
                status
        );

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("Appointment ID is required.");
        }

        return appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Appointment not found.")
                );
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByPatient(UUID patientId) {

        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID is required.");
        }

        if (!patientRepository.findById(patientId).isPresent()) {
            throw new IllegalArgumentException("Patient not found.");
        }

        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(UUID doctorId) {

        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID is required.");
        }

        if (!doctorRepository.findById(doctorId).isPresent()) {
            throw new IllegalArgumentException("Doctor not found.");
        }

        return appointmentRepository.findByDoctorId(doctorId);
    }

    public Appointment updateAppointment(
            UUID id,
            UUID patientId,
            UUID doctorId,
            LocalDateTime dateHeure,
            String motif,
            AppointmentStatus status) {

        Appointment appointment = getAppointmentById(id);

        if (patientId == null) {
            throw new IllegalArgumentException("Patient is required.");
        }

        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor is required.");
        }

        if (dateHeure == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }

        if (dateHeure.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past."
            );
        }

        if (motif != null && motif.length() > 500) {
            throw new IllegalArgumentException(
                    "Appointment reason cannot exceed 500 characters."
            );
        }

        if (status == null) {
            throw new IllegalArgumentException("Appointment status is required.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found.")
                );

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Doctor not found.")
                );

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDateHeure(dateHeure);
        appointment.setMotif(motif);
        appointment.setStatus(status);

        return appointmentRepository.update(appointment);
    }

    public void deleteAppointment(UUID id) {

        getAppointmentById(id);

        appointmentRepository.delete(id);
    }
}