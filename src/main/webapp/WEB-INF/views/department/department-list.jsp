<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, clinicmanagement.model.Department" %>
<%
    String pageTitle = "Departments";
    String activeNav = "departments";
    String ctx = request.getContextPath();

    List<Department> departments = (List<Department>) request.getAttribute("departments");
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6 flex flex-wrap items-center justify-between gap-3">
  <div>
    <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">Departments</h2>
    <p class="mt-1 text-sm text-ink-500">Organise your clinic into clinical departments.</p>
  </div>
  <a class="btn btn-primary" href="<%= ctx %>/departments/new">
    <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path stroke-linecap="round" d="M12 5v14M5 12h14"/></svg>
    New department
  </a>
</div>

<% if (departments == null || departments.isEmpty()) { %>

  <div class="empty">
    <span class="grid h-11 w-11 place-items-center rounded-full bg-ink-100 text-ink-500" aria-hidden="true">
      <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M3 21h18M5 21V7l7-4 7 4v14M9 21v-6h6v6"/></svg>
    </span>
    <p class="text-sm font-medium text-ink-700">No departments yet</p>
    <p class="max-w-md text-xs text-ink-500">Create your first department to start grouping specialties and doctors.</p>
    <a class="btn btn-primary btn-sm" href="<%= ctx %>/departments/new">New department</a>
  </div>

<% } else { %>

  <div class="card overflow-hidden">
    <div class="overflow-x-auto">
      <table class="table">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Identifier</th>
            <th scope="col" class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <% for (Department department : departments) { %>
            <tr>
              <td class="font-medium text-ink-900"><%= department.getName() %></td>
              <td class="text-xs text-ink-500"><%= department.getId() %></td>
              <td>
                <div class="flex items-center justify-end gap-2">
                  <a class="btn btn-secondary btn-sm" href="<%= ctx %>/departments/edit?id=<%= department.getId() %>">Edit</a>
                  <form method="post" action="<%= ctx %>/departments/delete" class="inline"
                        data-confirm="Delete department &quot;<%= department.getName() %>&quot;? This cannot be undone.">
                    <input type="hidden" name="id" value="<%= department.getId() %>">
                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                  </form>
                </div>
              </td>
            </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>

<% } %>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
