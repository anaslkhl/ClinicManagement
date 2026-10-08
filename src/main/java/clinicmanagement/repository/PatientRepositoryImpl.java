package clinicmanagement.repository;

import clinicmanagement.model.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PatientRepositoryImpl implements PatientRepository{


    private final EntityManagerFactory entityManagerFactory;

    public PatientRepositoryImpl(EntityManagerFactory entityManagerFactory){

        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Patient save(Patient patient){

        EntityManager em = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            if(patient.getId() == null){
                em.persist(patient);
            }else {
                patient = em.merge(patient);
            }
            transaction.commit();
            return patient;
        }catch (Exception e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<Patient> findByEmail(String email) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
             return
                     em.createQuery("SELECT p FROM Patient p WHERE p.email = :email")
                             .setParameter("email", email).getResultStream().findFirst();

        }
        finally {
            em.close();
        }

    }

    public Optional<Patient> findById(UUID id){


        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return Optional.ofNullable(em.find(Patient.class, id));

        }
        finally {
            em.close();
        }
    }

    @Override
    public List<Patient> findAll(){

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery("SELECT p FROM Patient p", Patient.class).getResultList();
        } catch (Exception e) {
            throw e;
        }finally {
            em.close();
        }
    }

    @Override
    public void delete(Patient patient){

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Patient managedPatient = em.contains(patient) ? patient : em.merge(patient);
            em.remove(managedPatient);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }finally {
            em.close();
        }
    }

    @Override
    public boolean existsByCin(String cin) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(p) FROM Patient p WHERE p.cin = :cin",
                            Long.class
                    )
                    .setParameter("cin", cin)
                    .getSingleResult();

            return count > 0;

        } finally {
            em.close();
        }
    }


    public boolean existsByEmail(String email){

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Long count = em.createQuery("SELECT COUNT(p) FROM Patient p WHERE p.email = :email", Long.class)
                    .setParameter("email", email).getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw e;
        }finally {
            em.close();
        }
    }
}
