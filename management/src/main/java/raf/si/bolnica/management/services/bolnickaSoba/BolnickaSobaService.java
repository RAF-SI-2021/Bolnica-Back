package raf.si.bolnica.management.services.bolnickaSoba;


import raf.si.bolnica.management.entities.BolnickaSoba;

import java.util.List;

public interface BolnickaSobaService {

    BolnickaSoba findById(long id);

    BolnickaSoba save(BolnickaSoba bolnickaSoba);

    List<BolnickaSoba> findAllByDepartmentId(long departmentId);

    int decrement(BolnickaSoba bolnickaSoba);

    int increment(BolnickaSoba bolnickaSoba);
}
