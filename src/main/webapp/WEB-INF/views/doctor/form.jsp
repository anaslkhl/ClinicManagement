<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="clinicmanagement.model.Doctor" %>
<%
    String pageTitle = "Doctor";
    String activeNav = "doctors";
    String ctx = request.getContextPath();

    Doctor doctor = (Doctor) request.getAttribute("doctor");
    boolean editing = doctor != null;

    String vNom = editing ? doctor.getNom() : request.getParameter("nom");
    String vPrenom = editing ? doctor.getPrenom() : request.getParameter("prenom");
    String vEmail = editing ? doctor.getEmail() : request.getParameter("email");
    String vTelephone = editing ? doctor.getTelephone() : request.getParameter("telephone");
    String vMatricule = editing ? doctor.getMatricule() : request.getParameter("matricule");
    String vTitre = editing ? doctor.getTitre() : request.getParameter("titre");
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6">
  <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">
    <%= editing ? "Edit doctor" : "New doctor" %>
  </h2>
  <p class="mt-1 text-sm text-ink-500">
    <%= editing ? "Update the practitioner's account details." : "Register a new practitioner account." %>
  </p>
</div>

<div class="mb-5 max-w-3xl alert alert-warning" role="status">
  <svg class="mt-0.5 h-5 w-5 shrink-0" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M12 9v4m0 4h.01M10.3 3.9 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0Z"/></svg>
  <div>
    <p class="font-semibold">Specialty assignment is not available yet</p>
    <p class="mt-1 text-xs leading-relaxed">
      A doctor record requires a specialty, but the current backend does not accept it
      from this form. Saving may fail with a validation error until that is wired up.
    </p>
  </div>
</div>

<div class="card max-w-3xl">
  <div class="card-body">
    <form method="post"
          action="<%= ctx %><%= editing ? "/doctors/update" : "/doctors/create" %>"
          class="space-y-6">
      <% if (editing) { %>
        <input type="hidden" name="id" value="<%= doctor.getId() %>">
      <% } %>

      <fieldset class="space-y-4">
        <legend class="text-sm font-semibold text-ink-700">Personal information</legend>
        <div class="grid gap-4 sm:grid-cols-2">
          <div>
            <label class="label" for="nom">Last name</label>
            <input class="input" type="text" id="nom" name="nom" required value="<%= vNom == null ? "" : vNom %>" placeholder="Amrani">
          </div>
          <div>
            <label class="label" for="prenom">First name</label>
            <input class="input" type="text" id="prenom" name="prenom" required value="<%= vPrenom == null ? "" : vPrenom %>" placeholder="Salma">
          </div>
          <div>
            <label class="label" for="email">Email address</label>
            <input class="input" type="email" id="email" name="email" required value="<%= vEmail == null ? "" : vEmail %>" placeholder="doctor@example.com">
          </div>
          <div>
            <label class="label" for="telephone">Phone number</label>
            <input class="input" type="tel" id="telephone" name="telephone" required value="<%= vTelephone == null ? "" : vTelephone %>" placeholder="0612345678">
          </div>
        </div>
      </fieldset>

      <fieldset class="space-y-4">
        <legend class="text-sm font-semibold text-ink-700">Professional information</legend>
        <div class="grid gap-4 sm:grid-cols-2">
          <div>
            <label class="label" for="matricule">Matricule</label>
            <input class="input" type="text" id="matricule" name="matricule" required value="<%= vMatricule == null ? "" : vMatricule %>" placeholder="MED-0001">
            <p class="help">Unique staff identifier.</p>
          </div>
          <div>
            <label class="label" for="titre">Title</label>
            <input class="input" type="text" id="titre" name="titre" value="<%= vTitre == null ? "" : vTitre %>" placeholder="Cardiologist">
          </div>
        </div>
      </fieldset>

      <fieldset class="space-y-4">
        <legend class="text-sm font-semibold text-ink-700">Account</legend>
        <div>
          <label class="label" for="password">Password</label>
          <input class="input" type="password" id="password" name="password" <%= editing ? "" : "required" %> minlength="6" placeholder="<%= editing ? "Leave as-is to keep current password" : "At least 6 characters" %>">
          <% if (editing) { %>
            <p class="help">Re-enter the password to submit changes.</p>
          <% } %>
        </div>
      </fieldset>

      <div class="flex flex-wrap items-center justify-end gap-3 border-t border-ink-200 pt-5">
        <a class="btn btn-secondary" href="<%= ctx %>/doctors/list">Cancel</a>
        <button type="submit" class="btn btn-primary">
          <%= editing ? "Save changes" : "Register doctor" %>
        </button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
