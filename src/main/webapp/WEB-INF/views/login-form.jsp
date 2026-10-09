<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String pageTitle = "Sign in";
    String activeNav = "";
    String ctx = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<body class="min-h-screen bg-gradient-to-b from-primary-50 via-white to-white">
  <a href="#main" class="skip-link">Skip to content</a>

  <main id="main" class="flex min-h-screen items-center justify-center px-4 py-10">
    <section class="w-full max-w-md">
      <div class="mb-6 flex items-center justify-center gap-2">
        <a href="<%= ctx %>/" class="flex items-center gap-2">
          <span class="grid h-9 w-9 place-items-center rounded-lg bg-primary-600 text-lg font-bold text-white" aria-hidden="true">+</span>
          <span class="text-lg font-semibold tracking-tight text-ink-900">Clinic<span class="text-primary-700">Manager</span></span>
        </a>
      </div>

      <div class="card">
        <div class="card-body sm:p-7">
          <h1 class="text-xl font-bold tracking-tight text-ink-900">Welcome back</h1>
          <p class="mt-1 text-sm text-ink-500">Sign in to access your clinic workspace.</p>

          <div class="mt-5">
            <%@ include file="/WEB-INF/views/fragments/flash.jspf" %>
          </div>

          <form method="post" action="<%= ctx %>/users/login" class="mt-5 space-y-4" novalidate>
            <div>
              <label class="label" for="email">Email address</label>
              <input class="input" type="email" id="email" name="email"
                     value="${param.email}" autocomplete="email" required
                     placeholder="you@example.com">
              <p class="help">We never share your email address.</p>
            </div>

            <div>
              <label class="label" for="password">Password</label>
              <input class="input" type="password" id="password" name="password"
                     autocomplete="current-password" required minlength="6"
                     placeholder="Your password">
            </div>

            <button type="submit" class="btn btn-primary btn-block">
              <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M15 12H3m0 0 3-3m-3 3 3 3m6-9h6a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1h-6"/></svg>
              Sign in
            </button>
          </form>

          <p class="mt-6 text-center text-sm text-ink-500">
            No account yet?
            <a class="font-semibold text-primary-700 hover:text-primary-800" href="<%= ctx %>/users/register">Create one</a>
          </p>
        </div>
      </div>

      <p class="mt-6 text-center text-xs text-ink-500">
        <a class="hover:text-ink-700" href="<%= ctx %>/">Back to home</a>
      </p>
    </section>
  </main>

  <script src="<%= ctx %>/js/app.js" defer></script>
</body>
</html>
