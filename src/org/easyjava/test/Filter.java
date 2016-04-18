package org.easyjava.test;

import org.easyjava.database.Model;
import org.easyjava.web.EFilter;

public class Filter extends EFilter{
	
	@Override
	public void createModel() {
		
		String[]  comment = new String[3];
		comment[0] = "field=name,string=User,type=Text";
		comment[1] = "field=date,string=Date,type=Date";
		comment[2] = "field=content,string=Comment,type=Text";
		Model.add("comment", comment);
		
		
		
		
		String[] company = new String[3];
		company[0] = "field=name,string=Name,type=Text";
		company[1] = "field=addr,string=Address,type=Text";
		company[2] = "field=type,string=Type,type=Text";
		Model.add("company", company);
		
		String[] fields = new String[4] ;
		fields[0] = "field=name,string=User,type=Text";
		fields[1] = "field=sex,string=Sex,type=Text";
		fields[2] = "field=salary,string=Salary,type=float";
		fields[3] = "field=company_id,string=Company,type=many2one:company,inverse=name";
	    Model.add("hr_employee",fields);
	    
	    String[]  forum = new String[5];
		forum[0] = "field=name,string=Name,type=Text";
		forum[1] = "field=date,string=Date,type=Date";
		forum[2] = "field=content,string=Content,type=Text";
		forum[3] = "field=comment,string=Comment,type=one2many:comment";
		forum[4] = "field=employee_ids,string=Employee,type=many2many:hr_employee,inverse=name";
		Model.add("forum", forum);	
	}

}
