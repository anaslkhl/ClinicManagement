<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, clinicmanagement.model.Doctor" %>
<%
    String pageTitle = "Doctors";
    String activeNav = "doctors";
    String ctx = request.getContextPath();

    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6 flex flex-wrap items-center justify-between gap-3">
  <div>
    <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">Doctors</h2>
    <p class="mt-1 text-sm text-ink-500">Manage practitioner records and their assignments.</p>
  </div>
  <a class="btn btn-primary" href="<%= ctx %>/doctors/register">
    <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path stroke-linecap="round" d="M12 5v14M5 12h14"/></svg>
    New doctor
  </a>
</div>

<% if (doctors == null || doctors.isEmpty()) { %>

  <div class="empty">
    <span class="grid h-11 w-11 place-items-center rounded-full bg-ink-100 text-ink-500" aria-hidden="true">
      <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8Zm0 0c-3.5 0-6 2-6 5v3h12v-3c0-3-2.5-5-6-5Z"/></svg>
    </span>
    <p class="text-sm font-medium text-ink-700">No doctors yet</p>
    <p class="max-w-md text-xs text-ink-500">Register your first practitioner to begin scheduling appointments.</p>
    <a class="btn btn-primary btn-sm" href="<%= ctx %>/doctors/register">New doctor</a>
  </div>

<% } else { %>

  <div class="card overflow-hidden">
    <div class="overflow-x-auto">
      <table class="table">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Matricule</th>
            <th scope="col">Title</th>
            <th scope="col">Contact</th>
            <th scope="col" class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <% for (Doctor doctor : doctors) { %>
            <tr>
              <td class="font-medium text-ink-900">
                <%= doctor.getPrenom() %> <%= doctor.getNom() %>
                <span class="badge badge-role ml-2"><%= doctor.getRole() %></span>
              </td>
              <td class="text-sm text-ink-700"><%= doctor.getMatricule() == null ? "—" : doctor.getMatricule() %></td>
              <td class="text-sm text-ink-700"><%= doctor.getTitre() == null ? "—" : doctor.getTitre() %></td>
              <td class="text-sm text-ink-500">
                <span class="block"><%= doctor.getEmail() %></span>
                <span class="block"><%= doctor.getTelephone() %></span>
              </td>
              <td>
                <div class="flex items-center justify-end gap-2">
                  <a class="btn btn-secondary btn-sm" href="<%= ctx %>/doctors/edit?id=<%= doctor.getId() %>">Edit</a>
                  <form method="post" action="<%= ctx %>/doctors/delete" class="inline"
                        data-confirm="Delete doctor &quot;<%= doctor.getPrenom() %> <%= doctor.getNom() %>&quot;? This cannot be undone.">
                    <input type="hidden" name="id" value="<%= doctor.getId() %>">
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
