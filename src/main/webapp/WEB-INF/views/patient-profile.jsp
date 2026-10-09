<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String pageTitle = "Patient profile";
    String activeNav = "my-profile";
    String ctx = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<% if (request.getAttribute("patient") == null) { %>

  <div class="empty">
    <span class="grid h-11 w-11 place-items-center rounded-full bg-ink-100 text-ink-500" aria-hidden="true">
      <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0ZM4 21a8 8 0 0 1 16 0"/></svg>
    </span>
    <p class="text-sm font-medium text-ink-700">No patient profile found</p>
    <p class="max-w-md text-xs text-ink-500">
      Use the patient registration form to create a medical profile.
    </p>
    <a class="btn btn-primary btn-sm" href="<%= ctx %>/patients/register">Patient registration</a>
  </div>

<% } else { %>

  <div class="mb-6 flex flex-wrap items-start justify-between gap-3">
    <div>
      <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">Patient profile</h2>
      <p class="mt-1 text-sm text-ink-500">Personal and medical identification details.</p>
    </div>
    <a class="btn btn-secondary btn-sm" href="<%= ctx %>/">Back to dashboard</a>
  </div>

  <div class="grid gap-4 lg:grid-cols-3">
    <div class="card lg:col-span-1">
      <div class="card-body text-center">
        <span class="mx-auto grid h-16 w-16 place-items-center rounded-full bg-primary-100 text-primary-700" aria-hidden="true">
          <svg class="h-8 w-8" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0ZM4 21a8 8 0 0 1 16 0"/></svg>
        </span>
        <p class="mt-4 text-lg font-semibold text-ink-900">${patient.prenom} ${patient.nom}</p>
        <p class="text-sm text-ink-500">${patient.email}</p>
        <span class="badge badge-role mt-3">${patient.role}</span>
      </div>
    </div>

    <div class="card lg:col-span-2">
      <div class="card-header">
        <h3 class="font-semibold text-ink-900">Details</h3>
      </div>
      <div class="card-body">
        <dl class="grid gap-x-8 gap-y-4 sm:grid-cols-2">
          <div>
            <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Patient ID</dt>
            <dd class="mt-1 break-all text-sm text-ink-900">${patient.id}</dd>
          </div>
          <div>
            <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">National ID (CIN)</dt>
            <dd class="mt-1 text-sm text-ink-900">${patient.cin}</dd>
          </div>
          <div>
            <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Date of birth</dt>
            <dd class="mt-1 text-sm text-ink-900">${patient.dateNaissance}</dd>
          </div>
          <div>
            <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Gender</dt>
            <dd class="mt-1 text-sm text-ink-900">${patient.gender}</dd>
          </div>
          <div>
            <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Phone</dt>
            <dd class="mt-1 text-sm text-ink-900">${patient.telephone}</dd>
          </div>
          <div>
            <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Status</dt>
            <dd class="mt-1 text-sm">
              <span class="badge ${patient.active ? 'badge-done' : 'badge-canceled'}">
                ${patient.active ? 'Active' : 'Inactive'}
              </span>
            </dd>
          </div>
          <div class="sm:col-span-2">
            <dt class="text-xs font-semibold uppercase tracking-wide text-ink-500">Home address</dt>
            <dd class="mt-1 text-sm text-ink-900">${patient.adresse}</dd>
          </div>
        </dl>
      </div>
    </div>
  </div>

<% } %>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
