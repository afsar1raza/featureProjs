package com.nt.model;




public class Employee {

	private Integer empno;
	private String ename;
	private String eadd;
	private Float salary;
	private Float grossSalary;
	private Float netSalary;
	
	//setter & getter
	public Integer getEmpno() {
		return empno;
	}
	public void setEmpno(Integer empno) {
		this.empno = empno;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEadd() {
		return eadd;
	}
	public void setEadd(String eadd) {
		this.eadd = eadd;
	}
	public Float getSalary() {
		return salary;
	}
	public void setSalary(Float salary) {
		this.salary = salary;
	}
	public Float getGrossSalary() {
		return grossSalary;
	}
	public void setGrossSalary(Float grossSalary) {
		this.grossSalary = grossSalary;
	}
	public Float getNetSalary() {
		return netSalary;
	}
	public void setNetSalary(Float netSalary) {
		this.netSalary = netSalary;
	}
	//toString(-)
	@Override
	public String toString() {
		return "Employee [empno=" + empno + ", ename=" + ename + ", eadd=" + eadd + ", salary=" + salary
				+ ", grossSalary=" + grossSalary + ", netSalary=" + netSalary + "]";
	}
	
}
