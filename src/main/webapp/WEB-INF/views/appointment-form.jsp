<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter, clinicmanagement.model.Appointment, clinicmanagement.model.AppointmentStatus" %>
<%
    String pageTitle = "Appointment";
    String activeNav = "appointments";
    String ctx = request.getContextPath();

    Appointment appointment = (Appointment) request.getAttribute("appointment");
    boolean editing = appointment != null;

    String vPatientId = "";
    String vDoctorId = "";
    String vDateHeure = "";
    String vMotif = "";
    String vStatus = "PLANNED";

    if (editing) {
        vMotif = appointment.getMotif() == null ? "" : appointment.getMotif();
        vStatus = appointment.getStatus() == null ? "PLANNED" : appointment.getStatus().name();
        if (appointment.getDateHeure() != null) {
            vDateHeure = appointment.getDateHeure().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        }
        try {
            if (appointment.getPatient() != null) vPatientId = appointment.getPatient().getId().toString();
        } catch (RuntimeException ignored) { }
        try {
            if (appointment.getDoctor() != null) vDoctorId = appointment.getDoctor().getId().toString();
        } catch (RuntimeException ignored) { }
    } else {
        vPatientId = request.getParameter("patientId") == null ? "" : request.getParameter("patientId");
        vDoctorId = request.getParameter("doctorId") == null ? "" : request.getParameter("doctorId");
        vDateHeure = request.getParameter("dateHeure") == null ? "" : request.getParameter("dateHeure");
        vMotif = request.getParameter("motif") == null ? "" : request.getParameter("motif");
        vStatus = request.getParameter("status") == null ? "PLANNED" : request.getParameter("status");
    }
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6">
  <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">
    <%= editing ? "Edit appointment" : "New appointment" %>
  </h2>
  <p class="mt-1 text-sm text-ink-500">Link a patient to a doctor at a specific date and time.</p>
</div>

<div class="mb-5 max-w-3xl alert alert-info" role="status">
  <svg class="mt-0.5 h-5 w-5 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/></svg>
  <div>
    <p class="font-semibold">Identifiers must be entered manually</p>
    <p class="mt-1 text-xs leading-relaxed">
      There is no patient or doctor picker yet, so paste the patient and doctor UUIDs
      exactly as they appear in their records.
    </p>
  </div>
</div>

<div class="card max-w-3xl">
  <div class="card-body">
    <form method="post"
          action="<%= ctx %><%= editing ? "/appointments/update" : "/appointments/create" %>"
          class="space-y-5">
      <% if (editing) { %>
        <input type="hidden" name="id" value="<%= appointment.getId() %>">
      <% } %>

      <div class="grid gap-4 sm:grid-cols-2">
        <div>
          <label class="label" for="patientId">Patient ID (UUID)</label>
          <input class="input font-mono text-xs" type="text" id="patientId" name="patientId" required
                 value="<%= vPatientId %>" placeholder="00000000-0000-0000-0000-000000000000">
        </div>
        <div>
          <label class="label" for="doctorId">Doctor ID (UUID)</label>
          <input class="input font-mono text-xs" type="text" id="doctorId" name="doctorId" required
                 value="<%= vDoctorId %>" placeholder="00000000-0000-0000-0000-000000000000">
        </div>
        <div>
          <label class="label" for="dateHeure">Date and time</label>
          <input class="input" type="datetime-local" id="dateHeure" name="dateHeure" required
                 value="<%= vDateHeure %>">
          <p class="help">Appointments cannot be scheduled in the past.</p>
        </div>
        <div>
          <label class="label" for="status">Status</label>
          <select class="input" id="status" name="status" required>
            <option value="PLANNED" <%= "PLANNED".equals(vStatus) ? "selected" : "" %>>Planned</option>
            <option value="DONE" <%= "DONE".equals(vStatus) ? "selected" : "" %>>Done</option>
            <option value="CANCELED" <%= "CANCELED".equals(vStatus) ? "selected" : "" %>>Canceled</option>
          </select>
        </div>
      </div>

      <div>
        <label class="label" for="motif">Reason for visit</label>
        <textarea class="input" id="motif" name="motif" rows="3" maxlength="500"
                  placeholder="Brief description (optional, up to 500 characters)"><%= vMotif %></textarea>
      </div>

      <div class="flex flex-wrap items-center justify-end gap-3 border-t border-ink-200 pt-5">
        <a class="btn btn-secondary" href="<%= ctx %>/appointments/list">Cancel</a>
        <button type="submit" class="btn btn-primary">
          <%= editing ? "Save changes" : "Create appointment" %>
        </button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
