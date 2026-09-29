package clinicmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor extends User {

    @Column(nullable = false, unique = true, length = 100)
    private String matricule;

    @Column(length = 100)
    private String titre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    public Doctor() {
        super();
    }

    public Doctor(String nom,
                  String prenom,
                  String email,
                  String telephone,
                  String password,
                  String matricule,
                  String titre,
                  Specialty specialty) {

        super(nom, prenom, email, telephone, password, Role.DOCTOR);

        this.matricule = matricule;
        this.titre = titre;
        this.specialty = specialty;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}