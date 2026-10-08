package clinicmanagement.service;

import clinicmanagement.Validation.*;
import clinicmanagement.model.Doctor;
import clinicmanagement.repository.DoctorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor createDoctor(Doctor doctor) {

        if(doctor == null){
            throw new IllegalArgumentException("Doctor is required !");
        }

        EmailValidation.validateEmail(doctor.getEmail());
        NameValidation.validateName(doctor.getNom());
        PhoneValidation.validatePhone(doctor.getTelephone());
        PasswordValidation.validatePassword(doctor.getPassword());
        MatriculeValidation.validateMatricule(doctor.getMatricule());
        TitreValidation.validateTitre(doctor.getTitre());
        SpecialtyValidation.validateSpecialty(doctor.getSpecialty());

        if(doctorRepository.existsByEmail(doctor.getEmail())){
            throw new IllegalArgumentException("Email already exist !");
        }
        if(doctorRepository.existsByMatricule(doctor.getMatricule())){
            throw new IllegalArgumentException("Matricule already exist !");
        }

        return doctorRepository.save(doctor);

    }

    public Optional<Doctor> findById(UUID id) {

        if(id == null){
            throw new IllegalArgumentException("ID is required !");
        }

        return doctorRepository.findById(id);
    }

    public Optional<Doctor> findByEmail(String email) {

        if(email == null){
            throw new IllegalArgumentException("Email is required !");
        }

        return doctorRepository.findByEmail(email);
    }

    public Optional<Doctor> findByMatricule(String matricule) {

        if(matricule == null){
            throw new IllegalArgumentException("Matricule is required !");
        }

        return doctorRepository.findByMatricule(matricule);
    }

    public List<Doctor> findAll() {

        return doctorRepository.findAll();
    }

    public Doctor updateDoctor(Doctor doctor) {

        if (doctor == null || doctor.getId() == null) {
            throw new IllegalArgumentException("Doctor and doctor ID are required.");
        }

        Doctor existingDoctor = doctorRepository.findById(doctor.getId()).orElseThrow(() -> new IllegalArgumentException("Doctor not found."));

        EmailValidation.validateEmail(doctor.getEmail());
        NameValidation.validateName(doctor.getNom());
        NameValidation.validateName(doctor.getPrenom());
        PhoneValidation.validatePhone(doctor.getTelephone());

        existingDoctor.setNom(doctor.getNom());
        existingDoctor.setPrenom(doctor.getPrenom());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setTelephone(doctor.getTelephone());
        existingDoctor.setPassword(doctor.getPassword());
        existingDoctor.setActive(doctor.isActive());

        existingDoctor.setMatricule(doctor.getMatricule());
        existingDoctor.setTitre(doctor.getTitre());
        existingDoctor.setSpecialty(doctor.getSpecialty());

        return doctorRepository.update(existingDoctor);
    }

    public void deleteDoctor(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("Doctor ID is required.");
        }

        doctorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found."));

        doctorRepository.delete(id);
    }
}