import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.OtpusnaListaController;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateOtpusnaListaDTO;
import raf.si.bolnica.management.services.otpusnaLista.OtpusnaListaService;

import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OtpusnaListaTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    OtpusnaListaService otpusnaListaService;

    @InjectMocks
    OtpusnaListaController otpusnaListaController;

    public CreateOtpusnaListaDTO getRequest(){
        CreateOtpusnaListaDTO request = new CreateOtpusnaListaDTO();
        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setAnalize("analize");
        request.setAnamneza("anamneza");
        request.setTerapija("terapija");
        request.setPbo(1);
        request.setZakljucak("zakljucak");
        request.setPrateceDijagnoze("pratece bolesti");
        request.setTokBolesti("Tok bolesti");

        return request;
    }

    @Test
    public void testCreateOtpusnaListaInvalidRequest(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreateOtpusnaListaDTO request = getRequest();
        request.setLbp(null);

        ResponseEntity<?> response = otpusnaListaController.registerOtpusnaLista(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(406);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("LBP je obavezno polje");
    }

    @Test
    public void testCreateOtpusnaListaSuccess() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreateOtpusnaListaDTO request = getRequest();
        ResponseEntity<?> response = otpusnaListaController.registerOtpusnaLista(request);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

}
