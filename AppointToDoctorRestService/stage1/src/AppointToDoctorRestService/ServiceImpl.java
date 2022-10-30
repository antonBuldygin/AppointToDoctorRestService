package AppointToDoctorRestService;

import java.util.*;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    Map<Long, Appointment> appointments = new TreeMap<>();
    List<Appointment> appointmentList = new ArrayList<>();
    int appId = 0;

    @Override
    public Appointment setAppointment(Appointment appointment) {
//        Appointment newApp = new Appointment(appointment.getDoctor(), appointment.getPatient(), appointment.getDate());
        appId++;
        appointment.setPatient(appointment.getPatient().toLowerCase().trim());
        appointment.setDoctor(appointment.getDoctor().trim().toLowerCase());
        appointments.put((long) appId, appointment);
        appointmentList.add(appointment);
        return appointment;
    }

    @Override
    public Map<Long, Appointment> showAppointment() {
        return appointments;
    }

    public List<Appointment> showListOfAppoinemnts() {
        return appointmentList;
    }

    @Override
    public Appointment deleteAppointment(String id) {
//        Appointment app = new Appointment();
        Optional<Appointment> optionalIsbn = appointmentList.stream()
                .filter(e -> (Long.parseLong(id)) == (e.getIdApp()))
                .findFirst();
        if (optionalIsbn.isPresent()) {
            Appointment res = optionalIsbn.get();
            appointmentList.remove(optionalIsbn.get());
//            appointmentList.remove(Long.parseLong(id));
            return res;
        }
        return new Appointment("null", "null", null);
    }
}
