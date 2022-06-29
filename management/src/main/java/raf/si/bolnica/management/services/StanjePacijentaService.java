package raf.si.bolnica.management.services;
import org.springframework.stereotype.Component;
import raf.si.bolnica.management.entities.StanjePacijenta;
public interface StanjePacijentaService {

    StanjePacijenta saveStanje(StanjePacijenta stanje);
}
