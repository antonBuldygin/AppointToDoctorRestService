package AppointToDoctorRestService;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
@Component
public interface AvailableDatesRepository extends CrudRepository<AvailableDates,Long> {


    Iterable<AvailableDates> findAvailableDatesByAvailabletimeAndBookedAndDoctor(LocalDate localDateTime, boolean status, Doctor doc);
}
