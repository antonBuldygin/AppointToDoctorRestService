package AppointToDoctorRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

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
    public ResponseEntity<?> getAppointments() {
        if (appointment.showAppointment().size() == 0) {
            return new ResponseEntity<>("appointment.showAppointment()", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(appointment.showAppointment(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<?> deleteAppointment(@RequestParam(required = false) String id) {
        Appointment res = appointment.deleteAppointment(id);
        try {
            res.getDoctorName().equals("null");
            res.getPatientName().equals("null");
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErorrMessage("The appointment does not exist or one of the fields is null!"),
                    HttpStatus.CONFLICT);
        }

        if (res.getDoctorName().equals("null") && res.getPatientName().equals("null")) {
            return new ResponseEntity<>(new ErorrMessage("The appointment does not exist or was already cancelled"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            res.getDate().equals("null");
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErorrMessage("The date field is null!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/setAppointment")
    public ResponseEntity<?> setAppointment(@Valid @RequestBody Appointment app) {
        int year = app.getDate().getYear();
        if (year == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Most probably Date is empty. ");
        }
        Appointment res = new Appointment();
        try {
            res = appointment.setAppointment(app);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error to add appointment  ");
        }

        if (idApp % 2 == 0) {
            idApp = idApp + 1;
        } else {
            idApp = idApp + 5;
        }
        res.setIdApp(idApp);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
