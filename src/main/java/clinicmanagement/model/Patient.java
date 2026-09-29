package clinicmanagement.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient extends User {

    @Column(nullable = false, unique = true, length = 50)
    private String cin;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(length = 255)
    private String adresse;

    public Patient() {
        super();
    }

    public Patient(String nom,
                   String prenom,
                   String email,
                   String telephone,
                   String password,
                   String cin,
                   LocalDate dateNaissance,
                   Gender gender,
                   String adresse) {

        super(nom, prenom, email, telephone, password, Role.PATIENT);

        this.cin = cin;
        this.dateNaissance = dateNaissance;
        this.gender = gender;
        this.adresse = adresse;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}