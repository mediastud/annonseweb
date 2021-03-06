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
import no.dusken.annonseweb.models.AnnonsePerson;
import no.dusken.annonseweb.service.AnnonsePersonService;
import no.dusken.common.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The <code>AnnonsePersonController</code> is a class created for management of <code>AnnonsePerson</code>s.
 * It is thought only to be temporary, until a common db for MediaStud is created.
 *
 * @author Sitron Te
 */
@Controller
@RequestMapping("/user")
public class AnnonsePersonController {
    @Autowired
    private AnnonsePersonService annonsePersonService;

    public AnnonsePerson getLoggedInUser() {
        String username = AuthenticationUtil.getLoggedinUsername();
        AnnonsePerson person = annonsePersonService.getByUsername(username);
        if (person == null) {
            // Inserts a new AnnonsePerson, and makes sure it is read from database
            person = new AnnonsePerson(username);
            annonsePersonService.saveAndFlush(person);
            person = annonsePersonService.getByUsername(username);
        }
        return person;
    }

    @RequestMapping("/doArchive/{user}")
    public String archiveUser(@PathVariable AnnonsePerson user) {
        user.setActive(Boolean.FALSE);
        annonsePersonService.saveAndFlush(user);
        return "redirect:/annonseweb/user/all";
    }

    @RequestMapping("/me")
    public String viewMe(Model model) {
        model.addAttribute("loggedIn", getLoggedInUser());
        return "/user/me";
    }

    @RequestMapping("/all")
    public String viewAll(Model model) {
        model.addAttribute("activeUserList", annonsePersonService.getActiveAnnonsePersons());
        model.addAttribute("nonActiveUserList", annonsePersonService.getNotActiveAnnonsePersons());
        return "/user/all";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveNew(@Valid @ModelAttribute AnnonsePerson user) {
        AnnonsePerson self = getLoggedInUser();
        self.setActive(user.getActive());
        self.setFirstname(user.getFirstname());
        self.setSurname(user.getSurname());
        self.setEmailAddress(user.getEmailAddress());
        self.setPhoneNumber(user.getPhoneNumber());
        annonsePersonService.saveAndFlush(self);
        return "redirect:/annonseweb/user/me";
    }

    @RequestMapping("/{user}")
    public String viewUser(Model model, @PathVariable AnnonsePerson user) {
        model.addAttribute("user", user);
        return "/user/viewUser";
    }

    @RequestMapping("/game/breakout")
    public String viewAnnonseWebGame() {
        return "/user/game";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(AnnonsePerson.class, new ResolveByUsernameEditor(annonsePersonService));
    }
}
