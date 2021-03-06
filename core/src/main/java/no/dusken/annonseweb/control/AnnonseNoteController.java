/*
 * Copyright 2013 Studentmediene i Trondheim AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.dusken.annonseweb.control;

import customeditors.ResolveByUsernameEditor;
import customeditors.ResolveCalendarEditor;
import no.dusken.annonseweb.models.*;
import no.dusken.annonseweb.service.*;
import no.dusken.common.editor.BindByIdEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;

/**
 * <code>ContactNoteController</code> keeps track of <code>AnnonseNote</code>s.
 *
 * @author Inge Halsaunet
 */
@Controller
@RequestMapping("/note")
public class AnnonseNoteController {

    @Autowired
    private AnnonseNoteService annonseNoteService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SalesService salesService;

    @Autowired
    private ContactPersonService contactPersonService;

    @Autowired
    private AnnonsePersonService annonsePersonService;

    @Autowired
    private AnnonsePersonController annonsePersonController;

    @RequestMapping()
    public String viewHome() {
        return "note/home";
    }

    @RequestMapping("/archive")
    public String viewArchivedNotes(Model model) {
        // TODO make comparator, sort list and remove if there are too many entries.
        AnnonsePerson me = annonsePersonController.getLoggedInUser();
        model.addAttribute("annonseNoteList", annonseNoteService.getUserAndDelegatedNotActiveAnnonseNotes(me));
        model.addAttribute("active", false);
        return "note/list";
    }

    @RequestMapping("/archive/all")
    public String viewAllArchivedNotes(Model model) {
        AnnonsePerson me = annonsePersonController.getLoggedInUser();
        model.addAttribute("annonseNoteList", annonseNoteService.getUserAndDelegatedNotActiveAnnonseNotes(me));
        model.addAttribute("active", false);
        return "note/list";
    }

    @RequestMapping("/doarchive/{annonseNote}")
    public String doArchive(@PathVariable AnnonseNote annonseNote) {
        annonseNote.setActive(Boolean.FALSE);
        annonseNoteService.saveAndFlush(annonseNote);
        return "redirect:/annonseweb/note/" + annonseNote.getId();
    }

    @RequestMapping("/active")
    public String viewActiveNotes(Model model) {
        AnnonsePerson me = annonsePersonController.getLoggedInUser();
        model.addAttribute("annonseNoteList", annonseNoteService.getUserAndDelegatedActiveAnnonseNotes(me));
        model.addAttribute("active", true);
        return "note/list";
    }

    @RequestMapping("/{annonseNote}")
    public String viewAnnonseNote(@PathVariable AnnonseNote annonseNote, Model model) {
        model.addAttribute("annonseNote", annonseNote);
        return "note/note";
    }

    @RequestMapping("/new")
    public String viewNew(Model model) {
        AnnonseNote note = new AnnonseNote();
        return viewEdit(note, model);
    }

    @RequestMapping("/new/sale/{sale}")
    public String viewNewWithSale(@PathVariable Sale sale, Model model) {
        AnnonseNote note = new AnnonseNote();
        note.setSale(sale);
        return viewEdit(note, model);
    }

    @RequestMapping("/new/customer/{customer}")
    public String viewNewWithCustomer(@PathVariable Customer customer, Model model) {
        AnnonseNote note = new AnnonseNote();
        note.setCustomer(customer);
        return viewEdit(note, model);
    }

    @RequestMapping("/new/contactperson/{contactPerson}")
    public String viewNewWithContactPerson(@PathVariable ContactPerson contactPerson, Model model) {
        AnnonseNote note = new AnnonseNote();
        note.setCustomer(contactPerson.getCustomer());
        note.setContactPerson(contactPerson);
        return viewEdit(note, model);
    }

    @RequestMapping("/edit/{annonseNote}")
    public String viewEdit(@PathVariable AnnonseNote annonseNote, Model model) {
        List<AnnonsePerson> uList = annonsePersonService.findAll();
        uList.add(null);
        List<Sale> sList = salesService.getActiveSales();
        sList.add(null);
        List<Customer> cList = customerService.getActiveCustomers();
        cList.add(null);
        List<ContactPerson> pList = contactPersonService.getActiveContactPersonsForCustomer(annonseNote.getCustomer());
        pList.add(null);
        model.addAttribute("contactPersonList", pList);
        model.addAttribute("annonseNote", annonseNote);
        model.addAttribute("userList", uList);
        model.addAttribute("saleList", sList);
        model.addAttribute("customerList", cList);
        return"note/edit";
    }

    @RequestMapping("/save")
    public String saveNew(@Valid @ModelAttribute AnnonseNote annonseNote) {
        annonseNote.setCreatedUser(annonsePersonController.getLoggedInUser());
        annonseNote.setCreatedDate(Calendar.getInstance());
        annonseNoteService.saveAndFlush(annonseNote);
        return "redirect:/annonseweb/note/" + annonseNote.getId();
    }

    @RequestMapping("/save/{pathAnnonseNote}")
    public String saveEdit(@PathVariable AnnonseNote pathAnnonseNote, @Valid @ModelAttribute AnnonseNote annonseNote) {
        pathAnnonseNote.setActive(annonseNote.getActive());
        pathAnnonseNote.setContactPerson(annonseNote.getContactPerson());
        pathAnnonseNote.setCustomer(annonseNote.getCustomer());
        pathAnnonseNote.setDelegatedUser(annonseNote.getDelegatedUser());
        pathAnnonseNote.setDueDate(annonseNote.getDueDate());
        pathAnnonseNote.setSale(annonseNote.getSale());
        pathAnnonseNote.setText(annonseNote.getText());
        annonseNoteService.saveAndFlush(pathAnnonseNote);
        return "redirect:/annonseweb/note/" + pathAnnonseNote.getId();
    }

    @RequestMapping("/blank/sidebar_notes")
    public String viewSidebarNotes(Model model) {
        AnnonsePerson me = annonsePersonController.getLoggedInUser();
        Calendar now = Calendar.getInstance();
        model.addAttribute("myComingNotes", annonseNoteService.getUserActiveAnnonseNotesAfterDate(me, now));
        model.addAttribute("myDelegatedNotes", annonseNoteService.getDelegatedActiveAnnonseNotesAfterDate(me, now));
        model.addAttribute("myExpiredNotes", annonseNoteService.getUserAndDelegatedActiveAnnonseNotesBeforeDate(me,now));
        return "note/sidebar_notes";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(AnnonseNote.class, new BindByIdEditor(annonseNoteService));
        binder.registerCustomEditor(Sale.class, new BindByIdEditor(salesService));
        binder.registerCustomEditor(Customer.class, new BindByIdEditor(customerService));
        binder.registerCustomEditor(ContactPerson.class, new BindByIdEditor(contactPersonService));
        binder.registerCustomEditor(AnnonsePerson.class, new ResolveByUsernameEditor(annonsePersonService));
        binder.registerCustomEditor(Calendar.class, new ResolveCalendarEditor());
    }
}
