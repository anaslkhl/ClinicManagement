<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.time.format.DateTimeFormatter, clinicmanagement.model.Appointment, clinicmanagement.model.AppointmentStatus" %>
<%
    String pageTitle = "Appointments";
    String activeNav = "appointments";
    String ctx = request.getContextPath();

    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' HH:mm");
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6 flex flex-wrap items-center justify-between gap-3">
  <div>
    <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">Appointments</h2>
    <p class="mt-1 text-sm text-ink-500">Plan, update and close patient visits.</p>
  </div>
  <a class="btn btn-primary" href="<%= ctx %>/appointments/create">
    <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"><path stroke-linecap="round" d="M12 5v14M5 12h14"/></svg>
    New appointment
  </a>
</div>

<div class="mb-5 alert alert-info" role="status">
  <svg class="mt-0.5 h-5 w-5 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/></svg>
  <div>
    <p class="font-semibold">Patient and doctor names are not shown</p>
    <p class="mt-1 text-xs leading-relaxed">
      The appointment list currently loads patient and doctor references lazily, so
      related names cannot be displayed without a backend change. Dates, status and
      reasons are unaffected.
    </p>
  </div>
</div>

<% if (appointments == null || appointments.isEmpty()) { %>

  <div class="empty">
    <span class="grid h-11 w-11 place-items-center rounded-full bg-ink-100 text-ink-500" aria-hidden="true">
      <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M7 3v3m10-3v3M4 8h16M5 5h14a1 1 0 0 1 1 1v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1Z"/></svg>
    </span>
    <p class="text-sm font-medium text-ink-700">No appointments scheduled</p>
    <p class="max-w-md text-xs text-ink-500">Create an appointment to see it appear in this schedule.</p>
    <a class="btn btn-primary btn-sm" href="<%= ctx %>/appointments/create">New appointment</a>
  </div>

<% } else { %>

  <div class="card overflow-hidden">
    <div class="overflow-x-auto">
      <table class="table">
        <thead>
          <tr>
            <th scope="col">Date</th>
            <th scope="col">Status</th>
            <th scope="col">Reason</th>
            <th scope="col">Reference</th>
            <th scope="col" class="text-right">Actions</th>
          </tr>
        </thead>
        <tbody>
          <% for (Appointment appointment : appointments) {
               AppointmentStatus status = appointment.getStatus();
               String badge = "badge-canceled";
               if (status == AppointmentStatus.PLANNED) badge = "badge-planned";
               else if (status == AppointmentStatus.DONE) badge = "badge-done";
          %>
            <tr>
              <td class="whitespace-nowrap font-medium text-ink-900"><%= appointment.getDateHeure() == null ? "—" : fmt.format(appointment.getDateHeure()) %></td>
              <td><span class="badge <%= badge %>"><%= status %></span></td>
              <td class="max-w-xs truncate text-sm text-ink-700"><%= appointment.getMotif() == null || appointment.getMotif().isBlank() ? "—" : appointment.getMotif() %></td>
              <td class="text-xs text-ink-500"><%= appointment.getId() %></td>
              <td>
                <div class="flex items-center justify-end gap-2">
                  <a class="btn btn-secondary btn-sm" href="<%= ctx %>/appointments/edit?id=<%= appointment.getId() %>">Edit</a>
                  <form method="post" action="<%= ctx %>/appointments/delete" class="inline"
                        data-confirm="Delete this appointment? This cannot be undone.">
                    <input type="hidden" name="id" value="<%= appointment.getId() %>">
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
