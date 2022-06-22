import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.controllers.OdeljenjeController;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.service.OdeljenjeService;

import java.sql.Date;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OdeljenjeControllerTest {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    OdeljenjeService odeljenjeService;

    @InjectMocks
    OdeljenjeController odeljenjeController;

    @Test
    public void whenUnauthorizedRole_ExpectStatusCode403() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        ResponseEntity<?> response = odeljenjeController.searchForDepartmentByName("Hirurgija");

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenNoDepartmentExists_ExpectStatusCode204() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.ROLE_DR_SPEC_ODELJENJA);

        ZdravstvenaUstanova zdravstvenaUstanova = new ZdravstvenaUstanova();
        zdravstvenaUstanova.setAdresa("Heroja Milana Tepića 1, Beograd");
        zdravstvenaUstanova.setDatumOsnivanja(new Date(Calendar.getInstance().getTime().getTime()));
        zdravstvenaUstanova.setDelatnost("Ginekologija i akušerstvo");
        zdravstvenaUstanova.setMesto("Beograd");
        zdravstvenaUstanova.setNaziv("Kliničko-bolnički centar \"Dragiša Mišović\"");
        zdravstvenaUstanova.setPoslovniBrojBolnice(1234);
        zdravstvenaUstanova.setSkracenNaziv("KBC Dragiša Mišović");

        Odeljenje odeljenje = new Odeljenje();
        odeljenje.setNaziv("Hirurgija");
        odeljenje.setBolnica(zdravstvenaUstanova);
        odeljenje.setPoslovniBrojOdeljenja(12345);

        when(loggedInUser.getRoles()).thenReturn(roles);

        ResponseEntity<?> response = odeljenjeController.searchForDepartmentByName("Hirurgija");

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    public void whenFindByPbb_ExpectStatusCode200() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_RECEPCIONER");

        ZdravstvenaUstanova zdravstvenaUstanova = new ZdravstvenaUstanova();
        zdravstvenaUstanova.setAdresa("Heroja Milana Tepića 1, Beograd");
        zdravstvenaUstanova.setDatumOsnivanja(new Date(Calendar.getInstance().getTime().getTime()));
        zdravstvenaUstanova.setDelatnost("Ginekologija i akušerstvo");
        zdravstvenaUstanova.setMesto("Beograd");
        zdravstvenaUstanova.setNaziv("Kliničko-bolnički centar \"Dragiša Mišović\"");
        zdravstvenaUstanova.setPoslovniBrojBolnice(1234);
        zdravstvenaUstanova.setSkracenNaziv("KBC Dragiša Mišović");

        Odeljenje odeljenje = new Odeljenje();
        odeljenje.setNaziv("Hirurgija");
        odeljenje.setBolnica(zdravstvenaUstanova);
        odeljenje.setPoslovniBrojOdeljenja(12345);

        when(loggedInUser.getRoles()).thenReturn(roles);

        ResponseEntity<?> response = odeljenjeController.getAllDepartments();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

}