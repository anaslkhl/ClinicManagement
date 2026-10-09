<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, clinicmanagement.model.Availability, clinicmanagement.model.AvailabilityStatus" %>
<%
    String pageTitle = "Availabilities";
    String activeNav = "availabilities";
    String ctx = request.getContextPath();

    List<Availability> availabilities = (List<Availability>) request.getAttribute("availabilities");
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6 flex flex-wrap items-center justify-between gap-3">
  <div>
    <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">Availabilities</h2>
    <p class="mt-1 text-sm text-ink-500">Publish doctor working hours and absences.</p>
  </div>
  <a class="btn btn-primary" href="<%= ctx %>/availabilities/create">
    <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path stroke-linecap="round" d="M12 5v14M5 12h14"/></svg>
    New availability
  </a>
</div>

<% if (availabilities == null || availabilities.isEmpty()) { %>

  <div class="empty">
    <span class="grid h-11 w-11 place-items-center rounded-full bg-ink-100 text-ink-500" aria-hidden="true">
      <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 2m6-2a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/></svg>
    </span>
    <p class="text-sm font-medium text-ink-700">No availabilities published</p>
    <p class="max-w-md text-xs text-ink-500">Add a doctor's working hours to make them bookable.</p>
    <a class="btn btn-primary btn-sm" href="<%= ctx %>/availabilities/create">New availability</a>
  </div>

<% } else { %>

  <div class="card overflow-hidden">
    <div class="overflow-x-auto">
      <table class="table">
        <thead>
          <tr>
            <th scope="col">Day</th>
            <th scope="col">Time</th>
            <th scope="col">Period</th>
            <th scope="col">Status</th>
            <th scope="col" class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <% for (Availability availability : availabilities) {
               AvailabilityStatus status = availability.getStatus();
               String badge = status == AvailabilityStatus.AVAILABLE ? "badge-available" : "badge-absent";
          %>
            <tr>
              <td class="font-medium capitalize text-ink-900"><%= availability.getDayOfWeek() == null ? "—" : availability.getDayOfWeek().name().toLowerCase() %></td>
              <td class="whitespace-nowrap text-sm text-ink-700">
                <%= availability.getStartTime() == null ? "—" : availability.getStartTime() %> &ndash;
                <%= availability.getEndTime() == null ? "—" : availability.getEndTime() %>
              </td>
              <td class="whitespace-nowrap text-sm text-ink-500">
                <%= availability.getDateDebut() %> &rarr; <%= availability.getDateFin() %>
              </td>
              <td><span class="badge <%= badge %>"><%= status %></span></td>
              <td>
                <div class="flex items-center justify-end gap-2">
                  <a class="btn btn-secondary btn-sm" href="<%= ctx %>/availabilities/<%= availability.getId() %>">View</a>
                  <button type="button" class="btn btn-danger btn-sm"
                          data-delete-url="<%= ctx %>/availabilities/<%= availability.getId() %>"
                          data-confirm="Delete this availability? This cannot be undone."
                          data-success="Availability deleted."
                          data-redirect="<%= ctx %>/availabilities">Delete</button>
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
