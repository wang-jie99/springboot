package com.example.demo.bysj.domain;

import com.example.demo.util.IdService;

import java.io.Serializable;
import java.util.Set;

public final class Teacher implements Comparable<Teacher>,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String no;
	private ProfTitle profTitle;
	private Degree degree;
	private Department department;
	private Set<GraduateProject> projects;
	{
		this.id = IdService.getId();
	}

	public Teacher(Integer id,
				   String name,
				   String no,
				   ProfTitle title,
				   Degree degree,
                   Department department) {
		this(name, no, title, degree, department);
		this.id = id;

	}
	public Teacher(
				   String name,
				   String no,
				   ProfTitle title,
				   Degree degree,
				   Department department) {
		super();
		this.name = name;
		this.no = no;
		this.profTitle = title;
		this.degree = degree;
		this.department = department;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo(){
		return no;
	}

	public void setNo(String no){
		this.no = no;
	}

	public ProfTitle getTitle() {
		return this.profTitle;
	}

	public void setTitle(ProfTitle title) {
		this.profTitle = title;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Set<GraduateProject> getProjects() {
		return projects;
	}

	public void setProjects(Set<GraduateProject> projects) {
		this.projects = projects;
	}

	




	@Override
	public int compareTo(Teacher o) {
		// TODO Auto-generated method stub
		return this.id-o.getId();
	}


	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "Teacher ( "
	        + super.toString() + TAB
	        + "id = " + this.id + TAB
	        + "name = " + this.name + TAB
	        + "title = " + this.profTitle + TAB
	        + "degree = " + this.degree + TAB
	        + "department = " + this.department + TAB
	        + "projects = " + this.projects + TAB
	        + " )";
	
	    return retValue;
	}

}
