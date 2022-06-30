package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.ZdravstveniKarton;

public interface IstorijaBolestiService {

    IstorijaBolesti saveIstorijaBolesti(IstorijaBolesti istorijaBolesti);

    IstorijaBolesti fetchByZdravstveniKartonPodaciValidni(ZdravstveniKarton zk, boolean podaci);
}
