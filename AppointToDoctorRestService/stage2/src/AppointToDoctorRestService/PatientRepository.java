package AppointToDoctorRestService;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface PatientRepository extends CrudRepository<Patient, Long> {

    Optional<Patient> findPatientByPatientName(String patient);
}
