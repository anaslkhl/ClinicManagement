<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String pageTitle = "New availability";
    String activeNav = "availabilities";
    String ctx = request.getContextPath();

    String vDoctorId = request.getParameter("doctorId") == null ? "" : request.getParameter("doctorId");
    String vDay = request.getParameter("dayOfWeek") == null ? "" : request.getParameter("dayOfWeek");
    String vStart = request.getParameter("startTime") == null ? "" : request.getParameter("startTime");
    String vEnd = request.getParameter("endTime") == null ? "" : request.getParameter("endTime");
    String vStatus = request.getParameter("status") == null ? "AVAILABLE" : request.getParameter("status");
    String vDebut = request.getParameter("dateDebut") == null ? "" : request.getParameter("dateDebut");
    String vFin = request.getParameter("dateFin") == null ? "" : request.getParameter("dateFin");

    String[] days = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
    String[] dayLabels = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6">
  <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">New availability</h2>
  <p class="mt-1 text-sm text-ink-500">Define a recurring working window for a doctor.</p>
</div>

<div class="mb-5 max-w-3xl alert alert-info" role="status">
  <svg class="mt-0.5 h-5 w-5 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4m0 4h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/></svg>
  <div>
    <p class="font-semibold">Paste the doctor's UUID</p>
    <p class="mt-1 text-xs leading-relaxed">
      There is no doctor picker on this form yet. Use the identifier from the doctor's record.
      Overlapping windows for the same doctor are rejected automatically.
    </p>
  </div>
</div>

<div class="card max-w-3xl">
  <div class="card-body">
    <form method="post" action="<%= ctx %>/availabilities" class="space-y-5">
      <div>
        <label class="label" for="doctorId">Doctor ID (UUID)</label>
        <input class="input font-mono text-xs" type="text" id="doctorId" name="doctorId" required
               value="<%= vDoctorId %>" placeholder="00000000-0000-0000-0000-000000000000">
      </div>

      <div class="grid gap-4 sm:grid-cols-2">
        <div>
          <label class="label" for="dayOfWeek">Day of week</label>
          <select class="input" id="dayOfWeek" name="dayOfWeek" required>
            <option value="" disabled <%= vDay.isEmpty() ? "selected" : "" %>>Select&hellip;</option>
            <% for (int i = 0; i < days.length; i++) { %>
              <option value="<%= days[i] %>" <%= days[i].equals(vDay) ? "selected" : "" %>><%= dayLabels[i] %></option>
            <% } %>
          </select>
        </div>
        <div>
          <label class="label" for="status">Status</label>
          <select class="input" id="status" name="status" required>
            <option value="AVAILABLE" <%= "AVAILABLE".equals(vStatus) ? "selected" : "" %>>Available</option>
            <option value="ABSENT" <%= "ABSENT".equals(vStatus) ? "selected" : "" %>>Absent</option>
          </select>
        </div>
        <div>
          <label class="label" for="startTime">Start time</label>
          <input class="input" type="time" id="startTime" name="startTime" required value="<%= vStart %>">
        </div>
        <div>
          <label class="label" for="endTime">End time</label>
          <input class="input" type="time" id="endTime" name="endTime" required value="<%= vEnd %>">
          <p class="help">Must be later than the start time.</p>
        </div>
        <div>
          <label class="label" for="dateDebut">Valid from</label>
          <input class="input" type="date" id="dateDebut" name="dateDebut" required value="<%= vDebut %>">
        </div>
        <div>
          <label class="label" for="dateFin">Valid until</label>
          <input class="input" type="date" id="dateFin" name="dateFin" required value="<%= vFin %>">
        </div>
      </div>

      <div class="flex flex-wrap items-center justify-end gap-3 border-t border-ink-200 pt-5">
        <a class="btn btn-secondary" href="<%= ctx %>/availabilities">Cancel</a>
        <button type="submit" class="btn btn-primary">Create availability</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
