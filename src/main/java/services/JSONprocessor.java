package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Department;
import model.Employee;

import java.util.ArrayList;
import java.util.List;

//Класс для удобной обработки объектов из JSON формата и обратно.
public class JSONprocessor implements iJSONprocessor{

    //Метод преобразует из JSON строки в объект Employee.
    @Override
    public Employee JSONtoEmp(String jsonString)    {

        ObjectMapper objectMapper= new ObjectMapper();
        try
        {
            return objectMapper.readValue(jsonString, Employee.class);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //Метод преобразует объект Employee в строку JSON.
    @Override
    public String EmpToJSON(Employee emp)
    {
        ObjectMapper objectMapper= new ObjectMapper();
        try
        {
            return objectMapper.writeValueAsString(emp);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //Метод преобразует строку JSON в список объектов Employee.
    @Override
    public List<Employee> JSONtoListEmp(String jsonString)
    {
        List<Employee> employeeList;
        ObjectMapper objectMapper= new ObjectMapper();
        try
        {
            employeeList=objectMapper.readValue(jsonString, new TypeReference<List<Employee>>() {});

            return employeeList;
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    //Метод преобразует строку JSON в список объектов Department.
    @Override
    public List<Department> JSONtoListDep(String jsonString)
    {

        ObjectMapper objectMapper= new ObjectMapper();
        try
        {
            return objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, Department.class));
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
