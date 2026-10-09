<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String pageTitle = "Create account";
    String activeNav = "";
    String ctx = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/fragments/head.jspf" %>
<body class="min-h-screen bg-gradient-to-b from-primary-50 via-white to-white">
  <a href="#main" class="skip-link">Skip to content</a>

  <main id="main" class="flex min-h-screen items-center justify-center px-4 py-10">
    <section class="w-full max-w-lg">
      <div class="mb-6 flex items-center justify-center gap-2">
        <a href="<%= ctx %>/" class="flex items-center gap-2">
          <span class="grid h-9 w-9 place-items-center rounded-lg bg-primary-600 text-lg font-bold text-white" aria-hidden="true">+</span>
          <span class="text-lg font-semibold tracking-tight text-ink-900">Clinic<span class="text-primary-700">Manager</span></span>
        </a>
      </div>

      <div class="card">
        <div class="card-body sm:p-7">
          <h1 class="text-xl font-bold tracking-tight text-ink-900">Create your account</h1>
          <p class="mt-1 text-sm text-ink-500">It only takes a minute. You can complete your patient profile afterwards.</p>

          <div class="mt-5">
            <%@ include file="/WEB-INF/views/fragments/flash.jspf" %>
          </div>

          <form method="post" action="<%= ctx %>/users/register" class="mt-5 space-y-4">
            <div class="grid gap-4 sm:grid-cols-2">
              <div>
                <label class="label" for="nom">Last name</label>
                <input class="input" type="text" id="nom" name="nom"
                       value="${param.nom}" autocomplete="family-name" required
                       placeholder="Amrani">
              </div>
              <div>
                <label class="label" for="prenom">First name</label>
                <input class="input" type="text" id="prenom" name="prenom"
                       value="${param.prenom}" autocomplete="given-name" required
                       placeholder="Ali">
              </div>
            </div>

            <div>
              <label class="label" for="email">Email address</label>
              <input class="input" type="email" id="email" name="email"
                     value="${param.email}" autocomplete="email" required
                     placeholder="you@example.com">
            </div>

            <div>
              <label class="label" for="telephone">Phone number</label>
              <input class="input" type="tel" id="telephone" name="telephone"
                     value="${param.telephone}" autocomplete="tel" required
                     placeholder="0612345678">
            </div>

            <div>
              <label class="label" for="password">Password</label>
              <input class="input" type="password" id="password" name="password"
                     autocomplete="new-password" required minlength="6"
                     placeholder="At least 6 characters">
              <p class="help">Use at least 6 characters. Your password is stored securely hashed.</p>
            </div>

            <button type="submit" class="btn btn-primary btn-block">
              <svg class="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M12 5v14M5 12h14"/></svg>
              Create account
            </button>
          </form>

          <p class="mt-6 text-center text-sm text-ink-500">
            Already registered?
            <a class="font-semibold text-primary-700 hover:text-primary-800" href="<%= ctx %>/users/login">Sign in</a>
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
