package com.example.WebApplication.servlets;

import model.Department;
import model.Employee;
import services.RESTmanager;
import services.DataHandler;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

/*
Принято решение использовать один сервлет, обрабатывающий несколько путей.
Мапинг производится через аннотацию @WebServlet, а не через web.xml.
*/
@WebServlet(name = "EmployeeServlet", urlPatterns ={"/servlet/new","/servlet/update","/servlet/edit","/servlet/delete","/servlet/departmentStaff","/servlet/filter"})
public class EmployeeServlet extends HttpServlet {

    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        //В переменную action записывается текущий путь, по которому вызван сервлет.
        String action = request.getServletPath();

        //Создаем объект RESTmanager() и ссылку на него. Используем один объект на сервелет.
        RESTmanager rest= new RESTmanager();

        // Получаем объект HttpSession из запроса и ссылку на него. Используем один объект на сервелет.
        // Необходим только для записи атрибутов в SessionScope.
        HttpSession session=request.getSession();

        // Создаем объект DataHandler для обработки данных.Например, для расчета средней зарплаты
        // Используем один объект в методе.
        DataHandler dataHandler=new DataHandler();

        // Ссылки, используемые для обработки данных объекта Employee, для всех действий сервлета.
        // Добавление новго сотрудника или изменение существующего.
        String department;
        String name;
        Date date;
        long salary;
        Employee employee;
        int id;

