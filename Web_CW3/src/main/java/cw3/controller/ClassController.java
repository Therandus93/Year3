package cw3.controller;

import java.sql.SQLException;
import java.util.Vector;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import cw3.dao.ClassDAO;
import cw3.entities.Instance;
import cw3.entities.JsonClass;
import cw3.entities.UMTClass;
import javassist.NotFoundException;

@Controller
public class ClassController {

	private static Vector<String> result = new Vector<String>();
	private ClassDAO dao = new ClassDAO();
	
	@RequestMapping(value = {"/rest/class/create"}, method = RequestMethod.POST)
	public ModelAndView createClass(@RequestParam("name") String name, @RequestParam("superclass") String superclass) {
		
		Boolean added = false;
		UMTClass tmp = new UMTClass();
		Vector<UMTClass> newarr = new Vector<UMTClass>();
		if (superclass != "") {
			newarr.add(new UMTClass(superclass));
			try {
				UMTClass supertmp = new UMTClass(superclass);
				supertmp.addSuperclass(new UMTClass("Root"));
				
				dao.addClass(supertmp);
			} catch (SQLException | NotFoundException e) {
				e.printStackTrace();
			}
		} else {
			newarr.addElement(new UMTClass("Root"));
		}
		
		tmp.setName(name);
		tmp.setSuperClasses(newarr);
		
		Vector<String> arr = new Vector<String>();
		
		try {
			added = dao.addClass(tmp);
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView("taxonomy_builder");
		model.addObject("added",added);
		model.addObject("classes",arr);
		return model;
	}
	
	@RequestMapping(value = {"/rest/class/delete"})
	public ModelAndView deleteClass(@RequestParam("name") String name) {
		
		Boolean removed = false;
		Vector<String> arr = new Vector<String>();
		try {
			removed = dao.deleteClass(name);
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView("taxonomy_builder");
		model.addObject("removed",removed);
		model.addObject("classes",arr);
		return model;
	}
	
	
	@RequestMapping(value = {"/rest/class/hierarchy"},method =RequestMethod.GET, produces={"application/json"})
	public @ResponseBody String hierarchy(@RequestParam("name") String name,@RequestParam("direct") Boolean direct) {

		if (name == null) {
			name = "Root";
		}
		
		JsonClass jclass = new JsonClass(name);
		
		if (direct == false) {
			jclass = buildTree(jclass);
		} else {
			try {
				jclass.setProperties(dao.getPropertiesByClass(name));
				if (!dao.getSubClasses(name).isEmpty()) {
					Vector<JsonClass> tmp = new Vector<JsonClass>();
					tmp = dao.getSubClasses(name);
					for(int i = 0; i < tmp.size();i++) {
						jclass.addSubClass(tmp.get(i));
					}
				}
			} catch (SQLException | NotFoundException e) {
				e.printStackTrace();
			}
		}

		Gson gson = new Gson();
		String json = gson.toJson(jclass);
		
		return json;
	}
	
	
	@RequestMapping(value = {"/rest/class/hierarchy/root"},method =RequestMethod.GET, produces={"application/json"})
	public @ResponseBody String hierarchy() {


		JsonClass jclass = new JsonClass("Root");
		
		jclass = buildTree(jclass);


		Gson gson = new Gson();
		String json = gson.toJson(jclass);
		
		return json;
	}
	
	public JsonClass buildTree(JsonClass jclass) {
		
		try {
			jclass.setProperties(dao.getPropertiesByClass(jclass.getName()));
			if (dao.getSubClasses(jclass.getName()).isEmpty()) {
				return jclass;
			}
			
			for (int i = 0;i < dao.getSubClasses(jclass.getName()).size();i++) {
				jclass.addSubClass(buildTree(dao.getSubClasses(jclass.getName()).elementAt(i)));
			}
			
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		
		return jclass;
	}
	
	@RequestMapping(value = {"/rest/class/getSubclassList"})
	public ModelAndView getSubclassList(@RequestParam("name") String name,@RequestParam("direct") Boolean direct) {
		
		Vector<String> tmp = new Vector<String>();
		Vector<JsonClass> tmpjclass = new Vector<JsonClass>();
		Vector<String> arr = new Vector<String>();
		result.clear();
		
		if (direct) {
			try {
				tmpjclass = dao.getSubClasses(name);
			} catch (SQLException | NotFoundException e) {
				e.printStackTrace();
			}
			for (int i = 0;i < tmpjclass.size();i++) {
			tmp.add(tmpjclass.get(i).getName());
			}
		} else {
			getSubClassListTree(name);
		}
		
		try {
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("taxonomy_builder");
		mav.addObject("subtree",result);
		mav.addObject("classes",arr);
		return mav;
	}
	
	public void getSubClassListTree(String name) {
		
		try {
			result.add(name);
			Vector<JsonClass> tmp = new Vector<JsonClass>();
			tmp = dao.getSubClasses(name);
			
			for (int i = 0;i < tmp.size();i++) {
				getSubClassListTree(tmp.elementAt(i).getName());
			}
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = {"/getClasses"})
	public ModelAndView getClasses() {
		
		Vector<String> arr = new Vector<String>();
		try {
			arr = dao.getClasses();
		/*
		 * ##This code is needed to get full class objects (with data)##
		Vector<UMTClass> classarr = new Vector<UMTClass>();
		int count = arr.size();
		
    	while(count > 0) {
    		UMTClass tmp = new UMTClass();
    		tmp.setName(arr.get(count));
    		tmp.setSuperClasses(dao.getSuperClasses(tmp.getName()));
    		
    		for (int iterator = dao.countProperties(tmp.getName());iterator > 0;iterator--) {
    			tmp.setProperties(dao.getPropertiesByClass(tmp.getName()));
    		}
    		
    		classarr.addElement(tmp);
    		--count;
    		
    	}
    	*/
		} catch (SQLException | NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return new ModelAndView("edit","mainclasses",arr);
	}  
}
