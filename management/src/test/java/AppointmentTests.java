import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.SearchForAppointmentDTO;
import raf.si.bolnica.management.requests.UpdateAppointmentStatusDTO;
import raf.si.bolnica.management.requests.UpdateArrivalStatusDTO;
import raf.si.bolnica.management.service.ScheduledAppointmentService;
import raf.si.bolnica.management.services.PacijentService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

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
        managementController.setAppointment(requestDTO);
    }

    @Test
    public void testUpdateAppointmentTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateAppointmentStatusDTO requestDTO = new UpdateAppointmentStatusDTO();
        requestDTO.setAppointmentStatus("status");
        managementController.updateAppointmentStatus(requestDTO);
    }

    @Test
    public void testUpdateArrivalTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateArrivalStatusDTO requestDTO = new UpdateArrivalStatusDTO();
        requestDTO.setArrivalStatus("status");
        managementController.updateArrivalStatus(requestDTO);
    }

    @Test
    public void testListAppointmentLBZ() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchForAppointmentDTO requestDTO = new SearchForAppointmentDTO();
        managementController.listAppointmentsByLBZ(requestDTO);
    }
}
