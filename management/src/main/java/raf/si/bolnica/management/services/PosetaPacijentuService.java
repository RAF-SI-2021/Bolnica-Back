package raf.si.bolnica.management.services;


import raf.si.bolnica.management.entities.PosetaPacijentu;

import java.util.List;
import java.util.UUID;

public interface PosetaPacijentuService {

    PosetaPacijentu save(PosetaPacijentu posetaPacijentu);

    List<PosetaPacijentu> findAllByLBP(UUID lbp);
}
