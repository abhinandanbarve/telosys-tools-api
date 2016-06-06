package org.telosys.tools.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.Link;
import org.telosys.tools.generic.model.Model;

public class ModelContentFromDSLModelTest {
	
	@Before
	public void setUp() throws Exception {
		System.out.println("Before test");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("After test");
	}

	private TelosysProject initProject() throws Exception {

		String projectFolderFullPath = TestsEnv.createProjectFolder("myproject");
		TelosysProject telosysProject = new TelosysProject(projectFolderFullPath);
		
		System.out.println("Init project...");
		String s = telosysProject.initProject();
		System.out.println(s);
		
//		TestsEnv.copyDbModelFile(projectFolderFullPath, "bookstore.dbrep");
		TestsEnv.copyDslModelFiles(projectFolderFullPath, "employees");
		TestsEnv.copyDslModelFiles(projectFolderFullPath, "cars-and-drivers");
//		TestsEnv.copyDslModelFiles(projectFolderFullPath, "employees_invalid");
//		TestsEnv.copyTemplatesFiles(projectFolderFullPath, "basic-templates-TT210");
		
		return telosysProject ;
	}
	
	@Test
	public void testModelDSL_Employees() throws Exception {
		System.out.println("========== Loading .model ");
		TelosysProject telosysProject = initProject() ;
		System.out.println("getTelosysToolsCfg...");
		TelosysToolsCfg telosysToolsCfg = telosysProject.getTelosysToolsCfg();
		
		GenericModelLoader genericModelLoader =  new GenericModelLoader(telosysToolsCfg);
		Model model = genericModelLoader.loadModel("employees.model");
		assertNotNull(model);
		assertNull(genericModelLoader.getErrorMessage());
		assertNull(genericModelLoader.getParsingErrors());
		System.out.println("Model loaded : " + model.getEntities().size() + " entities");
		
		assertEquals(2, model.getEntities().size() );
		
		Entity e = model.getEntityByClassName("Employee");
		assertNotNull(e);
		assertEquals("Employee", e.getClassName());
		assertEquals("Employee", e.getDatabaseTable());

		List<Link> links = e.getLinks() ;
		assertNotNull(links);
		assertEquals(1, links.size() );
		for ( Link link : links ) {
			assertNotNull(link.getId());
			assertNotNull(link.getFieldName());
			assertNotNull(link.getFieldType());
			assertEquals(Cardinality.MANY_TO_ONE, link.getCardinality() );
		}
	}

	@Test
	public void testModelDSL_CarsAndDrivers() throws Exception {
		System.out.println("========== Loading .model ");
		TelosysProject telosysProject = initProject() ;
		System.out.println("getTelosysToolsCfg...");
		TelosysToolsCfg telosysToolsCfg = telosysProject.getTelosysToolsCfg();
		
		GenericModelLoader genericModelLoader =  new GenericModelLoader(telosysToolsCfg);
		Model model = genericModelLoader.loadModel("cars-and-drivers.model");
		System.out.println("Loading result :");
		System.out.println(" - Error message = " + genericModelLoader.getErrorMessage() );
		assertNotNull(model);
		assertNull(genericModelLoader.getErrorMessage());
		assertNull(genericModelLoader.getParsingErrors());
		System.out.println("Model loaded : " + model.getEntities().size() + " entities");
		
		assertEquals(3, model.getEntities().size() );
		
		Entity e = model.getEntityByClassName("Car");
		assertNotNull(e);
		assertEquals("Car", e.getClassName());
		assertEquals("Car", e.getDatabaseTable());

		List<Link> links = e.getLinks() ;
		assertNotNull(links);
		assertEquals(2, links.size() );
		for ( Link link : links ) {
			System.out.println("Link Id : " + link.getId() );
			assertNotNull(link.getId());
			System.out.println("getFieldName() : " + link.getFieldName() );
			assertNotNull(link.getFieldName());
			System.out.println("getFieldType() : " + link.getFieldType() );
			assertNotNull(link.getFieldType());
			System.out.println("getCardinality() : " + link.getCardinality() );
			assertEquals(Cardinality.MANY_TO_ONE, link.getCardinality() );
		}
	}

}
