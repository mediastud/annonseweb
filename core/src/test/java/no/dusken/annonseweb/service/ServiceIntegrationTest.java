package no.dusken.annonseweb.service;

import no.dusken.annonseweb.models.*;
import no.dusken.common.model.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author Marvin B. Lillehaug <lillehau@underdusken.no>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/integrationtest-jpa.xml"})
@TransactionConfiguration
@Transactional
public class ServiceIntegrationTest {

    @Autowired
    private SalesService salesService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AnnonsePersonService annonsePersonService;

    @Test
    public void testSave(){


        Ad ad = new PrintedAd(new BigDecimal("100"), new BigDecimal("10"));
        Customer customer = new Customer("customer", "mail@mail.mail", "12345678", "address");
        // I save customer first, since at least first version of this program should not save more than one entity at a time
        customerService.saveAndFlush(customer);
        AnnonsePerson createdUser = getPerson();

        Sale s = new Sale("Appointment name", new LinkedList<Ad>(Collections.singleton(ad)),
                customer, createdUser, true);

        Sale sale = salesService.saveAndFlush(s);
        assertNotNull(sale);
        assertNotNull(sale.getId());
        assertEquals(ad, sale.getAds().get(0));
        assertEquals(customer, sale.getCustomer());


    }

    private AnnonsePerson getPerson() {
        AnnonsePerson createdUser = new AnnonsePerson();
        createdUser.setPrincipal("UserName");
        annonsePersonService.saveAndFlush(createdUser);
        return createdUser;
    }

}
