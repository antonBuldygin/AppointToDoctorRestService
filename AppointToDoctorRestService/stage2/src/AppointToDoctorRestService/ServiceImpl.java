package AppointToDoctorRestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {

    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    AvailableDatesRepository availableDatesRepository;

    int daysToSchedule = 4;
    Doctor director;

    @Override
    public Appoint setAppointment(Appoint appointment) {
        Optional<Doctor> doc = doctorRepository
                .findDoctorByDoctorName(appointment.getDoctor()
                        .getDoctorName().toLowerCase().trim());
        Optional<Patient> patient = patientRepository
                .findPatientByPatientName(appointment
                        .getPatient().getPatientName().toLowerCase().trim());

        if (!doc.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Doctor name");
        }
        appointment.setDoctor(doc.get());

        if (patient.isPresent()) {
            appointment.setPatient(patient.get());
        }
       try{ if (appointment.getPatient().getPatientName()==null||appointment.getPatient().getPatientName().trim().isEmpty()){ throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
               "Patient is empty");}}
       catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Patient is empty");
        }
        Optional<Appoint> appointIsExist = appointmentRepository
                .findAppointByDateAndDoctor(appointment.getDate(), appointment.getDoctor());
        if (appointIsExist.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Appoint exist. Chose another time or another doctor");
        }

        Iterable<AvailableDates> availableDates = availableDatesRepository
                .findAvailableDatesByAvailabletimeAndBookedAndDoctor(appointment.getDate(), false, doc.get());
        List<AvailableDates> requestedDates = new ArrayList<>();
        availableDates.forEach(requestedDates::add);

        if (requestedDates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The date is not available for appointment");
        }
        AvailableDates foundAvailableDates = new AvailableDates();
        if (requestedDates.stream().findFirst().isPresent()) {
            foundAvailableDates = requestedDates.stream().findFirst().get();
        }
        foundAvailableDates.setBooked(true);

        patientRepository.save(appointment.getPatient());
        appointmentRepository.save(appointment);

        return appointment;
    }

    @Override
    public Doctor addDoctor(Doctor doc) {
        if (doc.getDoctorName().toLowerCase().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name should not be empty");
        }
        if (doctorRepository.findDoctorByDoctorName(doc.getDoctorName().toLowerCase().trim()).isPresent()) {
            return null;
        } else doc.setDoctorName(doc.getDoctorName().toLowerCase().trim());
        return doctorRepository.save(doc);
    }

    @Override
    public List<Doctor> allDoctors() {
        Iterable<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> doctorSet = new ArrayList<>();
        doctors.forEach(doctorSet::add);

        return doctorSet;
    }

    @Override
    public List<Patient> allPatients() {
        Iterable<Patient> patients = patientRepository.findAll();
        List<Patient> patientList = new ArrayList<>();
        patients.forEach(patientList::add);

        return patientList;
    }

    @Override
    public List<AvailableDates> availableDatesByDoctor(String doc) {
        Optional<Doctor> doctor = doctorRepository.findDoctorByDoctorName(doc.toLowerCase().trim());
        List<AvailableDates> appointList1 = new ArrayList<>();
        LocalDate today = LocalDate.now();
        long id;
        long days = 0;
        Doctor doctorToUpdate;
        Doctor doctor1;
        if (doctor.isPresent()) {
            doctor1 = doctor.get();
            id = doctor1.getId();
            appointList1 = doctor1.getAvailableDatesSet();
            doctorToUpdate = doctorRepository.findById(id).get();
            if (!appointList1.isEmpty()) {

                Optional<AvailableDates> maxdate = appointList1.stream()
                        .max(Comparator.comparing(AvailableDates::getAvailabletime));

                if (maxdate.isPresent()) {
                    Period duration = Period
                            .between(today, maxdate.get().getAvailabletime());

                    days = duration.getDays() + 2;

                }
                for (int i = (int) days; i <= daysToSchedule - days; i++) {
                    AvailableDates availableDates = new AvailableDates(today.plusDays(i), false, doctorToUpdate);
//                    availableDatesRepository.save(availableDates);
                    appointList1.add(availableDates);
                }


                doctorToUpdate.setAvailableDatesSet(appointList1);
                doctorRepository.save(doctorToUpdate);

                return appointList1.stream().sorted(Comparator.comparing(AvailableDates::getAvailabletime)).collect(Collectors.toList());

            } else for (int i = 1; i <= daysToSchedule; i++) {
                AvailableDates availableDates = new AvailableDates(today.plusDays(i), false, doctorToUpdate);
//                availableDatesRepository.save(availableDates);
                appointList1.add(availableDates);

            }

            doctorToUpdate.setAvailableDatesSet(appointList1);
            doctorRepository.save(doctorToUpdate);
//            availableDatesRepository.findAll().forEach(appointList1::add);
            return appointList1
                    .stream()
                    .sorted(Comparator
                            .comparing(AvailableDates::getAvailabletime))
                    .collect(Collectors.toList());
//                    doctorToUpdate;
        }
        return appointList1;
    }

    @Override
    public Doctor deleteDoctor(String doc) {

        Optional<Doctor> doctorToFind = doctorRepository.findDoctorByDoctorName(doc.toLowerCase().trim());
        Optional<Doctor> directorToFind = doctorRepository.findDoctorByDoctorName("director");

        if (doctorToFind.isPresent()) {
            if (!directorToFind.isPresent()) {
                director = new Doctor("director");
            } else {
                director = directorToFind.get();
            }


            Doctor doctor = doctorToFind.get();
            List<Appoint> existingAppoints = new ArrayList<>();
            appointmentRepository.findAppointByDoctor(doctor).forEach(existingAppoints::add);


            existingAppoints.stream().forEach(d -> d.setDoctor(director));
//            List<Appoint> collect = doctor.getAppoint().stream().sequential().collect(Collectors.toCollection(() -> director.getAppoint()));
            director.setAppoint(existingAppoints);
            doctorRepository.save(director);
            doctorRepository.delete(doctor);

//
            return doctor;
        }

        return null;
    }

    @Override
    public List<Appoint> showAppointment() {
        Iterable<Appoint> appoints = appointmentRepository.findAll();
        List<Appoint> appointList = new ArrayList<>();
        appoints.forEach(appointList::add);
        return appointList;
    }

    @Override
    public List<Appoint> deleteAppointment(String id) {
        List<Appoint> appointToDelete = appointmentRepository.findByIdApp(Long.parseLong(id));
        if (!appointToDelete.isEmpty()) {
            Optional<Doctor> first = appointToDelete.stream().map(d -> d.getDoctor()).findFirst();
            if (first.isPresent()) {
                Optional<AvailableDates> first1 = first.get().getAvailableDatesSet().stream()
                        .filter(s -> s.getAvailabletime()
                                .equals(appointToDelete.get(0).getDate())).findFirst();
                first1.get().setBooked(false);
            }
            appointmentRepository.deleteById(Long.parseLong(id));
        }

        return appointToDelete;
    }

    @Override
    public List<Appoint> showAppointmentbyDoctor(String doc) {

        Optional<Doctor> doctor = doctorRepository.findDoctorByDoctorName(doc.toLowerCase().trim());
        List<Appoint> appointList1 = new ArrayList<>();
        if (doctor.isPresent()) {
            Doctor doctor1 = doctor.get();
            appointList1 = doctor1.getAppoint();
        }
        return appointList1;
    }
}
