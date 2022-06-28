package raf.si.bolnica.management.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.entities.enums.StatusPregleda;
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
        long appointmentDuration = 60;
        Timestamp startTime = new Timestamp(appointment.getDatumIVremePregleda().getTime());
        Timestamp endTime = new Timestamp(startTime.getTime());
        endTime.setTime(startTime.getTime() + TimeUnit.MINUTES.toMillis(appointmentDuration));
        Optional<ZakazaniPregled> toSave = scheduledAppointmentRepository
                .findByLbzLekaraAndDatumIVremePregledaBetweenAndStatusPregleda(appointment.getLbzLekara(), startTime, endTime, StatusPregleda.ZAKAZANO);
        if (toSave.isPresent()) {
            System.out.println(toSave.get().getStatusPregleda());
            throw new AccessDeniedException("Already appointed!");
        }
        return scheduledAppointmentRepository.save(appointment);
    }

    @Override
    public ZakazaniPregled saveAppointmentStatus(ZakazaniPregled appointment) {
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