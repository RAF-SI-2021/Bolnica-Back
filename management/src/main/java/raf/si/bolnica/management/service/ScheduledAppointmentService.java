package raf.si.bolnica.management.service;

import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;

import java.sql.Timestamp;
import java.util.List;


public interface ScheduledAppointmentService {

        ZakazaniPregled setAppointment(CreateScheduledAppointmentRequestDTO createScheduledAppointmentRequestDTO);
        ZakazaniPregled updateAppointment(UpdateAppointmentStatusDTO updateAppointmentStatusDTO);
        ZakazaniPregled updateArrival(UpdateArrivalStatusDTO updateArrivalStatusDTO);
        List<ZakazaniPregled> getAppointmentByLBZ(long lbz) ;
        List<ZakazaniPregled> getAppointmentByLBZAndDate(long lbz, Timestamp date);
}