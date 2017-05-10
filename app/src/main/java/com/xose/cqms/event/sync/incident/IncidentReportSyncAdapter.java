package com.xose.cqms.event.sync.incident;

import android.accounts.Account;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.xose.cqms.event.core.modal.event.incident.IncidentReport;
import com.xose.cqms.event.sync.SyncManager;
import com.xose.cqms.event.util.ConnectionUtils;
import com.xose.cqms.event.util.NotificationUtils;

import javax.inject.Inject;

import static com.xose.cqms.event.core.Constants.Notification.UPLOAD_NOTIFICATION_ID;


public class IncidentReportSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "IncidentSync";

    /**
     * URL to fetch content from during a sync.
     * <p/>
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds


    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    @Inject
    protected NotificationManager notificationManager;

    //  @Inject
    // protected Bus eventBus;

    @Inject
    protected Context mContext;

    @Inject
    protected IncidentReportSyncLocalDatastore auditSyncLocalDatastore;

    @Inject
    protected IncidentReportSyncRemoteDatastore auditSyncRemoteDatastore;


    public IncidentReportSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        Log.d(TAG, "DrugReactionSyncAdapter constructor... " + autoInitialize);
        mContentResolver = context.getContentResolver();
        // Register the bus so we can send notifications.
        //eventBus.register(this);

    }

    public IncidentReportSyncAdapter(Context context, boolean autoInitialize,
                                     boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
        mContentResolver = context.getContentResolver();
        // Register the bus so we can send notifications.
        //eventBus.register(this);
    }

    public IncidentReportSyncAdapter(Context context, NotificationManager notificationManager, IncidentReportSyncLocalDatastore localDatastore, IncidentReportSyncRemoteDatastore remoteDatastore) {
        super(context, true);
        mContext = context;
        Log.d(TAG, "DrugReactionSyncAdapter constructor... " + true);
        this.notificationManager = notificationManager;
        mContentResolver = context.getContentResolver();
        this.auditSyncLocalDatastore = localDatastore;
        this.auditSyncRemoteDatastore = remoteDatastore;
        // Register the bus so we can send notifications.
        // eventBus.register(this);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.e(TAG, "onPerformSync");
        try {
            if (ConnectionUtils.isInternetAvaialable(getContext())) {
                //TaskApi api = new GoogleTaskApi(getGoogleAuthToken());
                //TaskDb db = new TaskDb(mContext);
                //db.open();
                try {
                    updateNotification("Data sync in progress");
                    Log.e(TAG, "auditSyncLocalDatastore" + (null == auditSyncLocalDatastore));
                    Log.e(TAG, "auditSyncRemoteDatastore" + (null == auditSyncRemoteDatastore));
                    SyncManager<IncidentReport, IncidentReport> syncManager = new SyncManager<IncidentReport, IncidentReport>(auditSyncLocalDatastore, auditSyncRemoteDatastore);
                    syncManager.sync();
                    updateNotification("Data sync completed");
                } finally {
                    //db.close();
                }
            }
            //TODO What does the below code actually do???
            getContext().getContentResolver().notifyChange(IncidentReportSyncContentProvider.CONTENT_URI, null);
        } catch (Exception e) {
            Log.e(TAG, "syncFailed:", e);
            updateNotification("Data sync failed!");
        }
    }

    @Override
    public void onSyncCanceled() {
        //eventBus.unregister(this);
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);
        super.onSyncCanceled();
    }

    @Override
    public void onSyncCanceled(Thread thread) {
        //eventBus.unregister(this);
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);
        super.onSyncCanceled(thread);
    }

    private void updateNotification(String message) {
        if (null != notificationManager) {
            notificationManager.notify(UPLOAD_NOTIFICATION_ID, NotificationUtils.getNotification(getContext(), "CQMS : Data Sync", message));
        }
    }
}
