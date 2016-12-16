package cw3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {
	
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView index(){
        return new ModelAndView("index");
    }  
	
	@RequestMapping(value = {"/index"}, method = RequestMethod.GET)
	public ModelAndView homePage(){
	    return new ModelAndView("index");
	}  
	  
	@RequestMapping(value = {"/errorPage"}, method = RequestMethod.GET)
	public ModelAndView error(){
	    return new ModelAndView("error");
	}  
}
