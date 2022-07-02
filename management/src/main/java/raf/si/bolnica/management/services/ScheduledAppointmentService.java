package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.ZakazaniPregled;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


public interface ScheduledAppointmentService {

    ZakazaniPregled saveAppointment(ZakazaniPregled appointment);

    ZakazaniPregled saveAppointmentStatus(ZakazaniPregled appointment);

    List<ZakazaniPregled> getAppointmentByLBZ(UUID lbz);

    List<ZakazaniPregled> getAppointmentByLBZAndDate(UUID lbz, Timestamp date);

    ZakazaniPregled fetchById(long id);
}