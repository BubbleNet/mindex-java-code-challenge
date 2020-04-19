package com.mindex.challenge.model;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import java.util.List;

public class ReportingStructure {
    private final Employee employee;
    private int numberOfReports;

    private final EmployeeService employeeService;

    public ReportingStructure(EmployeeService employeeService, Employee employee){
        this.employeeService = employeeService;
        this.employee = employee;
    }

    public Employee getEmployee() {
        fill(this.employee);
        return employee;
    }

    public int getNumberOfReports(){ return reports(this.employee); }

    // Counts the number of direct reports.
    private int reports(Employee employee){
        int totalReports = 0;
        for (Employee directReport : employee.getDirectReports()){
            // If the directReport has children, count them
            // employeeService.read only gets the ID of children, so get the child based on its Id
            Employee directReportFull = employeeService.read(directReport.getEmployeeId());
            if (directReportFull.getDirectReports() != null){
                if (directReportFull.getDirectReports().size() != 0){
                    totalReports += reports(directReportFull);
                }
            }
            // Count the directReport
            totalReports++;
        }
        return totalReports;
    }

    // Fills out the passed employee object with the fully filled out ReportingStructure.
    private void fill(Employee employee){
        List<Employee> directReportList = employee.getDirectReports();
        // Iterate by index so we're not working with a copy
        for (int i = 0; i < directReportList.size(); i++){
            directReportList.set(i, employeeService.read(directReportList.get(i).getEmployeeId()));
            if (directReportList.get(i).getDirectReports() != null){
                if (directReportList.get(i).getDirectReports().size() != 0){
                    fill(directReportList.get(i));
                }
            }
        }
    }

}
