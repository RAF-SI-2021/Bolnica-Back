import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateScheduledAppointmentRequestDTO;
import raf.si.bolnica.management.requests.CreateZakazaniTerminPrijemaRequestDTO;
import raf.si.bolnica.management.services.ZakazaniTerminPrijemaService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ZakazaniTerminiPrijemaTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    ZakazaniTerminPrijemaService zakazaniTerminPrijemaService;

    @InjectMocks
    ManagementController managementController;

    @Test
    public void testCreatePrijemUnauthorizedTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        CreateZakazaniTerminPrijemaRequestDTO requestDTO = new CreateZakazaniTerminPrijemaRequestDTO();
        requestDTO.setNapomena("Napomena");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setDatumVremePrijema(Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<?> response = managementController.createTerminPrijema(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }


}
