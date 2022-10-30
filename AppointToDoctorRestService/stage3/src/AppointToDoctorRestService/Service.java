package AppointToDoctorRestService;

import javax.print.Doc;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Service {

    public Appoint setAppointment(Appoint appointment);

    public List<Appoint> showAppointment();

    public List<Appoint> deleteAppointment(String id);
    public List<Appoint> showAppointmentbyDoctor(String doc);
    public Doctor addDoctor(Doctor doc);

    public List<Doctor> allDoctors();
    public List<Patient> allPatients();

    public List<AvailableDates> availableDatesByDoctor(String doc);

    public Doctor deleteDoctor(String id);
}
