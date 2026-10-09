
package clinicmanagement.repository.impl;

import clinicmanagement.model.Appointment;
import clinicmanagement.repository.AppointmentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final EntityManagerFactory emf;

    public AppointmentRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Appointment save(Appointment appointment) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            em.persist(appointment);

            transaction.commit();

            return appointment;

        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Appointment> findById(UUID id) {

        EntityManager em = emf.createEntityManager();

        try {
            Appointment appointment = em.find(Appointment.class, id);

            return Optional.ofNullable(appointment);

        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> findAll() {

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Appointment> query = em.createQuery(
                    "SELECT a FROM Appointment a",
                    Appointment.class
            );

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> findByPatientId(UUID patientId) {

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Appointment> query = em.createQuery(
                    "SELECT a FROM Appointment a WHERE a.patient.id = :patientId",
                    Appointment.class
            );

            query.setParameter("patientId", patientId);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Appointment> findByDoctorId(UUID doctorId) {

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Appointment> query = em.createQuery(
                    "SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId",
                    Appointment.class
            );

            query.setParameter("doctorId", doctorId);

            return query.getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public Appointment update(Appointment appointment) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Appointment updatedAppointment = em.merge(appointment);

            transaction.commit();

            return updatedAppointment;

        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    @Override
    public void delete(UUID id) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Appointment appointment = em.find(Appointment.class, id);

            if (appointment != null) {
                em.remove(appointment);
            }

            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsById(UUID id) {

        EntityManager em = emf.createEntityManager();

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(a) FROM Appointment a WHERE a.id = :id",
                            Long.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

            return count > 0;

        } finally {
            em.close();
        }
    }
}