<%@ page import="services.RESTmanager" %>
<!DOCTYPE html>
<html>

<head>
    <title>Employee	Management App</title>
    <!-- Импорт CSS -->
    <link href="../css/main-page-style.css" rel="stylesheet" type="text/css">
    <!-- Подключаем JSTL-core -->
    <%@ taglib prefix = "c"  uri ="http://java.sun.com/jsp/jstl/core" %>
</head>
<body class="body">

<!--Заголовок-->
<header>
    <div class="header">
        <p class="header_text" style="margin-left:20px"> Employee	Management App </p>
    </div>
</header>

<!-- Блок с двумя ссылками, оформленными как кнопки-->
<div>
    <!--Ссылка на получение списка всех сотрудников-->
    <a  href="<c:url value="/servlet/update">
              <c:param name="purpose" value="employees"/>
              </c:url>"
        class="main_button size_1"> Employees
    </a>
    <!--Ссылка на получение списка всех отделов-->
    <a  href="<c:url value="/servlet/update">
              <c:param name="purpose" value="departments"/>
              </c:url>"
        class="main_button size_2"> Departments
    </a>

    <!--Ссылка на добавление нового сотрудника-->
    <a  href="${pageContext.request.contextPath}/jsp/new-user-form.jsp"
        class="main_button size_3"> Add Employee
    </a>
</div>

<div class="text1"> date range</div>
<div class="input_position1">
    <!--Форма для отправки диапазона дат, для фильтрации сотрудников по дате рождения-->
    <form class="inputs form_position_1"
          action="${pageContext.request.contextPath}/servlet/filter"
          method="post">

        <!--Поле для первой даты в диапазоне (Дата "От").Если список уже
         был отфильтрован, то поле заполнено предыдущим значением-->
        <fieldset class="form-group">
            <label>from</label>
            <input type="date"
                   value="${dateFrom}"
                   class="form-control"
                   name="dateFrom"
                   required="required">
        </fieldset>

        <!--Поле для второй даты в диапазоне (Дата "До").Если список уже
         был отфильтрован, то поле заполнено предыдущим значением-->
        <fieldset class="form-group">
            <label>to</label>
            <input type="date"
                   value="${dateTo}"
                   class="form-control"
                   name="dateTo"
                   required="required">
        </fieldset>

        <!-- Кнопка для отправки формы -->
        <div class="main_button2_position1">
            <button type="submit" class="main_button_2">Filter</button>
        </div>
    </form>
</div>

<div class="text2"> date</div>
<div class="input_position2">
    <!--Форма для отправки конкретной даты, для фильтрации сотрудников по дате рождения-->
    <form class="inputs form_position_1"
          action="${pageContext.request.contextPath}/servlet/filter"
          method="post">

        <!--Поле для даты по которой будет фильроваться список.Если список уже
         был отфильтрован, то поле заполнено предыдущим значением-->
        <fieldset class="form-group">
            <label>date</label>
            <input type="date"
                   value="${exactDate}"
                   class="form-control"
                   name="exactDate"
                   required="required">
        </fieldset>

        <!-- Кнопка для отправки формы -->
        <div class="main_button2_position2">
            <button type="submit" class="main_button_2">Filter</button>
        </div>
    </form>
</div>

<!-- Когда новый пользователь запускает приложение атрибут "sorted"
устанавливается в 0.-->
<%
    if (request.getSession().isNew())
        session.setAttribute("sorted", 0);
%>

<!-- Если в applicationScope есть заполненный список 'employeeList',
 то выводим его содержимое в таблице, но только если в sessionScope
 атрибут "sorted" установлен в 0.-->
<c:if test="${not empty applicationScope['employeeList']}">

    <!-- Проверяем атрибут "sorted", если 0, то в переменную "list"  устанавливаем список всех сотрудников-
     атрибут 'employeeList' из applicationScope. Если 1, то устанавливаем отсортированный список 'sortedList'
     из sessionScope. -->
    <c:choose>
        <c:when test="${sessionScope['sorted'] == 0}">
            <c:set var="list" value="${applicationScope['employeeList']}"/>
        </c:when>

        <c:when test="${sessionScope['sorted'] == 1}">
        <c:set var="list" value="${sessionScope['sortedList']}"/>
        </c:when>
    </c:choose>

<div class="table_div">
    <table class="table">

        <!-- Названия столбцов таблицы -->
        <thead>
        <tr>
            <th style="width: 30px">ID</th>
            <th style="width: 270px">Department</th>
            <th style="width: 320px">Name</th>
            <th style="width: 170px">Date of birth</th>
            <th style="width: 80px">Salary</th>
            <th style="width: 60px"></th>
            <th style="width: 80px"></th>
        </tr>
        </thead>
        <tbody>

        <!-- Перебираем все значения в списке, который в переменной "list" -->
        <c:forEach var="employee" items="${list}">
        <tr>
            <!-- id сотрудника -->
            <td> <c:out value="${employee.id}"/> </td>

            <!-- Отдел, в котором работает сотрудник -->
            <td> <c:out value="${employee.department}"/> </td>

            <!-- Имя сотрудника -->
            <td> <c:out value="${employee.name}"/> </td>

            <!-- Дата рождения сотрудника. Используем метод getStringDate()
            для вывода в формате "yyyy-MM-dd" -->
            <td> <c:out value="${employee.getStringDate()}"/></td>

            <!-- Зарплата сотрудника -->
            <td> <c:out value="${employee.salary}"/></td>

            <!-- Ссылка ввиде кнопки, для редактирования данных сотрудника.
             Передаем в параметре запроса данные сотрудника.-->
            <td>
                 <a  href="<c:url value="/jsp/new-user-form.jsp">
                           <c:param name="id" value="${employee.id}"/>
                           <c:param name="department" value="${employee.department}"/>
                           <c:param name="name" value="${employee.name}"/>
                           <c:param name="date" value="${employee.getStringDate()}"/>
                           <c:param name="salary" value="${employee.salary}"/>
                           </c:url>"
                     class="btn btn_type1">
                     EDIT
                 </a>
            </td>

            <td>
            <!-- Ссылка ввиде кнопки, для удаления сотрудника.
             Передаем в параметре запроса id сотрудника.-->
                 <a  href="<c:url value="/servlet/delete">
                           <c:param name="id" value="${employee.id}"/>
                           </c:url>"
                     class="btn btn_type2">
                     DELETE
                 </a>
            </td>
        </tr>
        </c:forEach>
        </tbody>
        </table>
</div>
</c:if>

</body>
</html>
