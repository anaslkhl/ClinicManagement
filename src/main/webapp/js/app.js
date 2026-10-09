/* Clinic Manager - progressive enhancement only.
   No framework, no backend coupling. Everything degrades gracefully
   when JavaScript is unavailable (plain HTML forms still work). */
(function () {
  'use strict';

  document.addEventListener('DOMContentLoaded', function () {
    initSidebar();
    initConfirm();
    initSubmitGuard();
    initDeleteButtons();
    initAutoDismiss();
  });

  /* ---------------------------------------------------------------- sidebar */
  function initSidebar() {
    var toggle = document.getElementById('sidebar-toggle');
    var sidebar = document.getElementById('sidebar');
    var backdrop = document.getElementById('sidebar-backdrop');
    if (!toggle || !sidebar) return;

    function setOpen(open) {
      sidebar.classList.toggle('-translate-x-full', !open);
      if (backdrop) backdrop.classList.toggle('hidden', !open);
      toggle.setAttribute('aria-expanded', String(open));
      document.body.classList.toggle('overflow-hidden', open && isMobile());
    }

    function isMobile() {
      return window.matchMedia('(max-width: 1023px)').matches;
    }

    toggle.addEventListener('click', function () {
      var isOpen = !sidebar.classList.contains('-translate-x-full');
      setOpen(!isOpen);
    });

    if (backdrop) {
      backdrop.addEventListener('click', function () { setOpen(false); });
    }

    sidebar.querySelectorAll('a').forEach(function (link) {
      link.addEventListener('click', function () {
        if (isMobile()) setOpen(false);
      });
    });

    document.addEventListener('keydown', function (event) {
      if (event.key === 'Escape') setOpen(false);
    });

    window.addEventListener('resize', function () {
      if (!isMobile()) {
        sidebar.classList.remove('-translate-x-full');
        document.body.classList.remove('overflow-hidden');
        if (backdrop) backdrop.classList.add('hidden');
      }
    });
  }

  /* ---------------------------------------------------------------- confirm */
  var confirmDialog, confirmMessage, pendingAction = null;

  function initConfirm() {
    confirmDialog = document.getElementById('confirm-dialog');
    confirmMessage = document.getElementById('confirm-message');
    if (!confirmDialog) return;

    var ok = document.getElementById('confirm-ok');
    var cancel = document.getElementById('confirm-cancel');
    if (ok) {
      ok.addEventListener('click', function () {
        confirmDialog.close('confirm');
        var action = pendingAction;
        pendingAction = null;
        if (action) action();
      });
    }
    if (cancel) {
      cancel.addEventListener('click', function () {
        pendingAction = null;
        confirmDialog.close('cancel');
      });
    }
  }

  function askConfirm(message, action) {
    if (confirmDialog && typeof confirmDialog.showModal === 'function') {
      pendingAction = action;
      if (confirmMessage) confirmMessage.textContent = message;
      confirmDialog.showModal();
    } else if (window.confirm(message)) {
      action();
    }
  }

  /* --------------------------------------------------------------- requests */
  function request(url, options) {
    var opts = options || {};
    var headers = new Headers(opts.headers || {});
    headers.set('X-Requested-With', 'XMLHttpRequest');
    opts.headers = headers;
    return fetch(url, opts);
  }

  /* ---------------------------------------------------------- submit guard  */
  function initSubmitGuard() {
    document.addEventListener('submit', function (event) {
      var form = event.target;
      if (!(form instanceof HTMLFormElement)) return;

      if (form.hasAttribute('data-confirm') && form.dataset.confirmed !== 'true') {
        event.preventDefault();
        askConfirm(form.getAttribute('data-confirm'), function () {
          form.dataset.confirmed = 'true';
          form.submit();
        });
        return;
      }

      if (form.dataset.submitting === 'true') {
        event.preventDefault();
        return;
      }
      form.dataset.submitting = 'true';

      var submitter = form.querySelector('[type="submit"]');
      if (submitter) {
        submitter.disabled = true;
        submitter.setAttribute('aria-disabled', 'true');
      }
    });
  }

  /* -------------------------------------------------------- delete buttons  */
  function initDeleteButtons() {
    document.addEventListener('click', function (event) {
      var button = event.target.closest('[data-delete-url]');
      if (!button) return;
      event.preventDefault();

      var message = button.getAttribute('data-confirm') || 'Delete this item? This cannot be undone.';
      askConfirm(message, function () {
        button.disabled = true;
        request(button.getAttribute('data-delete-url'), { method: 'DELETE' })
          .then(function (response) {
            if (response.ok || response.status === 204) {
              toast(button.getAttribute('data-success') || 'Deleted successfully.', 'success');
              var redirect = button.getAttribute('data-redirect');
              window.setTimeout(function () {
                if (redirect) window.location.assign(redirect);
                else window.location.reload();
              }, 400);
            } else {
              button.disabled = false;
              toast('The item could not be deleted.', 'danger');
            }
          })
          .catch(function () {
            button.disabled = false;
            toast('Network error. Please try again.', 'danger');
          });
      });
    });
  }

  /* ------------------------------------------------------------------ toast */
  function initAutoDismiss() {
    document.querySelectorAll('[data-auto-dismiss]').forEach(function (element) {
      window.setTimeout(function () { element.remove(); }, Number(element.getAttribute('data-auto-dismiss')) || 5000);
    });
  }

  function toast(message, variant) {
    var region = document.getElementById('toast-region');
    if (!region) return;
    var variants = {
      success: 'alert-success',
      warning: 'alert-warning',
      info: 'alert-info',
      danger: 'alert-danger'
    };
    var element = document.createElement('div');
    element.className = 'alert shadow-card animate-toast-in ' + (variants[variant] || variants.info);
    element.setAttribute('role', variant === 'danger' ? 'alert' : 'status');
    element.textContent = message;
    region.appendChild(element);
    window.setTimeout(function () { element.remove(); }, 4500);
  }
})();
