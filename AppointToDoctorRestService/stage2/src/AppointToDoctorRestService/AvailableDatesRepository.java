package AppointToDoctorRestService;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AvailableDatesRepository extends CrudRepository<AvailableDates,Long> {


    Iterable<AvailableDates> findAvailableDatesByAvalabletimeAndBookedAndDoctor(LocalDate localDateTime, boolean status, Doctor doc);
}
