package raf.si.bolnica.management.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vakcinacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "vakcina_id")
    private Vakcina vakcina;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "zdravstveni_karton_id")
    private ZdravstveniKarton zdravstveniKarton;

    @Column(nullable = false)
    private Date datumVakcinacije;

    @Column(nullable = false)
    private Boolean obrisan = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vakcina getVakcina() {
        return vakcina;
    }

    public void setVakcina(Vakcina vakcina) {
        this.vakcina = vakcina;
    }

    public ZdravstveniKarton getZdravstveniKarton() {
        return zdravstveniKarton;
    }

    public void setZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
        this.zdravstveniKarton = zdravstveniKarton;
    }

    public Date getDatumVakcinacije() {
        return datumVakcinacije;
    }

    public void setDatumVakcinacije(Date datumVakcinacije) {
        this.datumVakcinacije = datumVakcinacije;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }
}
