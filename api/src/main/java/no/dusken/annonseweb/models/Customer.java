package no.dusken.annonseweb.models;

import no.dusken.common.model.DuskenObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "customer")
@SequenceGenerator(name = "customer_seq", sequenceName = "customer_id_seq")
public class Customer extends DuskenObject{

    @NotNull
    private String name;

    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String address;

    @ElementCollection
    private List<String> industryTags = new ArrayList<String>();

    private String homepage;

    @OneToMany(fetch = LAZY, mappedBy = "customer")
    private List<AnnonseNote> annonseNotes = new ArrayList<AnnonseNote>();

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "customer")
    private List<Sale> sales = new ArrayList<Sale>();

    @OneToMany(fetch = LAZY, mappedBy = "customer")
    private List<ContactPerson> contactPersons = new ArrayList<ContactPerson>();

    public Customer(){}

    public Customer(String name, String email, String phoneNumber,
                    String address){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Customer(String name, String email, String phoneNumber, String address,
                    String homepage, List<String> industryTags, List<AnnonseNote> annonseNotes,
                    List<Sale> sales) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.industryTags = industryTags;
        this.homepage = homepage;
        this.annonseNotes = annonseNotes;
        this.sales = sales;
    }

    /**
     * Clones all information about this customer if <code>other != null</code>.
     * @param other Customer to clone information from
     */
    public void cloneFrom(Customer other) {
        if (other == null){
            return;
        }
        this.name = other.name;
        this.contactPersons = other.contactPersons;
        this.email = other.email;
        this.phoneNumber = other.phoneNumber;
        this.address = other.address;
        this.industryTags = other.industryTags;
        this.homepage = other.homepage;
        this.annonseNotes = other.annonseNotes;
        this.sales = other.sales;
    }

    public void addSale(Sale sale){
        sales.add(sale);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIndustryTags(List<String> industryTags) {
        this.industryTags = industryTags;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getIndustryTags() {
        return industryTags;
    }

    public List<AnnonseNote> getAnnonseNotes() {
        return annonseNotes;
    }

    public void setAnnonseNotes(List<AnnonseNote> annonseNotes) {
        this.annonseNotes = annonseNotes;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
        this.contactPersons = contactPersons;
    }
}
