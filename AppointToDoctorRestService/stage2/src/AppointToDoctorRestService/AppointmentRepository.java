package AppointToDoctorRestService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public interface AppointmentRepository extends CrudRepository<Appoint,Long> {
    List<Appoint> findByIdApp(Long idAPP);

    Optional<Appoint> findAppointByDateAndDoctor(LocalDate d, Doctor doc);


    Iterable<Appoint> findAppointByDoctor(Doctor doc);
}
