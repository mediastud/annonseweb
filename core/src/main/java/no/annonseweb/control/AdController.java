package no.annonseweb.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by IntelliJ IDEA.
 * User: Emilie Brunsgaard Ek
 * Email: embrek@underdusken.no
 * Date: 13.10.11
 * Time: 20:08
 */
@Controller
@RequestMapping("/ads")
public class AdController {

    @RequestMapping("/adHome")
    public String viewAdHome(){
        return "ads/adHome";
    }

    @RequestMapping("/newAdGeneral")
    public String viewNewAdGeneral(){
        return "ads/newAdGeneral";
    }

    @RequestMapping("/newAdDuskenPaper")
    public String viewNewAdDuskenPaper(){
        return "ads/newAdDuskenPaper";
    }

    @RequestMapping("/newAdDuskenNett")
    public String viewNewAdDuskenNett(){
        return "ads/newAdDuskenNett";
    }

    @RequestMapping("/newAdRadioRevolt")
    public String viewNewAdRadioRevolt(){
        return "ads/newAdRadioRevolt";
    }

    @RequestMapping("/newAdStudentTV")
    public String viewNewAdStudentTV(){
        return "ads/newAdStudentTV";
    }

    @RequestMapping("/viewActiveAds")
    public String viewActiveAds(){
        return "ads/viewActiveAds";
    }
}

