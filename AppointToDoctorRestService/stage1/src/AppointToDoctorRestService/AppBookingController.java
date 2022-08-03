package AppointmentBookingToDoctorRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AppBookingController {
    long idApp = 0;
    private Service appointment;

    @Autowired
    public AppBookingController(Service appointment) {
        this.appointment = appointment;
    }

    @GetMapping("/appointments")
    public Map<Long, Appointment> getAppointments() {
        return appointment.showAppointment();
    }

    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<?> deleteAppointment(@RequestParam(required = false) String id) {
        Appointment res = appointment.deleteAppointment(id);
        if (res.getDoctorName().equals("null")&&res.getPatientName().equals("null")) {
            return new ResponseEntity<>(new ErorrMessage(), HttpStatus.BAD_REQUEST);
        }
        else return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/setAppointment")
    public ResponseEntity<?> setAppointment(@RequestBody Appointment app) {
        Appointment res = appointment.setAppointment(app);
        idApp++;
        res.setIdApp(idApp);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
