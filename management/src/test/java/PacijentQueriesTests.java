import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.*;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.FilterPatientsRequestDTO;
import raf.si.bolnica.management.requests.IstorijaBolestiRequestDTO;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.requests.PreglediRequestDTO;
import raf.si.bolnica.management.response.PacijentPodaciResponseDTO;
import raf.si.bolnica.management.response.PacijentResponseDTO;
import raf.si.bolnica.management.services.PacijentService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacijentQueriesTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    EntityManager entityManager;

    @Mock
    PacijentService pacijentService;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

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

    Pacijent getPacijent() {
        Pacijent pacijent = new Pacijent();

        pacijent.setLbp(UUID.randomUUID());
        pacijent.setJmbg("123456789");
        pacijent.setIme("Pacijent");
        pacijent.setImeRoditelja("Pacijent");
        pacijent.setPrezime("Pacijent");
        pacijent.setPol(Pol.MUSKI);
        pacijent.setDatumRodjenja(Date.valueOf("1990-01-01"));
        pacijent.setMestoRodjenja("Loznica");
        pacijent.setZemljaDrzavljanstva(CountryCode.SRB);
        pacijent.setZemljaStanovanja(CountryCode.SRB);

        return pacijent;
    }

    List<Pregled> getPregledi() {
        List<Pregled> pregledi = new ArrayList<>();
        Pregled pregled1 = new Pregled();
        pregled1.setDatumPregleda(Date.valueOf(LocalDate.now()));
        pregled1.setObjektivniNalaz("nalaz1");
        pregled1.setIndikatorPoverljivosti(false);

        Pregled pregled2 = new Pregled();
        pregled1.setDatumPregleda(Date.valueOf(LocalDate.now()));
        pregled1.setObjektivniNalaz("nalaz2");
        pregled1.setIndikatorPoverljivosti(false);


        pregledi.add(pregled1);
        pregledi.add(pregled2);

        return  pregledi;
    }

    List<IstorijaBolesti> getIstorija() {
        List<IstorijaBolesti> istorija = new ArrayList<>();
        IstorijaBolesti i1 = new IstorijaBolesti();
        i1.setDijagnoza("dijgnoza");
        i1.setIndikatorPoverljivosti(true);
        i1.setDatumPocetkaZdravstvenogProblema(Date.valueOf(LocalDate.now()));
        i1.setRezultatLecenja(RezultatLecenja.OPORAVLJEN);
        i1.setOpisTekucegStanja("stanje");
        i1.setPodatakValidanOd(Date.valueOf(LocalDate.now()));
        i1.setPodatakValidanDo(Date.valueOf(LocalDate.now()));
        i1.setPodaciValidni(true);

        istorija.add(i1);
        return istorija;
    }

    @Test
    public void testFetchPatientData() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        Pacijent p = getPacijent();

        ZdravstveniKarton zk = new ZdravstveniKarton();
        zk.setKrvnaGrupa(KrvnaGrupa.A);
        zk.setRhFaktor(RhFaktor.MINUS);

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenReturn(query);

        ResponseEntity<?> response = managementController.fetchPatientDataLbp(p.getLbp().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(PacijentPodaciResponseDTO.class);

        assertThat(((PacijentPodaciResponseDTO)response.getBody()).getRhFaktor()).isEqualTo(RhFaktor.MINUS);

        assertThat(((PacijentPodaciResponseDTO)response.getBody()).getKrvnaGrupa()).isEqualTo(KrvnaGrupa.A);

    }

    @Test
    public void testFetchPregledi() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        Pacijent p = getPacijent();

        List<Pregled> pregledi = getPregledi();


        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        TypedQuery<Pregled> query1 = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT i FROM Pregled p WHERE p.zdravstveniKarton = :zk AND p.indikatorPoverljivosti = false"),
                any(Class.class))).thenReturn(query1);

        when(query1.getResultList()).thenReturn(pregledi);

        ResponseEntity<?> response = managementController.fetchPreglediLbp(new PreglediRequestDTO(),p.getLbp().toString(),1,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(pregledi.size());

    }

    @Test
    public void testFetchIstorijaBolesti() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        roles.add(Constants.SPECIJLISTA_POV);

        Pacijent p = getPacijent();

        List<IstorijaBolesti> istorija = getIstorija();


        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(pacijentService.fetchPacijentByLbp(p.getLbp())).thenReturn(p);

        TypedQuery<IstorijaBolesti> query1 = mock(TypedQuery.class);
        when(entityManager.createQuery(eq("SELECT i FROM IstorijaBolesti i WHERE i.zdravstveniKarton = :zk"),
                any(Class.class))).thenReturn(query1);

        when(query1.getResultList()).thenReturn(istorija);

        ResponseEntity<?> response = managementController.fetchIstorijaBolestiLbp(new IstorijaBolestiRequestDTO(),p.getLbp().toString(),1,2);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(istorija.size());

    }

    @Test
    public void testFilterPacijent() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        roles.add(Constants.SPECIJLISTA_POV);

        Pacijent p = getPacijent();

        List<Pacijent> pacijenti = new ArrayList<>();

        pacijenti.add(p);

        when(loggedInUser.getRoles()).thenReturn(roles);

        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(eq("SELECT p FROM Pacijent p"),
                any(Class.class))).thenReturn(query);

        when(query.getResultList()).thenReturn(pacijenti);

        ResponseEntity<?> response = managementController.filterPatients(new FilterPatientsRequestDTO());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(List.class);

        assertThat(((List)response.getBody()).size()).isEqualTo(pacijenti.size());

    }

    /*
    @Test
    public void testCreateFetchPatientZdravstveniKarton() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        roles.add(Constants.NACELNIK);

        when(loggedInUser.getRoles()).thenReturn(roles);

        PacijentCRUDRequestDTO request = getRequest();

        when(pacijentService.savePacijent(any(Pacijent.class))).thenReturn(new Pacijent());

        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenReturn(new ZdravstveniKarton());

        ResponseEntity<?> responseCreate = managementController.createPatient(request);

        assertThat(responseCreate.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseCreate.getBody()).isInstanceOf(PacijentResponseDTO.class);

        UUID lbp = ((PacijentResponseDTO) Objects.requireNonNull(responseCreate.getBody())).getLbp();

        Pacijent p = new Pacijent();

        ZdravstveniKarton zk = new ZdravstveniKarton();

        p.setZdravstveniKarton(zk);

        p.setLbp(lbp);

        zk.setPacijent(p);

//        when(pacijentService.fetchPacijentByLbp(lbp)).thenReturn(p);

        ResponseEntity<?> responseFetchPatient = managementController.fetchPatientLbp(lbp.toString());

        assertThat(responseFetchPatient.getStatusCodeValue()).isEqualTo(200);

        assertThat(responseFetchPatient.getBody()).isInstanceOf(PacijentResponseDTO.class);

        assertThat(((PacijentResponseDTO)responseFetchPatient.getBody()).getLbp()).isEqualTo(lbp);

        ResponseEntity<?> responseFetchZdravstveniKarton = managementController.fetchZdravstveniKartonLbp(lbp.toString());

        assertThat(responseFetchZdravstveniKarton.getStatusCodeValue()).isEqualTo(200);
    }
     */
}
