<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="clinicmanagement.model.Department" %>
<%
    String pageTitle = "Department";
    String activeNav = "departments";
    String ctx = request.getContextPath();

    Department department = (Department) request.getAttribute("department");
    boolean editing = department != null;
    String departmentId = editing ? department.getId().toString() : request.getParameter("id");
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6">
  <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">
    <%= editing ? "Edit department" : "New department" %>
  </h2>
  <p class="mt-1 text-sm text-ink-500">
    <%= editing ? "Update the department name." : "Add a department to your clinic structure." %>
  </p>
</div>

<div class="card max-w-xl">
  <div class="card-body">
    <form method="post"
          action="<%= ctx %><%= editing ? "/departments/update" : "/departments/create" %>"
          class="space-y-4">
      <% if (editing && departmentId != null) { %>
        <input type="hidden" name="id" value="<%= departmentId %>">
      <% } %>

      <div>
        <label class="label" for="name">Department name</label>
        <input class="input" type="text" id="name" name="name" required maxlength="100"
               value="<%= editing ? department.getName() : (request.getParameter("name") == null ? "" : request.getParameter("name")) %>"
               placeholder="Cardiology">
        <p class="help">A short, unique name (up to 100 characters).</p>
      </div>

      <div class="flex flex-wrap items-center justify-end gap-3 border-t border-ink-200 pt-5">
        <a class="btn btn-secondary" href="<%= ctx %>/departments">Cancel</a>
        <button type="submit" class="btn btn-primary">
          <%= editing ? "Save changes" : "Create department" %>
        </button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
