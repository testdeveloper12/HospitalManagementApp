package com.synnefx.cqms.event.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;

import com.synnefx.cqms.event.BootstrapServiceProvider;
import com.synnefx.cqms.event.core.Constants;
import com.synnefx.cqms.event.util.SafeAsyncTask;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * Class used for logging a user out.
 */
public class LogoutServiceImpl implements LogoutService {

    protected final Context context;
    protected final AccountManager accountManager;

    protected final BootstrapServiceProvider serviceProvider;

    @Inject
    public LogoutServiceImpl(final Context context, final AccountManager accountManager, final BootstrapServiceProvider serviceProvider) {
        this.context = context;
        this.accountManager = accountManager;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void logout(final Runnable onSuccess) {
        new LogoutTask(context, onSuccess).execute();
    }

    private static class LogoutTask extends SafeAsyncTask<Boolean> {

        private final Context taskContext;
        private final Runnable onSuccess;

        protected LogoutTask(final Context context, final Runnable onSuccess) {
            this.taskContext = context;
            this.onSuccess = onSuccess;
        }

        @Override
        public Boolean call() throws Exception {

            final AccountManager accountManagerWithContext = AccountManager.get(taskContext);
            if (accountManagerWithContext != null) {
                final Account[] accounts = accountManagerWithContext
                        .getAccountsByType(Constants.Auth.BOOTSTRAP_ACCOUNT_TYPE);
                if (accounts.length > 0) {
                    //TODO before loging out, need to push all pending records
                    final AccountManagerFuture<Boolean> removeAccountFuture
                            = accountManagerWithContext.removeAccount(accounts[0], null, null);

                    return removeAccountFuture.getResult();
                }
            } else {
                Timber.w("accountManagerWithContext is null");
            }

            return false;
        }

        @Override
        protected void onSuccess(final Boolean accountWasRemoved) throws Exception {
            super.onSuccess(accountWasRemoved);

            Timber.d("Logout succeeded: %s", accountWasRemoved);
            onSuccess.run();

        }

        @Override
        protected void onException(final Exception e) throws RuntimeException {
            super.onException(e);

            Timber.e(e.getCause(), "Logout failed.");
        }
    }
}