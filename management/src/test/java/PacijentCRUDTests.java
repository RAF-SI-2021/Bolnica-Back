import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.entities.enums.CountryCode;
import raf.si.bolnica.management.entities.enums.Pol;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.response.PacijentCRUDResponseDTO;
import raf.si.bolnica.management.services.PacijentService;
import raf.si.bolnica.management.services.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    public void testPacijentCreateUnauthorized() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.ADMIN);

        when(loggedInUser.getRoles()).thenReturn(roles);

        PacijentCRUDRequestDTO request = getRequest();

        ResponseEntity<?> response = managementController.createPatient(request);

        assertThat(response.getStatusCodeValue()!=200);
    }

    @Test
    public void testPacijentCreateInvalidRequest() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);


        PacijentCRUDRequestDTO request = getRequest();
        request.setPol(null);

        ResponseEntity<?> response = managementController.createPatient(request);

        assertThat(response.getStatusCodeValue()!=200);

        assertThat(response.getBody() instanceof String);

        assertThat((response.getBody()).equals("Pol je obavezno polje!"));
    }

    @Test
    public void testPacijentCreateRemoveSuccess() {

        Set<String> roles = new TreeSet<>();

        roles.add(Constants.MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.savePacijent(any(Pacijent.class))).thenAnswer(i -> i.getArguments()[0]);

        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenAnswer(i -> i.getArguments()[0]);

        PacijentCRUDRequestDTO request = getRequest();

        ResponseEntity<?> responseCreate = managementController.createPatient(request);

        assertThat(responseCreate.getStatusCodeValue()==200);

        assertThat(responseCreate.getBody() instanceof PacijentCRUDResponseDTO);

        Pacijent test = new Pacijent();
        test.setZdravstveniKarton(new ZdravstveniKarton());

        when(pacijentService.fetchPacijentById(Long.valueOf(1))).thenReturn(test);

        TypedQuery<AlergenZdravstveniKarton> query1 = mock(TypedQuery.class);
        when(query1.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),eq(AlergenZdravstveniKarton.class))).thenReturn(query1);

        TypedQuery<Vakcinacija> query2 = mock(TypedQuery.class);
        when(query2.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),eq(Vakcinacija.class))).thenReturn(query2);

        TypedQuery<Operacija> query3 = mock(TypedQuery.class);
        when(query3.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),eq(Operacija.class))).thenReturn(query3);

        TypedQuery<Pregled> query4 = mock(TypedQuery.class);
        when(query4.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),eq(Pregled.class))).thenReturn(query4);

        TypedQuery<IstorijaBolesti> query5 = mock(TypedQuery.class);
        when(query5.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),eq(IstorijaBolesti.class))).thenReturn(query5);

        ResponseEntity<?> responseRemove = managementController.removePatient(Long.valueOf(1));

        assertThat(responseRemove.getStatusCodeValue()==200);
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

        assertThat(responseCreate.getStatusCodeValue()==200);

        assertThat(responseCreate.getBody() instanceof PacijentCRUDResponseDTO);

        when(pacijentService.fetchPacijentById(Long.valueOf(1))).thenReturn(new Pacijent());

        request.setBrojDece(5);

        ResponseEntity<?> responseUpdate = managementController.updatePatient(request,Long.valueOf(1));

        assertThat(responseUpdate.getStatusCodeValue()==200);
    }
}
