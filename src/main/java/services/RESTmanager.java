package services;

import model.Department;
import model.Employee;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

//Класс для работы с REST-сервисом.
public class RESTmanager implements iRESTManager
{

    //Метод для получения списка всех сотрудников
    @Override
    public List<Employee> getAllEmployees()
    {
        //Создаем пустой список
        ArrayList<Employee> allEmployees = new ArrayList<>();

        //Создаем объект JSONprocessor со ссылкой на него, для чтения/записи объектов в формате JSON.
        JSONprocessor jsoNprocessor= new JSONprocessor();

        //Получаем объект String, содержащий список сотрудников в формате JSON.
        String json= this.RestGetJSON("http://localhost:8080/api/employees");

        //Используя JSONprocessor преобразуем список сотрудников из формата JSON
        //в список объектов Employee.
        return jsoNprocessor.JSONtoListEmp(json);
    }

    //Метод для получения списка всех отделов
    @Override
    public List<Department> getAllDepartments()
    {

        //Создаем пустой список
        ArrayList<Department> allDepartments = new ArrayList<>();

        //Создаем объект JSONprocessor со ссылкой на него, для чтения/записи объектов в формате JSON.
        JSONprocessor jsoNprocessor= new JSONprocessor();

        //Получаем объект String, содержащий список отделов в формате JSON.
        String json= this.RestGetJSON("http://localhost:8080/api/employees/departments");

        //Используя JSONprocessor преобразуем список отделов из формата JSON
        //в список объектов Department.
        return jsoNprocessor.JSONtoListDep(json);
    }

    //Добавляем сотрудника (объект Employee) в БД, используя REST.
    @Override
    public void addEmployee(Employee employee)
    {
        this.RestPostJSON("http://localhost:8080/api/employees/add",employee);

    }
    //Редактируем данные сотрудника в БД, используя REST.
    @Override
    public void editEmployee(Employee employee)
    {
        this.RestPutJSON("http://localhost:8080/api/employees/update", employee);
    }

    //Удаляем сотрудника, используя REST.
    //Передаем id, как параметр в строке запроса.
    @Override
    public void delete(int id)
    {
        this.RestDelete("http://localhost:8080/api/employees/delete?id="+id);
    }

    //Метод для получения объекта String, содержащеГо объекты POJO в JSON формате, используя REST.
    //Метод запроса-GET. Используется для получения данных.
    private String RestGetJSON(String RestURL)
    {
        //Создаем HttpURLConnection для создания HTTP соединения
        HttpURLConnection connection=null;

        //Вспомогательные объекты для более удобного чтения ответа.
        BufferedReader reader;
        String line;

        //Данные будут считываться в StringBuffer
        StringBuffer responseContent = new StringBuffer();

        try
        {
            //Указываем параметры соединения.
            //Устанавливаем URL по которому будем устанавливать HTTP соединение.
            connection=this.getConnection(RestURL);

            //Указываем HTTP метод "GET".
            connection.setRequestMethod("GET");


            //Указываем формат получаемых данных-JSON.
            connection.setRequestProperty("Accept", "application/json");

            //Время на установление соединения в миллисекундах
            connection.setConnectTimeout(5000);

            //Время на чтение данных
            connection.setReadTimeout(5000);

            //Отправляем запрос и получаем ответ.
            //В переменной status содержится код состояния HTTP.
            int status= connection.getResponseCode();

            //Если код состояния больше 299, то получаем ошибку.
            if (status>299)
            {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine())!=null)
                {
                    responseContent.append(line);
                }
                System.out.println("We're have an error: "+ responseContent.toString());
            }
            //В остальных случаях начинаем чтение данных.
            else
            {
               //Читаем все данные, используя BufferedReader
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine())!=null)
                {
                    responseContent.append(line);
                }
            }
            //Закрываем BufferedReader.
            //Разрываем соединение.
            reader.close();
            connection.disconnect();

            //Возвращаем все что прочитали.
            return responseContent.toString();

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //Метод для преобразования объектов POJO в объект String, в JSON формате, используя REST,
    //для дальнейщей записи в БД. Метод запроса-POST. Используется для записи новых данных.
    private String RestPostJSON(String RestURL, Employee employee)
    {

        //Объект для создания HTTP соединения
        HttpURLConnection connection=null;

        //Если REST отправляем какой- то ответ о состоянии,
        //то будем его считывать в StringBuffer.
        StringBuffer response = new StringBuffer();

        //JSONprocessor для записи/чтения объектов в формате JSON.
        JSONprocessor jsoNprocessor= new JSONprocessor();

        //Преобразуем  POJO объект employee в JSON-формат и записываем в jsonString.
        String jsonString = jsoNprocessor.EmpToJSON(employee);

        try
        {
            //Указываем параметры соединения.
            //Устанавливаем URL по которому будем устанавливать HTTP соединение.
            connection=this.getConnection(RestURL);

            //Указываем HTTP метод "POST".
            connection.setRequestMethod("POST");

            //Указываем формат отправляемых данных-JSON.
            connection.setRequestProperty("Content-Type", "application/json");

            //Указываем формат получаемых данных-JSON.
            connection.setRequestProperty("Accept", "application/json");

            //Указываем, что соединение может использоваться и для ввода и для вывода данных.
            connection.setDoOutput(true);
            connection.setDoInput(true);

            //Записываем jsonString в OutputStream соединения.
            try(OutputStream os = connection.getOutputStream())
            {
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            //Отправляем запрос и получаем ответ.
            //В переменной status содержится код состояния HTTP.
            int status=connection.getResponseCode();

            //Если код состояния больше 299, то получаем ошибку.
            if(status>299)
            {
                System.out.println("Error");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "successful";
    }

    //Метод для преобразования объектов POJO в объект String, в JSON формате, используя REST,
    //для дальнейщей записи в БД. Метод запроса-PUT используется для редактирования данных.
    private String RestPutJSON(String RestURL, Employee employee)
    {

        HttpURLConnection connection=null;
        StringBuffer response = new StringBuffer();
        JSONprocessor jsoNprocessor= new JSONprocessor();
        String jsonString = jsoNprocessor.EmpToJSON(employee);
        System.out.println(jsonString);

        try
        {
            connection=this.getConnection(RestURL);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            System.out.println(connection.getResponseCode());

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        /*try(BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8")))
        {

            String responseLine = null;
            while ((responseLine = br.readLine()) != null)
            {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }*/
        return "successful";
    }

    //Метод для удаления данных из БД, путем использования REST.
    private String RestDelete(String RestURL){

        //Объект для создания HTTP соединения
        HttpURLConnection connection=null;
        try
        {
            //Указываем параметры соединения.
            //Устанавливаем URL по которому будем устанавливать HTTP соединение.
            connection=this.getConnection(RestURL);

            //Указываем HTTP метод "DELETE".
            connection.setRequestMethod("DELETE");

            //Указываем формат отправляемых данных-JSON.
            connection.setRequestProperty("Content-Type", "application/json");

            //Указываем, что соединение может использоваться и для ввода и для вывода данных.
            connection.setDoOutput(true);
            connection.setDoInput(true);

            //Отправляем запрос и получаем ответ.
            //В переменной status содержится код состояния HTTP.
            int status=connection.getResponseCode();

            //Если код состояния больше 299, то получаем ошибку.
            if(status>299)
            {
                System.out.println("Error");
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "successful";
    }
    //Метод возвращает новый объект HttpURLConnection, по заданному пути URL.
    private HttpURLConnection getConnection(String RestURL) {

        URL url= null;
        try {
            url = new URL(RestURL);
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }



    }
}
