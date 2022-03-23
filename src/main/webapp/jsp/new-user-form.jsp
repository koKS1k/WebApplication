<!DOCTYPE html>
<html>
<head>
    <title>Employee	Management App</title>
    <!-- Импорт CSS -->
    <link href="../css/user-form.css" rel="stylesheet" type="text/css">

    <%@ taglib prefix = "c"  uri ="http://java.sun.com/jsp/jstl/core" %>
</head>

<!-- Заголовок-->
<header>
    <div class="header">
        <p class="header_text" style="margin-left:20px"> Employee	Management App </p>
    </div>
</header>

<body class="body">
<div class="user_form">

    <!-- Если в запросе передается id сотрудника, то выводим название формы "Update employee"
         Если в запросе не передается id сотрудника, то выводим название формы "Add new employee"
     -->
    <div class="user_form_title">
        <c:if test="${not empty param.id}">
            Update employee
        </c:if>

        <c:if test="${empty param.id}">
            Add new employee
        </c:if>
    </div>

    <!-- Если в запросе передается id сотрудника, то данные формы будут отправляться по пути "/servlet/edit"
         Если в запросе не передается id сотрудника, то данные формы будут отправляться по пути "/servlet/new"
    -->
    <form class="inputs"
          action= "<c:if test="${not empty param.id}">
                  ${pageContext.request.contextPath}/servlet/edit
                  </c:if>

                  <c:if test="${empty param.id}">
                  ${pageContext.request.contextPath}/servlet/new
                  </c:if>"

          method="post">

        <!-- Перекладываем id из параметра запроса в параметр запроса формы -->
        <input type="hidden" name="id" value="${param.id}" />

        <!-- Поле department для ввода названия отдела. Если изменяем сущ. сотрудника,
         то в поле выводится отдел, в котором он зачислен. Если создаем нового, то поле пустое -->
        <fieldset class="form-group">
            <label>Department</label>
            <input type="text"
                   list="departments"
                   value="${param.department}"
                   class="form-control"
                   name="department"
                   required="required">
        </fieldset>

        <!-- datalist для перечня существующих отделов. создано для удобства -->
        <datalist id="departments">
            <c:forEach var="department" items="${applicationScope['departmentsList']}">
            <option value=${department.name}>
            </c:forEach>
        </datalist>

        <!-- Поле name для ввода имени. Если изменяем сущ. сотрудника,
         то в поле выводится его имя. Если создаем нового, то поле пустое -->
        <fieldset class="form-group">
            <label>Name</label>
            <input type="text"
                   value="${param.name}"
                   class="form-control"
                   name="name"
                   required="required">
        </fieldset>

        <!-- Поле date для ввода даты рождения. Если изменяем сущ. сотрудника,
        то в поле выводится его дата рождения. Если создаем нового, то поле пустое -->
        <fieldset class="form-group">
            <label>Date of birth</label>
            <input type="date"
                   max="2004-01-01"
                   value="${param.date}"
                   class="form-control"
                   name="date"
                   required="required">
        </fieldset>

        <!-- Поле salary для ввода зарплаты. Если изменяем сущ. сотрудника,
        то в поле выводится его зарплата. Если создаем нового, то поле пустое -->
        <fieldset class="form-group">
            <label>Salary</label>
            <input type="text"
                   value="${param.salary}"
                   class="form-control"
                   name="salary"
                   required="required">
        </fieldset>

        <!-- Кнопка для отправки формы -->
        <div class="button_position">
            <button type="submit" class="main_button">Save</button>
        </div>
        </form>
    </div>

</body>
</html>
