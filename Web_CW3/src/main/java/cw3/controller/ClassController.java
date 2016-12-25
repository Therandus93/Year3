package cw3.controller;

import java.sql.SQLException;
import java.util.Vector;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import cw3.dao.ClassDAO;
import cw3.entities.Instance;
import cw3.entities.JsonClass;
import cw3.entities.PRIMITIVE_TYPE;
import cw3.entities.Property;
import cw3.entities.UMTClass;
import cw3.entities.simpleJsonClass;
import cw3.entities.simpleProperty;
import javassist.NotFoundException;

@Controller
public class ClassController {

	private static Vector<String> result = new Vector<String>();
	private ClassDAO dao = new ClassDAO();
	private Instance instance = new Instance();
	
	@RequestMapping(value = {"/rest/class/create"}, method = RequestMethod.POST)
	public ModelAndView createClass(@RequestParam("name") String name, @RequestParam("superclass") String superclass) {
		
		Boolean added = false;
		UMTClass tmp = new UMTClass();
		Vector<UMTClass> newarr = new Vector<UMTClass>();
		simpleJsonClass jclass = new simpleJsonClass("Root",0);
		jclass = buildSimpleTree(jclass);
		int level = 0;
		if (superclass != "") {
			newarr.add(new UMTClass(superclass));
			try {
				UMTClass supertmp = new UMTClass(superclass);
				supertmp.addSuperclass(new UMTClass("Root"));
				level = 2;
				dao.addClass(supertmp,level);
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
			level = 1;
			added = dao.addClass(tmp,level);
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView("taxonomy_builder");
		model.addObject("jsonclass",jclass);
		model.addObject("added",added);
		model.addObject("classes",arr);
		return model;
	}
	
	public int findLevel(String classname) {
		
		
		
		return 0;
	}
	
	@RequestMapping(value = {"/rest/class/delete"})
	public ModelAndView deleteClass(@RequestParam("name") String name) {
		
		Boolean removed = false;
		Vector<String> arr = new Vector<String>();
		simpleJsonClass jclass = new simpleJsonClass("Root",0);
		jclass = buildSimpleTree(jclass);
		try {
			removed = dao.deleteClass(name);
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		ModelAndView model = new ModelAndView("taxonomy_builder");
		model.addObject("jsonclass",jclass);
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
	
	@RequestMapping(value = {"/rest/class/tree"},method =RequestMethod.GET, produces={"application/json"})
	public @ResponseBody String getTree() {
		
		simpleJsonClass jclass = new simpleJsonClass("Root",0);
		
		jclass = buildSimpleTree(jclass);

		Gson gson = new Gson();
		String json = gson.toJson(jclass);
		
	return json;
	}
	
	
	@RequestMapping(value = {"/rest/class/hierarchy/root"},method =RequestMethod.GET, produces={"application/json"})
	public @ResponseBody String hierarchy() {


		simpleJsonClass jclass = new simpleJsonClass("Root",0);
		jclass = buildSimpleTree(jclass);


		Gson gson = new Gson();
		String json = gson.toJson(jclass);
		
		return json;
	}
	
	public JsonClass buildTree(JsonClass jclass) {
		
		try {
			jclass.setProperties(dao.getPropertiesByClass(jclass.getName()));
			jclass.setLevel(dao.getLevel(jclass.getName()));
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
			result = tmp;
		} else {
			getSubClassListTree(name);
			result.removeElementAt(0);
		}
		
		try {
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("taxonomy_builder");
		mav.addObject("direct",direct);
		mav.addObject("selectedclass",name);
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
	
	@RequestMapping(value = {"/rest/class/createHierarchy"})
	public ModelAndView createHierarchy(@RequestParam("construct") String construct,@RequestParam("root") String name) {
		
		int bl = 0;
		int br = 0;
		Vector<UMTClass> tmpclasses = new Vector<UMTClass>();
		Vector<UMTClass> parentclasses = new Vector<UMTClass>();
		parentclasses.add(new UMTClass(name));
		UMTClass tmp = new UMTClass();
		String classname = "";
		String error = "";
		Boolean created = false;
		
		//Construct Check
		
		if (construct.charAt(0) == '{') {
			for (int i = 1; i < construct.length();i++) {
				System.out.println(construct.charAt(i));
				if (construct.charAt(i) == '{') {
					tmp.setName(classname);
					tmp.addSuperclass(parentclasses.elementAt(bl));
					tmp.setLevel(bl+1);
					parentclasses.add(tmp);
					tmpclasses.add(tmp);
					tmp = new UMTClass();
					classname = "";
					System.out.println("###  {  ###  bl = " + bl);
					bl++;
					
				} else if (construct.charAt(i) == '}' && i != construct.length()-1) {
					if (construct.charAt(i-1) != '}') {
					tmp.setName(classname);
					tmp.addSuperclass(parentclasses.elementAt(bl));
					tmp.setLevel(bl+1);
					tmpclasses.add(tmp);
					tmp = new UMTClass();
					classname = "";
					System.out.println(parentclasses.elementAt(bl).getName());
					System.out.println("###  }  ###  bl = " + (bl));
					}
					bl--;

					
					
				} else if (construct.charAt(i) == ','){
					if (construct.charAt(i-1) != '}') {
					tmp.setName(classname);
					tmp.addSuperclass(parentclasses.elementAt(bl));
					tmp.setLevel(bl+1);
					tmpclasses.add(tmp);
					tmp = new UMTClass();
					classname = "";
					System.out.println(parentclasses.elementAt(bl).getName());
					System.out.println("###  ,  ###  bl = " + (bl));
					}
				} else {
					classname += construct.charAt(i);
				}
			}
			if (bl == br) {
				for (int i = 0;i < tmpclasses.size();i++) {
					try {
						if (tmpclasses.get(i).getSuperClasses().isEmpty()) {
							UMTClass root = new UMTClass("Root");
							Vector<UMTClass> tmpsuperclass = new Vector<UMTClass>();
							tmpsuperclass.add(root);
							tmpclasses.get(i).setSuperClasses(tmpsuperclass);
						}
						System.out.println("####Name#####");
						System.out.println(tmpclasses.get(i).getName());
						System.out.println("####SC#####");
						System.out.println(tmpclasses.get(i).getSuperClasses());
						dao.addClass(tmpclasses.get(i),tmpclasses.get(i).getLevel());
						created = true;
					} catch (SQLException | NotFoundException e) {
						//e.printStackTrace();
						System.out.println("Failed");
					}
				}
			}
		}
		
		//Standard return data
		Vector<String> arr = new Vector<String>();
				
		try {
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("taxonomy_builder");
		mav.addObject("error",error);
		mav.addObject("created",created);
		mav.addObject("classes",arr);
		return mav;
		
	}
	
	@RequestMapping(value = {"/rest/class/addprop"})
	public ModelAndView addProperty(@RequestParam("pname") String pname,@RequestParam("type") String ptype,@RequestParam("classname") String classname) {
		
		//Standard return data
		Property p = null;
		Vector<String> arr = new Vector<String>();
				
		try {
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
				
		//Add Property
		
    	switch (ptype) {
    	
    	case "String":
    		p = new Property(pname,PRIMITIVE_TYPE.String);
    		break;
    	case "int":
    		p = new Property(pname,PRIMITIVE_TYPE.Int);
    		break;
    	case "double":
    		p = new Property(pname,PRIMITIVE_TYPE.Double);
    		break;
    	case "boolean":
    		p = new Property(pname,PRIMITIVE_TYPE.Boolean);
    		break;
    	default: 
    		p = new Property(pname.toLowerCase(),new UMTClass(ptype));
    	break;

    	}
		
		try {
			dao.addPropertyByClass(p, classname);
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("taxonomy_builder");
		mav.addObject("classes",arr);
		return mav;
		
	}
	
	@RequestMapping(value = {"/rest/class/delprop"})
	public ModelAndView deleteProperty(@RequestParam(value = "cname")String classname,@RequestParam(value = "pname")String pname) {
		
		//Standard return data
		Vector<String> arr = new Vector<String>();
		Boolean complete = false;
				
		try {
			arr = dao.getClasses();
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
				
		//Delete Property
				
		try {
			complete = dao.deletePropertyByClass(pname, classname);
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView("taxonomy_builder");
		mav.addObject("deleted",complete);
		mav.addObject("classes",arr);
		return mav;
		
	}
	
	@RequestMapping(value = {"/rest/class/grabProp"},method = RequestMethod.GET)
	public @ResponseBody Vector<String> grabProperties(@RequestParam(value = "name")String classname) {
		
		
		//Standard return data
		Vector<Property> parr = new Vector<Property>();
		Vector<String> arr = new Vector<String>();
				
		try {
			parr = dao.getPropertiesByClass(classname);
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
				
		for (int i =0;i < parr.size(); i++) {
			arr.add(parr.get(i).getPropertyName());
		}
		
		return arr;
		
	}
	
}

