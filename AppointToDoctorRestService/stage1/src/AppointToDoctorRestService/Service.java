package AppointToDoctorRestService;

import java.util.List;
import java.util.Map;

public interface Service {

    public Appointment setAppointment(Appointment appointment);

    public Map<Long,Appointment> showAppointment();

    public Appointment deleteAppointment(String id);

    public List<Appointment> showListOfAppoinemnts();
}
