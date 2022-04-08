package raf.si.bolnica.management.service;

import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;


public interface ScheduledAppointmentService {

        ZakazaniPregled setAppointment(CreateScheduledAppointmentRequestDTO createScheduledAppointmentRequestDTO);
        ZakazaniPregled updateAppointment(UpdateAppointmentStatusDTO updateAppointmentStatusDTO);
        ZakazaniPregled updateArrival(UpdateArrivalStatusDTO updateArrivalStatusDTO);
}