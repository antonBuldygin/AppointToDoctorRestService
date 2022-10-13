package AppointToDoctorRestService;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    Map<Long, Appointment> appointments = new TreeMap<>();
    int appId = 0;

    @Override
    public Appointment setAppointment(Appointment appointment) {
//        Appointment newApp = new Appointment(appointment.getDoctorName(), appointment.getPatientName(), appointment.getDate());
        appId++;
        appointments.put((long) appId, appointment);
        return appointment;
    }

    @Override
    public Map<Long, Appointment> showAppointment() {
        return appointments;
    }

    @Override
    public Appointment deleteAppointment(String id) {
//        Appointment app = new Appointment();
        Optional<Appointment> optionalIsbn = appointments.entrySet().stream()
                .filter(e -> (Long.parseLong(id))==(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst();
        if (optionalIsbn.isPresent()) {

            appointments.remove(Long.parseLong(id));
            return optionalIsbn.get();
        }
        return   new Appointment("null","null",null);
    }
}
