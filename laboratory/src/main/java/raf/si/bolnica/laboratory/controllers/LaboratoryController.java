package raf.si.bolnica.laboratory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.services.LaboratorijskiRadniNalogService;
import raf.si.bolnica.laboratory.services.UputService;
import raf.si.bolnica.laboratory.services.ZakazanLaboratorijskiPregledService;

@RestController
@RequestMapping("/api")
public class LaboratoryController {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private ZakazanLaboratorijskiPregledService laboratorijskiPregledService;

    @Autowired
    private UputService uputService;

    @Autowired
    private LaboratorijskiRadniNalogService radniNalogService;





}
