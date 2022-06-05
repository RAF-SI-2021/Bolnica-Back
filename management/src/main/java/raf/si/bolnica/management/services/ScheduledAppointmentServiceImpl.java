package raf.si.bolnica.management.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.repositories.ScheduledAppointmentRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ScheduledAppointmentServiceImpl implements ScheduledAppointmentService {

    @Autowired
    private ScheduledAppointmentRepository scheduledAppointmentRepository;

    @Override
    public ZakazaniPregled saveAppointment(ZakazaniPregled appointment) {
        long appointmentDuration = 15;
        Timestamp startTime = appointment.getDatumIVremePregleda();
        Timestamp endTime = appointment.getDatumIVremePregleda();
        endTime.setTime(appointment.getDatumIVremePregleda().getTime() + TimeUnit.MINUTES.toMillis(appointmentDuration));
        Optional<ZakazaniPregled> toSave = scheduledAppointmentRepository
                .findByLbzLekaraAndDatumIVremePregledaBetween(appointment.getLbzLekara(), startTime, endTime);
        if (toSave.isPresent()) {
            throw new AccessDeniedException("Already appointed!");
        }
        return scheduledAppointmentRepository.save(appointment);
    }

    @Override
    public List<ZakazaniPregled> getAppointmentByLBZ(UUID lbz) {
        return scheduledAppointmentRepository.findByLbzLekara(lbz);
    }

    @Override
    public ZakazaniPregled fetchById(long id) {
        return scheduledAppointmentRepository.getZakazaniPregledByZakazaniPregledId(id);
    }

    @Override
    public List<ZakazaniPregled> getAppointmentByLBZAndDate(UUID lbz, Timestamp date) {
        return scheduledAppointmentRepository.findByLbzLekaraAndAndDatumIVremePregleda(lbz, date);
    }

}