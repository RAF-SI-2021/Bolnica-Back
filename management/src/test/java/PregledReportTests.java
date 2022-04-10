import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.enums.CountryCode;
import raf.si.bolnica.management.entities.enums.Pol;
import raf.si.bolnica.management.entities.enums.RezultatLecenja;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreatePregledReportRequestDTO;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.response.PacijentResponseDTO;
import raf.si.bolnica.management.services.PregledService;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PregledReportTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    ManagementController managementController;


    CreatePregledReportRequestDTO getRequest() {
        CreatePregledReportRequestDTO request = new CreatePregledReportRequestDTO();

        request.setZaposleniId(1L);
        request.setLbp(UUID.randomUUID());
        request.setDijagnoza("dijagnoza");
        request.setGlavneTegobe("glavnetegobe");
        request.setIndikatorPoverljivosti(false);
        request.setLicnaAnamneza("licnaanamneza");
        request.setObjektivniNalaz("objektivninalaz");
        request.setMisljenjePacijenta("misljenjepacijenta");
        request.setRezultatLecenja(RezultatLecenja.OPORAVLJEN);
        request.setOpisTekucegStanja("opistekucegstanja");
        request.setPorodicnaAnamneza("porodicnaanamneza");
        request.setSadasnjaBolest("sadasnjabolest");
        request.setSavet("savet");
        request.setPredlozenaTerapija("predlozenaterapija");

        return request;
    }

    @Test
    public void testCreatePregledReportUnauthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.ADMIN);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testCreatePregledReportInvalidRequest() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreatePregledReportRequestDTO request = getRequest();
        request.setLbp(null);

        ResponseEntity<?> response = managementController.createPregledReport(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(406);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("Licni broj pacijenta(Lbp) je obavezno polje!");
    }
}
