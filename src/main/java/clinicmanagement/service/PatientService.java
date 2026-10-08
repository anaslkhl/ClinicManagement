package clinicmanagement.service;

import clinicmanagement.Validation.*;
import clinicmanagement.model.Patient;
import clinicmanagement.repository.PatientRepository;
import clinicmanagement.repository.UserRepository;
import clinicmanagement.util.PasswordHasher;

import java.util.Optional;
import java.util.UUID;

public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository){
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }


    public Patient registerPatient(Patient patient){

        if(patient == null){
            throw new IllegalArgumentException("There is no patient !");
        }

        EmailValidation.validateEmail(patient.getEmail());
        NameValidation.validateName(patient.getNom());
        PhoneValidation.validatePhone(patient.getTelephone());
        PasswordValidation.validatePassword(patient.getPassword());
        CinValidation.ValidateCin(patient.getCin());
        DateNaissanceValidation.ValidateDateNaissance(patient.getDateNaissance());
        GenderValidation.ValidateGender(patient.getGender());
        AddressValidation.ValidateAddress(patient.getAdresse());

        if(patientRepository.existsByCin(patient.getCin())){
            throw new IllegalArgumentException("CIN already exist it must be unique !");
        }
        if(userRepository.existsByEmail(patient.getEmail())){
            throw new IllegalArgumentException("Email alrady exist , it must be unique !");
        }

        patient.setPassword(PasswordHasher.hash(patient.getPassword()));

        patientRepository.save(patient);
        return patient;

    }

    public Patient getPatientById(UUID id){

        if(id == null){
            throw new IllegalArgumentException("Patient ID does not exist !");
        }
        return patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found !"));

    }
}