        // SimpleDateFormat используем для форматирования даты полученной из JSON и записи в него.
        // Просто для удобного представления.

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //Проверяем по какому пути происходило обращение у сервлету.
        switch (action)
            {
            //Создаем нового сотрудника
            case "/servlet/new":

                //Получаем данные о новом сотруднике из request.Id не нужен.
                //Он пропишется автоматически в БД, т.к. там он установлен с параметрами primary key, autoincrement.
                department=request.getParameter("department");
                name= request.getParameter("name");
                date = new Date();
                try
                {
                    date = format.parse(request.getParameter("date"));
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                salary= Long.parseLong(request.getParameter("salary"));

                //На основе полученных данных создаем сотрудника.
                employee=new Employee(department, name, date, salary);

                //Используя REST сервис записываем нового сотрудника в БД.
                rest.addEmployee(employee);
                System.out.println("Add successfull!!");

                /*
                  Обновляем список сотрудников и список отделов в ApplicationScope.
                  Список отделов необходимо обновлять,т.к., создавая нового сотрудника
                  в поле "Departments" можно прописать новый отдел, который REST добавит
                  автоматически.
                */
                getServletContext().setAttribute("employeeList", rest.getAllEmployees());
                getServletContext().setAttribute("departmentsList", rest.getAllDepartments());

               try
               {
                   // Пересылаем в "user-list.jsp"
                   getServletContext().getRequestDispatcher("/jsp/user-list.jsp").forward(request,response);
               }
               catch (ServletException e)
               {
                    System.out.println("Update servlet exception");
               }
            break;

            //Изменяем существующего сотрудника
            case "/servlet/edit":
               //Получаем все данные из запроса, включая id. id необходим,чтобы
               //REST смог найти сотрудника, которого необходимо отредактировать
               id=Integer.parseInt(request.getParameter("id"));
               department=request.getParameter("department");
               name= request.getParameter("name");
               date = new Date();
                try
                {
                    date = format.parse ( request.getParameter("date") );
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                salary= Long.parseLong(request.getParameter("salary"));

                //Создаем сотрудника, на основе полученных данных.
                employee=new Employee(id, department, name, date, salary);

                //Используя REST сервис заменяем данные сотрудника в БД.
                rest.editEmployee(employee);

                /*
                  Обновляем список сотрудников и список отделов в ApplicationScope.
                  Список отделов необходимо обновлять,т.к., создавая нового сотрудника
                  в поле "Departments" можно прописать новый отдел, который REST добавит
                  автоматически.
                */
                getServletContext().setAttribute("employeeList", rest.getAllEmployees());
                getServletContext().setAttribute("departmentsList", rest.getAllDepartments());

                /*
                  Если мы перешли в окно редакитрования из отсортированного списка в "user-list.jsp",
                  атрибут "sorted" будет равен 1. Таким образом нам необходимо взять из атрибута "sortedList"
                  в SessionScope отсортированный список, и заменить в нем отредактированного сотрудника, затем
                  положить обратно в "sortedList" список с измененным сотрудником.
                */
                if ((Integer) session.getAttribute("sorted")==1)
                {
                    //Получаем список из атрибута
                    ArrayList<Employee> sortedEmployees=(ArrayList<Employee>) session.getAttribute("sortedList");

                    //Проходим по всем элементам списка изщем сотрудника для замены
                    for(int i=0;i<sortedEmployees.size();i++)
                    {
                    //Заменяем его
                        if(sortedEmployees.get(i).getId()==id)
                            sortedEmployees.set(i,employee);
                    }
                    //Заменяем список на новый
                    session.setAttribute("sortedList",sortedEmployees);
                }

                try {
                /*
                  Переходим на "user-list.jsp" где будет отображатсья список
                  сотрудников (отсортированный или нет) с отредактированным
                  сотрудником.
                */
                    getServletContext().getRequestDispatcher("/jsp/user-list.jsp").forward(request, response);
                } catch (ServletException e) {
                    System.out.println("Update servlet exception");
                }

                break;

           //Фильтруем список по дате или по диапазону дат.
            case "/servlet/filter":

                //Проверяем какие есть в request параметры "exactDate" или пара "dateTo", "dateFrom".
                //Всех трех одновременно не может быть, поэтому реализуется один из двух путей обработки списка.

                //Если пришел параметр "exactDate", то ищем сотрудников с конкретной датой рождения.
                if(request.getParameter("exactDate")!=null)
                {
                    //Создаем новый объект Date и ссылку на него
                    Date exactDate = new Date();
                    try
                    {
                        // "exactDate" записан в формате "yyyy-MM-dd". Используя SimpleDataFormat
                        // преобразуем его в Date.
                        exactDate = format.parse (request.getParameter("exactDate"));
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    /*
                       Используя объект DataHandler филтруем список и сразу складываем его в атрибут "sortedList".
                       в SessionScope. В атрибут нового request перекладываем параметр "exactDate". чтобы на странице с
                       отфильтрованным списком в input'e было видно по какой дате фильтровался список.
                       Т.к. список теперь отфильтрован, то устанавливаем в SessionScope атрибут "sorted" равным 1.
                    */
                    session.setAttribute("sortedList", dataHandler.filterDate(exactDate));
                    request.setAttribute("exactDate", request.getParameter("exactDate"));
                    session.setAttribute("sorted", 1);
                }

                //Если пришли параметры "dateTo" и "dateFrom", то ищем сотрудников, родившихся в указанном диапазоне дат.
                if (request.getParameter("dateFrom")!=null && request.getParameter("dateTo")!=null)
                {
                    //Создаем объекты Date и ссылки на них, для полученных дат.
                    Date from = new Date();
                    Date to = new Date();

                    try
                    {
                        // "dateTo" и "dateFrom" записаны в формате "yyyy-MM-dd". Используя SimpleDataFormat
                        // преобразуем их в Date.
                        from = format.parse ( request.getParameter("dateFrom"));
                        to = format.parse ( request.getParameter("dateTo"));
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    /*
                       Используя объект DataHandler филтруем список и сразу складываем его в атрибут "sortedList".
                       в SessionScope. В атрибут нового request перекладываем параметры "dateTo" и "dateFrom". чтобы
                       на странице с отфильтрованным списком в input'e было видно по каким датам фильтровался список.
                       Т.к. список теперь отфильтрован, то устанавливаем в SessionScope атрибут "sorted" равным 1.
                    */
                    session.setAttribute("sortedList", dataHandler.filterDate(from,to));
                    request.setAttribute("dateFrom", request.getParameter("dateFrom"));
                    request.setAttribute("dateTo", request.getParameter("dateTo"));
                    getServletContext().setAttribute("sorted", 1);
                }

                try
                {
                    //Переходим на "user-list.jsp", где будет отображатсья отфильтрованный список.
                    getServletContext().getRequestDispatcher("/jsp/user-list.jsp").forward(request,response);
                }
                catch (ServletException e)
                {
                    System.out.println("Filter servlet exception");
                }
                break;

        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //В переменную action записывается текущий путь, по которому вызван сервлет.
        String action = request.getServletPath();

        //Создаем объект RESTmanager() и ссылку на него. Используем один объект на сервелет.
        RESTmanager rest= new RESTmanager();

        // Получаем объект HttpSession из запроса и ссылку на него. Используем один объект на сервелет.
        // Необходим только для записи атрибутов в SessionScope.
        HttpSession session=request.getSession();

        // Создаем объект DataHandler для обработки данных.Например, для расчета средней зарплаты
        // Используем один объект в методе.
        DataHandler dataHandler =new DataHandler();

        //Проверяем по какому пути происходило обращение у сервлету.
        switch (action) {
            //
            case "/servlet/update":
                /*
                  Обновляем список сотрудников и список отделов. Результаты
                  записываются в ApplicationScope в атрибуты  "employeeList"
                  и "departmentsList" соответственно.
                  Атрибуту "sorted" присваивается значение 0. Списки не отсортированы.
                */
                getServletContext().setAttribute("employeeList", rest.getAllEmployees());
                getServletContext().setAttribute("departmentsList", rest.getAllDepartments());
                session.setAttribute("sorted", 0);

                try
                {
                    //Если параметр запроса "purpose" равен "employees",
                    //то перенаправляем запрос на "user-list.jsp"
                    if(request.getParameter("purpose").equals("employees"))
                       getServletContext().getRequestDispatcher("/jsp/user-list.jsp").forward(request,response);

                  /*
                    Если параметр запроса "purpose" равен "departments",
                    то формируем список отделов со средней зарплатой и
                    перенаправляем запрос на "user-list.jsp".
                  */
                    else if(request.getParameter("purpose").equals("departments"))
                    {
                        //Перебираем весь список отделов, который получаем через REST.
                        for (Department department : rest.getAllDepartments())
                        {
                         /*
                            В атрибуты request записываем пары: имя атрибута-название отдела,
                            значение атрибута-средняя зарплата отдела. Среднюю зарплату отдела
                            получаем, используя метод AverageSalaryByDepartment объекта dataHandler.
                         */
                            request.setAttribute(department.getName(), dataHandler.AverageSalaryByDepartment(department.getName()));
                        }
                         //Перенаправляем запрос на "department-list.jsp"
                    getServletContext().getRequestDispatcher("/jsp/department-list.jsp").forward(request,response);

                    }
                }
                catch (ServletException e)
                {
                    System.out.println("Update servlet exception");
                }
            break;

            //Удаляем пользователя
            case "/servlet/delete":
                //Получаем id сотрудника, которого надо удалить.
                //И удаляем его через REST.
                rest.delete(Integer.parseInt(request.getParameter("id")));

                //Обновляем список сотрудников в ApplicationScope в атрибуте "employeeList",
                //используя REST.
                getServletContext().setAttribute("employeeList", rest.getAllEmployees());

                try
                {
                    //Перенаправляем запрос на "user-list.jsp"
                    getServletContext().getRequestDispatcher("/jsp/user-list.jsp").forward(request,response);
                }
                catch (ServletException e)
                {
                    System.out.println("Delete servlet exception");
                }
            break;

            //Получаем список сотрудников, работающих в отделе.
            case "/servlet/departmentStaff":

                //Создаем новый лист и, используя StreamAPI, фильтруем список всех сотрудников,
                //оставляя только тех, которые работают в данном отделе
                List<Employee> sortedList=rest.getAllEmployees().stream()
                                                                .filter(e->e.getDepartment().equals(request.getParameter("department")))
                                                                .collect(Collectors.toList());

                //Полученный список перкладываем в SessionScope в атрибут "sortedList"
                //Параметр "sorted" устанавливаем равным 1.
                session.setAttribute("sortedList", sortedList);
                session.setAttribute("sorted", 1);
                try
                {
                    //Перенаправляем запрос на "user-list.jsp"
                    getServletContext().getRequestDispatcher("/jsp/user-list.jsp").forward(request,response);
                }
                catch (ServletException e)
                {
                    System.out.println("servlet exception");
                }
            break;


        }
    }

    public void destroy() {
    }
}