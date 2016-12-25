package cw3.controller;

import java.sql.SQLException;
import java.util.Vector;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cw3.dao.ClassDAO;
import cw3.entities.JsonClass;
import cw3.entities.Property;
import cw3.entities.simpleJsonClass;
import cw3.entities.simpleProperty;
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
		simpleJsonClass jclass = new simpleJsonClass("Root",0);
		jclass = buildSimpleTree(jclass);
		try {
			
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView("taxonomy_builder");
		model.addObject("jsonclass",jclass);
		model.addObject("classes",arr);
	    return model;
	}
	
	@RequestMapping(value = {"/objRep"})
	public ModelAndView objectRepository(){
		
		Vector<String> arr = new Vector<String>();
		Vector<Vector<String>> arr2 = new Vector<Vector<String>>();
		try {	
			arr2 = dao.getTreeMap();
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		ModelAndView model = new ModelAndView("object_repository");
		model.addObject("treemap",arr2);
		model.addObject("classes",arr);
	    return model;
	}
	
	  
	@RequestMapping(value = {"/errorPage"})
	public ModelAndView error(){
	    return new ModelAndView("error");
	}
	

	
	public simpleJsonClass buildSimpleTree(simpleJsonClass jclass) {
		
		try {
			Vector<Property> tmpprop = new Vector<Property>();
			Vector<simpleProperty> tmpsimpleprop = new Vector<simpleProperty>();
			
			tmpprop = dao.getPropertiesByClass(jclass.getName());
			for (int i = 0; i < tmpprop.size();i++) {
				tmpsimpleprop.add(new simpleProperty(tmpprop.get(i).getPropertyName(),tmpprop.get(i).getPropertyType().getName()));
			}
			
			jclass.setProperties(tmpsimpleprop);
			jclass.setLevel(dao.getLevel(jclass.getName()));
			if (dao.getSubClasses(jclass.getName()).isEmpty()) {
				return jclass;
			}

			for (int i = 0;i < dao.getSubClasses(jclass.getName()).size();i++) {
			jclass.addSubClass(buildSimpleTree(dao.getSimpleSubClasses(jclass.getName()).elementAt(i)));
			}
			
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		
		return jclass;
	}
}
