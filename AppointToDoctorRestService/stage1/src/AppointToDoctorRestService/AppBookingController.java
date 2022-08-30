package AppointToDoctorRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
      try {
          res.getDoctorName().equals("null");res.getPatientName().equals("null");}
      catch (NullPointerException e){
          System.out.println(e.getMessage());
              return new ResponseEntity<>(new ErorrMessage("The appointment does not exist or one of the fields is null!"),
                      HttpStatus.CONFLICT);
          }

      if (res.getDoctorName().equals("null")&&res.getPatientName().equals("null")) {
              return new ResponseEntity<>(new ErorrMessage("The appointment does not exist or was already cancelled"),
                      HttpStatus.BAD_REQUEST);
          }
        try {
            res.getDate().equals("null");}
        catch (NullPointerException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ErorrMessage("The date field is null!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
          return new ResponseEntity<>(res, HttpStatus.OK);
      }


    @PostMapping("/setAppointment")
    public ResponseEntity<?> setAppointment(@RequestBody Appointment app) {
        Appointment res = appointment.setAppointment(app);
        idApp++;
        res.setIdApp(idApp);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
