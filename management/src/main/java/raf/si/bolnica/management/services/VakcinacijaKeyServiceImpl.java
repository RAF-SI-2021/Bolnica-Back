package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.VakcinacijaKey;
import raf.si.bolnica.management.repositories.VakcinacijaKeyRepository;

@Service
@Transactional("transactionManager")
public class VakcinacijaKeyServiceImpl implements VakcinacijaKeyService {

    @Autowired
    VakcinacijaKeyRepository vakcinacijaKeyRepository;

    @Override
    public VakcinacijaKey saveVakcinacijaKey(VakcinacijaKey vakcinacijaKey) {
        return vakcinacijaKeyRepository.save(vakcinacijaKey);
    }
}
