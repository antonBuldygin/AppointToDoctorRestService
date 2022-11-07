package AppointToDoctorRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AppBookingController {
    long idApp = 0;
    private Service appointment;
    private List<Appoint> appoints;

    @Autowired
    public AppBookingController(Service appointment) {
        this.appointment = appointment;
    }


    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments() {
        if (appointment.showAppointment().size() == 0) {
            return new ResponseEntity<>("No appointments", HttpStatus.NO_CONTENT);
        }
        List<Appoint> appointList = appointment.showAppointment();
        List<AppointmentToShow> appointmentToShows = getAppointmentToShows(appointList);
        return new ResponseEntity<>(appointmentToShows, HttpStatus.OK);
    }

    private static List<AppointmentToShow> getAppointmentToShows(List<Appoint> appointList) {
        List<AppointmentToShow> appointmentToShows = appointList.stream().map(app -> {
            AppointmentToShow appointmentToShow = new AppointmentToShow();
            appointmentToShow.setIdApp(app.getIdApp());
            appointmentToShow.setDate(app.getDate());
            appointmentToShow.setDoctor(app.getDoctor().getDoctorName());
            appointmentToShow.setSpecialization(app.getDoctor().getDr());
            appointmentToShow.setDoctorId(app.getDoctor().getId());
            appointmentToShow.setPatient(app.getPatient().getPatientName());
            return appointmentToShow;
        }).collect(Collectors.toList());
        return appointmentToShows;
    }

    private static AppointmentToShow getAppointmentToShow(List<Appoint> appointList) {
        List<AppointmentToShow> appointmentToShows = appointList.stream().map(app -> {
            AppointmentToShow appointmentToShow = new AppointmentToShow();
            appointmentToShow.setIdApp(app.getIdApp());
            appointmentToShow.setDate(app.getDate());
            appointmentToShow.setDoctor(app.getDoctor().getDoctorName());
            appointmentToShow.setSpecialization(app.getDoctor().getDr());
            appointmentToShow.setDoctorId(app.getDoctor().getId());
            appointmentToShow.setPatient(app.getPatient().getPatientName());
            return appointmentToShow;
        }).collect(Collectors.toList());
        return appointmentToShows.get(0);
    }
    @GetMapping("/appointmentsByDoctor")
    public ResponseEntity<?> getDocAppointments(@RequestParam String doc) {
        if (appointment.showAppointmentbyDoctor(doc).size() == 0) {
            return new ResponseEntity<>("appointment.showAppointment()", HttpStatus.NO_CONTENT);
        }

        List<Appoint> appointList = appointment.showAppointmentbyDoctor(doc);

        List<AppointmentToShow> appointmentToShows = getAppointmentToShows(appointList);
        return new ResponseEntity<>(appointmentToShows, HttpStatus.OK);
    }

    @GetMapping("/availableDatesByDoctor")
    public ResponseEntity<?> getDocAvailableDates(@RequestParam String doc) {

        List<AvailableDates> doctor = appointment.availableDatesByDoctor(doc);
        if (doctor.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }


    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<?> deleteAppointment(@RequestParam(required = false) String id) {
        List<Appoint> deleted = appointment.deleteAppointment(id);
        if (deleted.isEmpty()) {
            return new ResponseEntity<>("The appointment does not exist or was already cancelled", HttpStatus.BAD_REQUEST);
        }
        AppointmentToShow appointmentToShows = getAppointmentToShow(deleted);
        return new ResponseEntity<>(appointmentToShows, HttpStatus.OK);
    }

    @PostMapping("/newDoctor")
    public ResponseEntity<?> addDoctor(@Valid @RequestBody Doctor doc) {
        doc = appointment.addDoctor(doc);
        if (doc == null) {
            return new ResponseEntity<>("Doctor exists", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(doc, HttpStatus.OK);
    }

    @GetMapping("/allDoctorslist")
    public ResponseEntity<?> AllDoctors() {
        if (appointment.allDoctors().isEmpty()) {
            return new ResponseEntity<>("No doctors in the database", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(appointment.allDoctors(), HttpStatus.OK);
    }

    @GetMapping("/allPatientslist")
    public ResponseEntity<?> AllPatients() {
        if (appointment.allPatients().isEmpty()) {
            return new ResponseEntity<>("No patients in the database", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(appointment.allPatients(), HttpStatus.OK);
    }


    @PostMapping("/setAppointment")
    public ResponseEntity<?> setAppointment(@Valid @RequestBody Appoint app) {
        if (app.getDoctor().getDoctorName().toLowerCase().trim().equals("director")) {
            return new ResponseEntity<>("Not allowed to set appointment to Director", HttpStatus.BAD_REQUEST);
        }
        app.getPatient().setPatientName(app.getPatient().getPatientName().trim().toLowerCase());
        Appoint appr = appointment.setAppointment(app);

        AppointmentToShow appointmentToShow = new AppointmentToShow();
        appointmentToShow.setIdApp(appr.getIdApp());
        appointmentToShow.setDate(appr.getDate());
        appointmentToShow.setDoctor(appr.getDoctor().getDoctorName());
        appointmentToShow.setSpecialization(appr.getDoctor().getDr());
        appointmentToShow.setDoctorId(appr.getDoctor().getId());
        appointmentToShow.setPatient(appr.getPatient().getPatientName());

        return new ResponseEntity<>(appointmentToShow, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDoctor")
    public ResponseEntity<?> deleteDoctor(@RequestParam(required = false) String doc) {

        if ((doc)==(null) || doc.trim().equals("")) {
            return new ResponseEntity<>("Parameter should not be null or empty", HttpStatus.BAD_REQUEST);
        }

        Doctor doctor = appointment.deleteDoctor(doc.toLowerCase().trim());
        if (doctor != null) {
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        } else return new ResponseEntity<>("Doctor not found", HttpStatus.BAD_REQUEST);
    }

//    @GetMapping("/statisticsDay")
//    public ResponseEntity<?> statisticsPerDay() {
//        if (appointment.showAppointment().size() == 0) {
//            return new ResponseEntity<>("No appointments", HttpStatus.NO_CONTENT);
//        }
//        appoints = appointment.showAppointment();
//        Map<LocalDate, List<Appoint>> collect = appoints.stream().collect(Collectors.groupingBy(Appoint::getDate));
//
//        Map<LocalDate, Long> count1 = collect.entrySet().stream().collect(Collectors.toMap(
//                e -> e.getKey(), n -> n.getValue().stream().count()));
//
//        List<Map.Entry<LocalDate, Long>> collect1 = count1.entrySet().stream().collect(Collectors.toList());
//        return new ResponseEntity<>(collect1, HttpStatus.OK);
//    }
//
//    @GetMapping("/statisticsDoc")
//    public ResponseEntity<?> statisticsPerDoctor() {
//        if (appointment.showAppointment().size() == 0) {
//            return new ResponseEntity<>("No appointments", HttpStatus.NO_CONTENT);
//        }
//        appoints = appointment.showAppointment();
//
//        Map<Doctor, List<Appoint>> collect1 = appoints.stream().collect(Collectors.groupingBy(Appoint::getDoctor));
//        Map<String, Long> collect2 = collect1.entrySet().stream().collect(Collectors.toMap(
//                e -> e.getKey().getDoctorName(), m -> m.getValue().stream().count()));
//        List<Map.Entry<String, Long>> collect3 = collect2.entrySet().stream().collect(Collectors.toList());
//        return new ResponseEntity<>(collect3, HttpStatus.OK);
//    }
}
