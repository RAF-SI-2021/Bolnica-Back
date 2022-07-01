import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.LekarskiIzvestajController;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.LekarskiIzvestajDTO;
import raf.si.bolnica.management.services.lekarskiIzvestaj.LekarskiIzvestajService;

import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LekarskiIzvestajTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    LekarskiIzvestajService lekarskiIzvestajService;

    @InjectMocks
    LekarskiIzvestajController lekarskiIzvestajController;

    LekarskiIzvestajDTO getRequest(){
        LekarskiIzvestajDTO req = new LekarskiIzvestajDTO();
        req.setLbp("237e9877-e79b-12d4-a765-321741963000");
        req.setDijagnoza("Dijagnoza");
        req.setIndikatorPoverljivosti(false);
        req.setSavet("Savet");
        req.setObjektivniNalaz("Objektivni nalaz");
        req.setPredlozenaTerapija("Predlozena terapija");

        return req;
    }

    @Test
    public void testCreateLekarskiIzvestajInvalidRequest(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        LekarskiIzvestajDTO req = getRequest();
        req.setLbp(null);

        ResponseEntity<?> response = lekarskiIzvestajController.registerLekarskiIzvestaj(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(406);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("LBP je obavezno polje");


    }


    @Test
    public void testCreateLekarskiIzvestajSuccess(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);
        LekarskiIzvestajDTO req = getRequest();
        ResponseEntity<?> response = lekarskiIzvestajController.registerLekarskiIzvestaj(req);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }
}
