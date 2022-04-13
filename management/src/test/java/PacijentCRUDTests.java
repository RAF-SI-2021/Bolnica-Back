import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.entities.enums.CountryCode;
import raf.si.bolnica.management.entities.enums.Pol;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.response.PacijentResponseDTO;
import raf.si.bolnica.management.services.PacijentService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PacijentCRUDTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    PacijentService pacijentService;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    ManagementController managementController;

    PacijentCRUDRequestDTO getRequest() {
        PacijentCRUDRequestDTO request = new PacijentCRUDRequestDTO();

        request.setJmbg("123456789");
        request.setIme("Pacijent");
        request.setImeRoditelja("Pacijent");
        request.setPrezime("Pacijent");
        request.setPol(Pol.MUSKI);
        request.setDatumRodjenja(Date.valueOf("1990-01-01"));
        request.setMestoRodjenja("Loznica");
        request.setZemljaDrzavljanstva(CountryCode.SRB);
        request.setZemljaStanovanja(CountryCode.SRB);

        return request;
    }


    @Test
    public void testPacijentCreateInvalidRequest() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);


        PacijentCRUDRequestDTO request = getRequest();
        request.setPol(null);

        ResponseEntity<?> response = managementController.createPatient(request);

        assertThat(response.getStatusCodeValue()).isNotEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("Pol je obavezno polje!");
    }

    @Test
    public void testPacijentCreateRemoveSuccess() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.savePacijent(any(Pacijent.class))).thenAnswer(i -> i.getArguments()[0]);

        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenAnswer(i -> i.getArguments()[0]);

        PacijentCRUDRequestDTO request = getRequest();

        ResponseEntity<?> responseCreate = managementController.createPatient(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseCreate.getBody()).isInstanceOf(PacijentResponseDTO.class);

        Pacijent test = new Pacijent();
        test.setZdravstveniKarton(new ZdravstveniKarton());

        when(pacijentService.fetchPacijentById(1L)).thenReturn(test);

        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenReturn(query);

        ResponseEntity<?> responseRemove = managementController.removePatient(1L);

        assertThat(responseRemove.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testPacijentCreateUpdateSuccess() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.savePacijent(any(Pacijent.class))).thenAnswer(i -> i.getArguments()[0]);

        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenAnswer(i -> i.getArguments()[0]);

        PacijentCRUDRequestDTO request = getRequest();

        ResponseEntity<?> responseCreate = managementController.createPatient(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseCreate.getBody()).isInstanceOf(PacijentResponseDTO.class);

        when(pacijentService.fetchPacijentById(1L)).thenReturn(new Pacijent());

        request.setBrojDece(5);

        ResponseEntity<?> responseUpdate = managementController.updatePatient(request, 1L);

        assertThat(responseUpdate.getStatusCodeValue()).isEqualTo(200);
    }
}
