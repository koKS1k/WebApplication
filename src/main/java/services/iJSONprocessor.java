package services;

import model.Department;
import model.Employee;

import java.util.List;

public interface iJSONprocessor {

    Employee JSONtoEmp (String jsonString);

    String EmpToJSON (Employee emp);

    List<Employee> JSONtoListEmp(String jsonString);

    List<Department> JSONtoListDep(String jsonString);

}
