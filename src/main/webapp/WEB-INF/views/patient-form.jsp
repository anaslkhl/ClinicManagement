<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String pageTitle = "Patient registration";
    String activeNav = "patient-register";
    String ctx = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<%@ include file="/WEB-INF/views/fragments/app-open.jspf" %>

<div class="mb-6">
  <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">Patient registration</h2>
  <p class="mt-1 text-sm text-ink-500">
    Complete your medical profile so your care team can serve you safely.
  </p>
</div>

<div class="card max-w-3xl">
  <div class="card-body">
    <form method="post" action="<%= ctx %>/patients/register" class="space-y-6">
      <fieldset class="space-y-4">
        <legend class="text-sm font-semibold text-ink-700">Identity</legend>
        <div class="grid gap-4 sm:grid-cols-2">
          <div>
            <label class="label" for="nom">Last name</label>
            <input class="input" type="text" id="nom" name="nom" value="${param.nom}" required placeholder="Amrani">
          </div>
          <div>
            <label class="label" for="prenom">First name</label>
            <input class="input" type="text" id="prenom" name="prenom" value="${param.prenom}" required placeholder="Ali">
          </div>
          <div>
            <label class="label" for="cin">National ID (CIN)</label>
            <input class="input" type="text" id="cin" name="cin" value="${param.cin}" required
                   inputmode="numeric" placeholder="AB123456">
            <p class="help">Required, at least 9 characters.</p>
          </div>
          <div>
            <label class="label" for="gender">Gender</label>
            <select class="input" id="gender" name="gender" required>
              <option value="" disabled ${empty param.gender ? 'selected' : ''}>Select&hellip;</option>
              <option value="MALE" ${param.gender == 'MALE' ? 'selected' : ''}>Male</option>
              <option value="FEMALE" ${param.gender == 'FEMALE' ? 'selected' : ''}>Female</option>
            </select>
          </div>
          <div>
            <label class="label" for="dateNaissance">Date of birth</label>
            <input class="input" type="date" id="dateNaissance" name="dateNaissance"
                   value="${param.dateNaissance}" required max="<%= java.time.LocalDate.now() %>">
          </div>
          <div>
            <label class="label" for="telephone">Phone number</label>
            <input class="input" type="tel" id="telephone" name="telephone" value="${param.telephone}" required placeholder="0612345678">
          </div>
        </div>
      </fieldset>

      <fieldset class="space-y-4">
        <legend class="text-sm font-semibold text-ink-700">Contact &amp; account</legend>
        <div class="grid gap-4 sm:grid-cols-2">
          <div>
            <label class="label" for="email">Email address</label>
            <input class="input" type="email" id="email" name="email" value="${param.email}" required placeholder="you@example.com">
          </div>
          <div>
            <label class="label" for="password">Password</label>
            <input class="input" type="password" id="password" name="password" required minlength="6" placeholder="At least 6 characters">
          </div>
          <div class="sm:col-span-2">
            <label class="label" for="adresse">Home address</label>
            <textarea class="input" id="adresse" name="adresse" rows="2" required placeholder="Street, city, postal code">${param.adresse}</textarea>
            <p class="help">Up to 255 characters.</p>
          </div>
        </div>
      </fieldset>

      <div class="flex flex-wrap items-center justify-end gap-3 border-t border-ink-200 pt-5">
        <a class="btn btn-secondary" href="<%= ctx %>/">Cancel</a>
        <button type="submit" class="btn btn-primary">Save patient profile</button>
      </div>
    </form>
  </div>
</div>

<%@ include file="/WEB-INF/views/fragments/app-close.jspf" %>
