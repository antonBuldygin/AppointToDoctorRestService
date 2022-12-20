package AppointToDoctorRestService;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AvailableDatesRepository extends CrudRepository<AvailableDates,Long> {
//"findAvailableDatesByAvalabletimeAndBookedAndDoctor"

    Iterable<AvailableDates> findAvailableDatesByAvailabletimeAndBookedAndDoctor(LocalDate localDateTime, boolean status, Doctor doc);
}
