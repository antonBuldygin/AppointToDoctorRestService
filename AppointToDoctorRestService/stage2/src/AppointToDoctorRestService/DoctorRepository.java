package AppointToDoctorRestService;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {

   Optional<Doctor> findDoctorByDoctorName(String doc);

    @Override
    <S extends Doctor> S save(S entity);

    @Override
    void delete(Doctor entity);

    @Override
    boolean existsById(Long aLong);

    void deleteDoctorByDoctorName(String doc);
}
