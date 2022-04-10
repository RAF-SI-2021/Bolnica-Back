package raf.si.bolnica.management.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.entities.enums.PrispecePacijenta;
import raf.si.bolnica.management.entities.enums.StatusPregleda;
import raf.si.bolnica.management.repositories.ScheduledAppointmentRepository;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduledAppointmentServiceImpl implements ScheduledAppointmentService {

    @Autowired
    private ScheduledAppointmentRepository scheduledAppointmentRepository;

    public ZakazaniPregled setAppointment(CreateScheduledAppointmentRequestDTO appointmentRequestDTO) {
        System.out.println(appointmentRequestDTO.getAppointmentEmployeeId());
        ZakazaniPregled appointment = new ZakazaniPregled();


        appointment.setStatusPregleda(StatusPregleda.ZAKAZANO);
        appointment.setLBZLekara(appointmentRequestDTO.getExaminationEmployeeId());
        appointment.setLBZSestre(appointmentRequestDTO.getAppointmentEmployeeId());
        appointment.setNapomena(appointmentRequestDTO.getNote());
        appointment.setDatumIVremePregleda(appointmentRequestDTO.getDateAndTimeOfAppointment());

        return scheduledAppointmentRepository.save(appointment);
    }

    @Override
    public ZakazaniPregled updateAppointment(UpdateAppointmentStatusDTO updateAppointmentStatusDTO) {
        ZakazaniPregled  appointmentForUpdate =
                scheduledAppointmentRepository.getZakazaniPregledByZakazaniPregledId(updateAppointmentStatusDTO.getAppointmentId());

            appointmentForUpdate.setStatusPregleda(StatusPregleda.valueOf(updateAppointmentStatusDTO.getAppointmentStatus()));
        return scheduledAppointmentRepository.save(appointmentForUpdate);
    }

    @Override
    public ZakazaniPregled updateArrival(UpdateArrivalStatusDTO updateArrivalStatusDTO) {
        ZakazaniPregled  appointmentForUpdate =
                scheduledAppointmentRepository.getZakazaniPregledByZakazaniPregledId(updateArrivalStatusDTO.getAppointmentId());
        appointmentForUpdate.setPrispecePacijenta(PrispecePacijenta.valueOf(updateArrivalStatusDTO.getArrivalStatus()));

        return scheduledAppointmentRepository.save(appointmentForUpdate);
    }

    @Override
    public List<ZakazaniPregled> getAppointmentByLBZ(long lbz) {
        Timestamp date = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
        List<ZakazaniPregled> allAppointments = scheduledAppointmentRepository.findByLBZLekara(lbz);
        List<ZakazaniPregled> appointments = new ArrayList<>();
        for(ZakazaniPregled appointment : allAppointments){
            if(appointment.getDatumIVremePregleda().after(date)){
                appointments.add(appointment);
            }
        }

        return appointments;
    }

    @Override
    public List<ZakazaniPregled> getAppointmentByLBZAndDate(long lbz, Timestamp date) {

        return scheduledAppointmentRepository.findByLBZLekaraAndAndDatumIVremePregleda(lbz, date);
    }

}