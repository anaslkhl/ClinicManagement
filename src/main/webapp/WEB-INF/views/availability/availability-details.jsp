<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="clinicmanagement.model.Availability, clinicmanagement.model.AvailabilityStatus" %>
<%
    String pageTitle = "Availability details";
    String activeNav = "availabilities";
    String ctx = request.getContextPath();

    Availability availability = (Availability) request.getAttribute("availability");
    AvailabilityStatus status = availability == null ? null : availability.getStatus();
    String badge = status == AvailabilityStatus.AVAILABLE ? "badge-available" : "badge-absent";

    String doctorRef = "Unavailable";
    if (availability != null) {
        try {
            if (availability.getDoctor() != null) doctorRef = availability.getDoctor().getId().toString();
        } catch (RuntimeException ignored) { }
    }
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6 flex flex-wrap items-center justify-between gap-3">
  <div>
    <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">Availability details</h2>
    <p class="mt-1 text-sm text-ink-500">Review a single working window.</p>
  </div>
  <a class="btn btn-secondary btn-sm" href="<%= ctx %>/availabilities">Back to list</a>
</div>

<% if (availability == null) { %>

  <div class="empty">
    <p class="text-sm font-medium text-ink-700">Availability not found</p>
    <a class="btn btn-secondary btn-sm" href="<%= ctx %>/availabilities">Back to list</a>
  </div>

<% } else { %>

  <div class="card max-w-3xl">
    <div class="card-body">
      <dl class="grid gap-x-8 gap-y-4 sm:grid-cols-2">
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Day of week</dt>
          <dd class="mt-1 text-sm capitalize text-ink-900"><%= availability.getDayOfWeek() == null ? "—" : availability.getDayOfWeek().name().toLowerCase() %></dd>
        </div>
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Status</dt>
          <dd class="mt-1 text-sm"><span class="badge <%= badge %>"><%= status %></span></dd>
        </div>
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Start time</dt>
          <dd class="mt-1 text-sm text-ink-900"><%= availability.getStartTime() == null ? "—" : availability.getStartTime() %></dd>
        </div>
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">End time</dt>
          <dd class="mt-1 text-sm text-ink-900"><%= availability.getEndTime() == null ? "—" : availability.getEndTime() %></dd>
        </div>
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Valid from</dt>
          <dd class="mt-1 text-sm text-ink-900"><%= availability.getDateDebut() %></dd>
        </div>
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Valid until</dt>
          <dd class="mt-1 text-sm text-ink-900"><%= availability.getDateFin() %></dd>
        </div>
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Doctor reference</dt>
          <dd class="mt-1 break-all font-mono text-xs text-ink-900"><%= doctorRef %></dd>
        </div>
        <div>
          <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Availability ID</dt>
          <dd class="mt-1 break-all font-mono text-xs text-ink-900"><%= availability.getId() %></dd>
        </div>
      </dl>
    </div>
    <div class="card-header justify-end border-t border-b-0">
      <button type="button" class="btn btn-danger"
              data-delete-url="<%= ctx %>/availabilities/<%= availability.getId() %>"
              data-confirm="Delete this availability? This cannot be undone."
              data-success="Availability deleted."
              data-redirect="<%= ctx %>/availabilities">Delete availability</button>
    </div>
  </div>

<% } %>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
