package cw3.controller;

import java.sql.SQLException;
import java.util.Vector;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cw3.dao.ClassDAO;
import javassist.NotFoundException;


@Controller
public class PageController {
	
	ClassDAO dao = new ClassDAO();
	
	@RequestMapping(value = {"/"})
	public ModelAndView main(){
	    return new ModelAndView("index");
	}
	
	@RequestMapping(value = {"/index"})
	public ModelAndView homePage(){
	    return new ModelAndView("index");
	}
	
	@RequestMapping(value = {"/taxBuild"})
	public ModelAndView taxonomyBuilder(){
		
		Vector<String> arr = new Vector<String>();
		try {
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView("taxonomy_builder");
		model.addObject("classes",arr);
	    return model;
	}
	
	@RequestMapping(value = {"/objRep"})
	public ModelAndView objectRepository(){
	    return new ModelAndView("object_repository");
	}
	  
	@RequestMapping(value = {"/errorPage"})
	public ModelAndView error(){
	    return new ModelAndView("error");
	}
	
}
