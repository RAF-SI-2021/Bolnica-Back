package raf.si.bolnica.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, unique = true)
    private long licni_broj_zaposlenog;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private Date datum_rodjena;

    @Column(nullable = false)
    private String pol;

    @Column(nullable = false)
    private String jmbg;

    @Column(nullable = false)
    private String adresa_stanovanja;

    @Column(nullable = false)
    private String mesto_stanovanja;

    @Column(nullable = true)
    private String kontakt_telefon;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String titula;

    @Column(nullable = false)
    private String zanimanje;

    @Column(unique = true, nullable = false)
    private String korisnickoime;

    @ManyToOne
    private Odeljenje odeljenje;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean obrisan = false;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public long getLicni_broj_zaposlenog() {
        return licni_broj_zaposlenog;
    }

    public void setLicni_broj_zaposlenog(long licni_broj_zaposlenog) {
        this.licni_broj_zaposlenog = licni_broj_zaposlenog;
    }

    public Date getDatum_rodjena() {
        return datum_rodjena;
    }

    public void setDatum_rodjena(Date datum_rodjena) {
        this.datum_rodjena = datum_rodjena;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getAdresa_stanovanja() {
        return adresa_stanovanja;
    }

    public void setAdresa_stanovanja(String adresa_stanovanja) {
        this.adresa_stanovanja = adresa_stanovanja;
    }

    public String getMesto_stanovanja() {
        return mesto_stanovanja;
    }

    public void setMesto_stanovanja(String mesto_stanovanja) {
        this.mesto_stanovanja = mesto_stanovanja;
    }

    public String getKontakt_telefon() {
        return kontakt_telefon;
    }

    public void setKontakt_telefon(String kontakt_telefon) {
        this.kontakt_telefon = kontakt_telefon;
    }

    public String getTitula() {
        return titula;
    }

    public void setTitula(String titula) {
        this.titula = titula;
    }

    public String getZanimanje() {
        return zanimanje;
    }

    public void setZanimanje(String zanimanje) {
        this.zanimanje = zanimanje;
    }

    public String getKorisnickoime() {
        return korisnickoime;
    }

    public void setKorisnickoime(String korisnickoime) {
        this.korisnickoime = korisnickoime;
    }

    public Odeljenje getOdeljenje() {
        return odeljenje;
    }

    public void setOdeljenje(Odeljenje odeljenje) {
        this.odeljenje = odeljenje;
    }

    public boolean isObrisan() {
        return obrisan;
    }

    public void setObrisan(boolean obrisan) {
        this.obrisan = obrisan;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {}

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
