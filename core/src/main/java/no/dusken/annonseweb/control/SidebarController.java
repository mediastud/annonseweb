package no.dusken.annonseweb.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: Emilie Brunsgaard Ek
 * Email: embrek@underdusken.no
 * Date: 13.10.11
 * Time: 21:13
 */
@Controller
public class SidebarController {

    @RequestMapping("/sidebar.do")
    public String viewSidebar(){
        return "sidebar";
    }
}
