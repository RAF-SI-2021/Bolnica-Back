import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.entities.enums.PrispecePacijenta;
import raf.si.bolnica.management.entities.enums.StatusPregleda;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.SearchForAppointmentDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;
import raf.si.bolnica.management.services.ScheduledAppointmentService;
import raf.si.bolnica.management.services.PacijentService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentTests {
    @Mock
    LoggedInUser loggedInUser;

    @Mock
    ScheduledAppointmentService appointmentService;

    @Mock
    PacijentService pacijentService;

    @InjectMocks
    ManagementController managementController;

    @Test
    public void testSetAppointmentTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        CreateScheduledAppointmentRequestDTO requestDTO = new CreateScheduledAppointmentRequestDTO();
        requestDTO.setDateAndTimeOfAppointment(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setExaminationEmployeeId(UUID.randomUUID());
        ResponseEntity<?> response = managementController.setAppointment(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testUpdateAppointmentTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateAppointmentStatusDTO requestDTO = new UpdateAppointmentStatusDTO();
        requestDTO.setAppointmentStatus(StatusPregleda.U_TOKU.toString());
        when(appointmentService.fetchById(any(Long.class))).thenReturn(new ZakazaniPregled());
        ResponseEntity<?> response = managementController.updateAppointmentStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testUpdateArrivalTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateArrivalStatusDTO requestDTO = new UpdateArrivalStatusDTO();
        requestDTO.setArrivalStatus(PrispecePacijenta.PRIMLJEN.toString());
        when(appointmentService.fetchById(any(Long.class))).thenReturn(new ZakazaniPregled());
        ResponseEntity<?> response = managementController.updateArrivalStatus(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testListAppointmentLBZ() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchForAppointmentDTO requestDTO = new SearchForAppointmentDTO();
        requestDTO.setLbz(UUID.randomUUID().toString());
        List<ZakazaniPregled> appointments = new LinkedList<>();
        ZakazaniPregled zp1 = new ZakazaniPregled();
        zp1.setZakazaniPregledId(1);
        zp1.setDatumIVremePregleda(Timestamp.valueOf(LocalDateTime.now().minusDays(2)));
        ZakazaniPregled zp2 = new ZakazaniPregled();
        zp2.setZakazaniPregledId(2);
        zp2.setDatumIVremePregleda(Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
        appointments.add(zp1);
        appointments.add(zp2);
        when(appointmentService.getAppointmentByLBZ(any(UUID.class))).thenReturn(appointments);
        ResponseEntity<?> response = managementController.listAppointmentsByLBZ(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        List<ZakazaniPregled> l = (List)response.getBody();

        assertThat(l.size()).isEqualTo(1);
        assertThat(l.get(0).getZakazaniPregledId()).isEqualTo(zp2.getZakazaniPregledId());
    }
}
