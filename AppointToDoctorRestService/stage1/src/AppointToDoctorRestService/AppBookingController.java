package AppointToDoctorRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.ParseException;

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
        if (appointment.showListOfAppoinemnts().size() == 0) {
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(appointment.showListOfAppoinemnts(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<?> deleteAppointment(@RequestParam(required = false) String id) throws ParseException {
        Appointment res = appointment.deleteAppointment(id);

        try {
            res.getDoctor().equals("null");
            res.getPatient().equals("null");
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErorrMessage("The appointment does not exist or one of the fields is null!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (res.getDoctor().equals("null") && res.getPatient().equals("null")) {
            return new ResponseEntity<>(new ErorrMessage("The appointment does not exist or was already cancelled"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/setAppointment")
    public ResponseEntity<?> setAppointment(@RequestBody Appointment app) {
        int year = 0;
        try {
            year = app.getDate().getYear();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date is null");
        }

        try {
            if (app.getDoctor() == null || app.getPatient() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor or Patient is null");
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor or Patient is null");
        }
        if (app.getDoctor().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "doctor field is empty!");
        }
        if (app.getPatient().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patient field is empty!");
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
