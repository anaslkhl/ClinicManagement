<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String pageTitle = "Dashboard";
    String activeNav = "dashboard";
    String ctx = request.getContextPath();
    Object indexUser = session.getAttribute("user");
    boolean loggedIn = indexUser instanceof clinicmanagement.model.User;
    String indexRole = loggedIn ? ((clinicmanagement.model.User) indexUser).getRole().name() : "";
    boolean isAdmin = "ADMIN".equals(indexRole) || "STAFF".equals(indexRole);
    boolean isDoctor = "DOCTOR".equals(indexRole);
    boolean isPatient = "PATIENT".equals(indexRole);
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>

<% if (!loggedIn) { %>
<body class="min-h-screen bg-gradient-to-b from-primary-50 via-white to-white">
  <a href="#main" class="skip-link">Skip to content</a>

  <header class="mx-auto flex max-w-6xl items-center justify-between px-4 py-5 sm:px-6">
    <span class="flex items-center gap-2">
      <span class="grid h-9 w-9 place-items-center rounded-lg bg-primary-600 text-lg font-bold text-white" aria-hidden="true">+</span>
      <span class="text-lg font-semibold tracking-tight text-ink-900">Clinic<span class="text-primary-700">Manager</span></span>
    </span>
    <nav class="flex items-center gap-2">
      <a href="<%= ctx %>/users/login" class="btn btn-secondary btn-sm sm:px-4 sm:py-2 sm:text-sm">Sign in</a>
      <a href="<%= ctx %>/users/register" class="btn btn-primary btn-sm sm:px-4 sm:py-2 sm:text-sm">Create account</a>
    </nav>
  </header>

  <main id="main" class="mx-auto max-w-6xl px-4 pb-20 sm:px-6">
    <section class="grid items-center gap-10 py-12 lg:grid-cols-2 lg:py-20">
      <div>
        <span class="badge badge-role">Healthcare management</span>
        <h1 class="mt-4 text-3xl font-bold leading-tight tracking-tight text-ink-900 sm:text-4xl lg:text-5xl">
          Calm, clear care management for your clinic.
        </h1>
        <p class="mt-4 max-w-xl text-base leading-relaxed text-ink-500">
          Manage departments, doctors, availabilities and appointments from one
          consistent workspace &mdash; built for administrators, doctors and patients.
        </p>
        <div class="mt-7 flex flex-wrap gap-3">
          <a href="<%= ctx %>/users/login" class="btn btn-primary">Sign in to your workspace</a>
          <a href="<%= ctx %>/users/register" class="btn btn-secondary">Create an account</a>
        </div>
      </div>

      <div class="card p-6 sm:p-8">
        <div class="grid gap-4 sm:grid-cols-2">
          <div class="rounded-xl border border-ink-200 p-4">
            <span class="grid h-9 w-9 place-items-center rounded-lg bg-primary-50 text-primary-700" aria-hidden="true">
              <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M3 21h18M5 21V7l7-4 7 4v14M9 21v-6h6v6"/></svg>
            </span>
            <p class="mt-3 text-sm font-semibold text-ink-900">Departments</p>
            <p class="mt-1 text-xs text-ink-500">Organise your clinic structure.</p>
          </div>
          <div class="rounded-xl border border-ink-200 p-4">
            <span class="grid h-9 w-9 place-items-center rounded-lg bg-primary-50 text-primary-700" aria-hidden="true">
              <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8Zm0 0c-3.5 0-6 2-6 5v3h12v-3c0-3-2.5-5-6-5Z"/></svg>
            </span>
            <p class="mt-3 text-sm font-semibold text-ink-900">Doctors</p>
            <p class="mt-1 text-xs text-ink-500">Keep practitioner records tidy.</p>
          </div>
          <div class="rounded-xl border border-ink-200 p-4">
            <span class="grid h-9 w-9 place-items-center rounded-lg bg-primary-50 text-primary-700" aria-hidden="true">
              <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M7 3v3m10-3v3M4 8h16M5 5h14a1 1 0 0 1 1 1v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1Z"/></svg>
            </span>
            <p class="mt-3 text-sm font-semibold text-ink-900">Appointments</p>
            <p class="mt-1 text-xs text-ink-500">Plan and track visits.</p>
          </div>
          <div class="rounded-xl border border-ink-200 p-4">
            <span class="grid h-9 w-9 place-items-center rounded-lg bg-primary-50 text-primary-700" aria-hidden="true">
              <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 2m6-2a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/></svg>
            </span>
            <p class="mt-3 text-sm font-semibold text-ink-900">Availabilities</p>
            <p class="mt-1 text-xs text-ink-500">Share doctor schedules.</p>
          </div>
        </div>
      </div>
    </section>
  </main>

  <footer class="border-t border-ink-200 py-6 text-center text-xs text-ink-500">
    Clinic Manager &middot; Calm, accessible healthcare management
  </footer>

  <script src="<%= ctx %>/js/app.js" defer></script>
</body>
</html>
<% } else { %>
<body class="min-h-screen bg-ink-50 text-ink-900">
  <a href="#main" class="skip-link">Skip to content</a>

  <div class="min-h-screen lg:flex">
    <%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>

    <div class="flex min-w-0 flex-1 flex-col">
      <%@ include file="/WEB-INF/views/fragments/topbar.jspf" %>

      <main id="main" class="flex-1 p-4 sm:p-6 lg:p-8">
        <div class="mx-auto max-w-6xl">
          <%@ include file="/WEB-INF/views/fragments/flash.jspf" %>

          <section class="mb-6">
            <h2 class="text-xl font-bold tracking-tight text-ink-900 sm:text-2xl">
              Welcome back, ${sessionScope.user.prenom}
            </h2>
            <p class="mt-1 text-sm text-ink-500">
              You are signed in as <span class="badge badge-role"><%= indexRole %></span>
            </p>
          </section>

          <section class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            <% if (isAdmin) { %>
              <a class="kpi group transition-shadow hover:shadow-lg" href="<%= ctx %>/departments">
                <span class="grid h-10 w-10 place-items-center rounded-xl bg-primary-50 text-primary-700" aria-hidden="true">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M3 21h18M5 21V7l7-4 7 4v14M9 21v-6h6v6"/></svg>
                </span>
                <p class="mt-3 font-semibold text-ink-900">Departments</p>
                <p class="mt-1 text-sm text-ink-500">Create, edit and remove departments.</p>
              </a>
              <a class="kpi group transition-shadow hover:shadow-lg" href="<%= ctx %>/doctors/list">
                <span class="grid h-10 w-10 place-items-center rounded-xl bg-primary-50 text-primary-700" aria-hidden="true">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8Zm0 0c-3.5 0-6 2-6 5v3h12v-3c0-3-2.5-5-6-5Z"/></svg>
                </span>
                <p class="mt-3 font-semibold text-ink-900">Doctors</p>
                <p class="mt-1 text-sm text-ink-500">Manage practitioner records.</p>
              </a>
            <% } %>

            <% if (isAdmin || isDoctor) { %>
              <a class="kpi group transition-shadow hover:shadow-lg" href="<%= ctx %>/appointments/list">
                <span class="grid h-10 w-10 place-items-center rounded-xl bg-primary-50 text-primary-700" aria-hidden="true">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M7 3v3m10-3v3M4 8h16M5 5h14a1 1 0 0 1 1 1v13a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1Z"/></svg>
                </span>
                <p class="mt-3 font-semibold text-ink-900">Appointments</p>
                <p class="mt-1 text-sm text-ink-500">Plan, update and close visits.</p>
              </a>
              <a class="kpi group transition-shadow hover:shadow-lg" href="<%= ctx %>/availabilities">
                <span class="grid h-10 w-10 place-items-center rounded-xl bg-primary-50 text-primary-700" aria-hidden="true">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 2m6-2a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/></svg>
                </span>
                <p class="mt-3 font-semibold text-ink-900">Availabilities</p>
                <p class="mt-1 text-sm text-ink-500">Publish working hours.</p>
              </a>
            <% } %>

            <% if (isPatient) { %>
              <a class="kpi group transition-shadow hover:shadow-lg" href="<%= ctx %>/patients/register">
                <span class="grid h-10 w-10 place-items-center rounded-xl bg-primary-50 text-primary-700" aria-hidden="true">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M12 5v14M5 12h14"/></svg>
                </span>
                <p class="mt-3 font-semibold text-ink-900">Patient registration</p>
                <p class="mt-1 text-sm text-ink-500">Complete your medical profile.</p>
              </a>
              <a class="kpi group transition-shadow hover:shadow-lg" href="<%= ctx %>/patients/profile?id=${sessionScope.user.id}">
                <span class="grid h-10 w-10 place-items-center rounded-xl bg-primary-50 text-primary-700" aria-hidden="true">
                  <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 1 1-8 0 4 4 0 0 1 8 0ZM4 21a8 8 0 0 1 16 0"/></svg>
                </span>
                <p class="mt-3 font-semibold text-ink-900">My profile</p>
                <p class="mt-1 text-sm text-ink-500">Review your information.</p>
              </a>
            <% } %>
          </section>

          <section class="mt-8">
            <div class="card">
              <div class="card-header">
                <h3 class="font-semibold text-ink-900">Recent activity</h3>
              </div>
              <div class="card-body">
                <div class="empty">
                  <span class="grid h-11 w-11 place-items-center rounded-full bg-ink-100 text-ink-500" aria-hidden="true">
                    <svg class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path stroke-linecap="round" stroke-linejoin="round" d="M9 17v-6h13M3 7h18M3 7l3 10h12"/></svg>
                  </span>
                  <p class="text-sm font-medium text-ink-700">No activity to show yet</p>
                  <p class="max-w-sm text-xs text-ink-500">
                    Once you start using the modules above, your recent actions will appear here.
                  </p>
                </div>
              </div>
            </div>
          </section>
        </div>
      </main>
    </div>
  </div>

  <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
<% } %>
