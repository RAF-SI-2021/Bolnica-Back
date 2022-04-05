package raf.si.bolnica.management.service;

import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.requests.CreatePregledRequestDTO;

import java.util.Optional;

public interface PregledService {

    Pregled getPregledById(Long id);

    Pregled createPregled(CreatePregledRequestDTO requestDTO);

}
