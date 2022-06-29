package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.ZakazaniTerminPrijema;
import raf.si.bolnica.management.entities.enums.StatusTermina;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface ZakazaniTerminPrijemaService {

    ZakazaniTerminPrijema save(ZakazaniTerminPrijema zakazaniTerminPrijema);

    List<ZakazaniTerminPrijema> getAll(long odeljenjeId, Date datum, UUID lbp);

    void setStatus(long id, StatusTermina status);

}
