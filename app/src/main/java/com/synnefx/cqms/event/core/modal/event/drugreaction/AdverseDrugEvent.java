package com.synnefx.cqms.event.core.modal.event.drugreaction;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.synnefx.cqms.event.core.modal.event.EventReport;
import com.synnefx.cqms.event.core.modal.event.PersonInvolved;
import com.synnefx.cqms.event.core.modal.event.ReportedBy;
import com.synnefx.cqms.event.sync.Syncable;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Josekutty on 2/9/2017.
 */

public class AdverseDrugEvent extends EventReport {


    @Expose
    private Calendar reactionDate;

    @Expose
    private String reactionDateStr;

    @Expose
    private Integer actionOutcomeCode;

    @Expose
    private DrugInfo suspectedDrug;

    @Expose
    private List<DrugInfo> otherDrugsTaken;

    @Expose
    private Calendar dateOfRecovery;

    @Expose
    private String dateOfRecoveryStr;

    @Expose
    private Calendar dateOfDeath;

    @Expose
    private String dateOfDeathStr;

    @Expose
    private boolean admittedPostReaction;

    @Expose
    private boolean reactionAddedToCasesheet;

    @Expose
    private String additionalInfo;


    public AdverseDrugEvent() {

    }

    public AdverseDrugEvent(Long id, Long serverId, String hospital, int statusCode, Long unitRef, String incidentNumber, String incidentLocation, Calendar updated, Calendar incidentTime,Calendar reactionDate, String description, String correctiveActionTaken, PersonInvolved personInvolved, ReportedBy reportedBy, Integer actionOutcomeCode, DrugInfo suspectedDrug, List<DrugInfo> otherDrugsTaken, Calendar dateOfRecovery, Calendar dateOfDeath, boolean admittedPostReaction, boolean reactionAddedToCasesheet, String additionalInfo,String reactionDateStr,String dateOfRecoveryStr,String dateOfDeathStr) {
        super(id, serverId, hospital, statusCode, unitRef, incidentNumber, incidentLocation, updated, incidentTime, description, correctiveActionTaken, personInvolved, reportedBy);
        this.reactionDate = reactionDate;
        this.actionOutcomeCode = actionOutcomeCode;
        this.suspectedDrug = suspectedDrug;
        this.otherDrugsTaken = otherDrugsTaken;
        this.dateOfRecovery = dateOfRecovery;
        this.dateOfDeath = dateOfDeath;
        this.admittedPostReaction = admittedPostReaction;
        this.reactionAddedToCasesheet = reactionAddedToCasesheet;
        this.additionalInfo = additionalInfo;
        this.reactionDateStr = reactionDateStr;
        this.dateOfDeathStr = dateOfDeathStr;
        this.dateOfRecoveryStr = dateOfRecoveryStr;
    }

    // copy
    public AdverseDrugEvent copy() {
        AdverseDrugEvent result = new AdverseDrugEvent(this.getId(), this.getServerId(), this.getHospital(), this.getStatusCode(), this.getUnitRef(), this.getIncidentNumber(), this.getIncidentLocation(), this.getUpdated(), this.getIncidentTime(),this.getReactionDate(), this.getDescription(), this.getCorrectiveActionTaken(), this.getPersonInvolved(), this.getReportedBy(), this.actionOutcomeCode, this.suspectedDrug, this.otherDrugsTaken, this.dateOfRecovery, this.dateOfDeath, this.admittedPostReaction, this.reactionAddedToCasesheet, this.additionalInfo,this.reactionDateStr,this.dateOfRecoveryStr,this.dateOfDeathStr);
        return result;
    }

    @Override
    public void mapFromRemote(Syncable remote) {
        super.mapFromRemote(remote);
        AdverseDrugEvent remoteReport = (AdverseDrugEvent) remote;
        setReactionDate(remoteReport.getReactionDate());
        setActionOutcomeCode(remoteReport.getActionOutcomeCode());
        setAdmittedPostReaction(remoteReport.isAdmittedPostReaction());
        setAdditionalInfo(remoteReport.getAdditionalInfo());
        setDateOfRecovery(remoteReport.getDateOfRecovery());
        setDateOfDeath(remoteReport.getDateOfDeath());
        setSuspectedDrug(remoteReport.getSuspectedDrug());
        setOtherDrugsTaken(remoteReport.getOtherDrugsTaken());
        setReactionAddedToCasesheet(remoteReport.isReactionAddedToCasesheet());
    }

    @Override
    public void mapFromLocal(Syncable local) {
        super.mapFromLocal(local);
        AdverseDrugEvent localReport = (AdverseDrugEvent) local;
        Log.e("AdverseDrugEvent", "mapFromLocal" + localReport);
        setReactionDate(localReport.getReactionDate());
        setActionOutcomeCode(localReport.getActionOutcomeCode());
        setAdmittedPostReaction(localReport.isAdmittedPostReaction());
        setAdditionalInfo(localReport.getAdditionalInfo());
        setDateOfRecovery(localReport.getDateOfRecovery());
        setDateOfDeath(localReport.getDateOfDeath());
        setSuspectedDrug(localReport.getSuspectedDrug());
        setOtherDrugsTaken(localReport.getOtherDrugsTaken());
        setReactionAddedToCasesheet(localReport.isReactionAddedToCasesheet());

    }


    public Calendar getReactionDate() {
        return reactionDate;
    }

    public void setReactionDate(Calendar reactionDate) {
        this.reactionDate = reactionDate;
    }

    public String getReactionDateStr() {
        return reactionDateStr;
    }

    public void setReactionDateStr(String reactionDateStr) {
        this.reactionDateStr = reactionDateStr;
    }

    public Integer getActionOutcomeCode() {
        return actionOutcomeCode;
    }

    public void setActionOutcomeCode(Integer actionOutcomeCode) {
        this.actionOutcomeCode = actionOutcomeCode;
    }

    public DrugInfo getSuspectedDrug() {
        return suspectedDrug;
    }

    public void setSuspectedDrug(DrugInfo suspectedDrug) {
        this.suspectedDrug = suspectedDrug;
    }

    public List<DrugInfo> getOtherDrugsTaken() {
        return otherDrugsTaken;
    }

    public void setOtherDrugsTaken(List<DrugInfo> otherDrugsTaken) {
        this.otherDrugsTaken = otherDrugsTaken;
    }

    public Calendar getDateOfRecovery() {
        return dateOfRecovery;
    }

    public void setDateOfRecovery(Calendar dateOfRecovery) {
        this.dateOfRecovery = dateOfRecovery;
    }

    public String getDateOfRecoveryStr() {
        return dateOfRecoveryStr;
    }

    public void setDateOfRecoveryStr(String dateOfRecoveryStr) {
        this.dateOfRecoveryStr = dateOfRecoveryStr;
    }

    public Calendar getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Calendar dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getDateOfDeathStr() {
        return dateOfDeathStr;
    }

    public void setDateOfDeathStr(String dateOfDeathStr) {
        this.dateOfDeathStr = dateOfDeathStr;
    }

    public boolean isAdmittedPostReaction() {
        return admittedPostReaction;
    }

    public void setAdmittedPostReaction(boolean admittedPostReaction) {
        this.admittedPostReaction = admittedPostReaction;
    }

    public boolean isReactionAddedToCasesheet() {
        return reactionAddedToCasesheet;
    }

    public void setReactionAddedToCasesheet(boolean reactionAddedToCasesheet) {
        this.reactionAddedToCasesheet = reactionAddedToCasesheet;
    }


    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
