package com.synnefx.cqms.event.ui.drugreaction;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.synnefx.cqms.event.BootstrapApplication;
import com.synnefx.cqms.event.R;
import com.synnefx.cqms.event.core.Constants;
import com.synnefx.cqms.event.core.modal.event.PersonInvolved;
import com.synnefx.cqms.event.core.modal.event.drugreaction.AdverseDrugEvent;
import com.synnefx.cqms.event.sqlite.DatabaseHelper;
import com.synnefx.cqms.event.util.CalenderUtils;
import com.synnefx.cqms.event.util.ListViewer;
import com.synnefx.cqms.event.util.PrefUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.synnefx.cqms.event.core.Constants.Extra.INCIDENT_ITEM;

/**
 * Created by hsrii on 10/4/2017.
 */

public class PatientDetailsFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener{

    protected View fragmentView;
    private DrugReactionActivity mActivity;

    @Bind(R.id.incident_details_save)
    protected Button saveDetailsBtn;

    @Bind(R.id.patient_involved_name)
    protected MaterialEditText patientName;

    @Bind(R.id.patient_type_ip)
    protected RadioButton ipPatient;
    @Bind(R.id.patient_type_op)
    protected RadioButton opPatient;

    @Bind(R.id.patient_number)
    protected MaterialEditText patientNumber;

    @Bind(R.id.patient_gender)
    protected MaterialBetterSpinner personGender;

    @Bind(R.id.patient_height)
    protected MaterialEditText patientHeight;

    @Bind(R.id.patient_weight)
    protected MaterialEditText patientWeight;

    @Bind(R.id.patient_dob_btn)
    protected Button patientDOBBtn;
    @Bind(R.id.patient_dob)
    protected MaterialEditText patientDOB;


    @Inject
    protected DatabaseHelper databaseHelper;
    @Inject
    EventBus eventBus;

    private AdverseDrugEvent report;
    private PersonInvolved patient;

    ArrayAdapter<CharSequence> gendorAdapter;


    public PatientDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_reaction_patient_details, container, false);
        ButterKnife.bind(this, fragmentView);
        eventBus.register(this);
        patient = new PersonInvolved();
        report = new AdverseDrugEvent();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            report = (AdverseDrugEvent) bundle.getSerializable(INCIDENT_ITEM);
            if (null == report) {
                Long reportRef = bundle.getLong(Constants.Extra.INCIDENT_REF, 0l);
                Log.e("reportRef ", String.valueOf(reportRef));
                if (0 < reportRef) {
                    report = databaseHelper.getAdverseDrugEventById(reportRef);
                }
            }
            if(null != report){
                Long personInvolvedRef = report.getPersonInvolvedRef();
                if(null != personInvolvedRef && 0 < personInvolvedRef){
                    patient = databaseHelper.getPersonInvolvedById(personInvolvedRef);
                }
            }
            if (null == patient && null != report.getId() && 0 < report.getId()) {
                Long patientRef = bundle.getLong(Constants.Extra.PATIENT_REF, 0l);
                Log.e("patientRef ", String.valueOf(patientRef));
                if (0 < patientRef) {
                    patient = databaseHelper.getPersonInvolvedById(patientRef);
                }
            }
            report.setPersonInvolved(patient);
        }
        initScreen();
        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEvent();
                eventBus.post(getString(R.string.save_btn_clicked));
            }
        });
        return fragmentView;
    }


    @Override
    public void onDestroyView() {
        eventBus.unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (DrugReactionActivity) context;
    }

    private void initScreen() {

        ipPatient.setChecked(Boolean.TRUE);
        opPatient.setChecked(Boolean.FALSE);
        if (null != patient && null != patient.getId() && 0 < patient.getId()) {
            patientName.setText(patient.getName());
            patientNumber.setText(patient.getHospitalNumber());
            if(null != patient.getPatientTypeCode()){
                if(1 == patient.getPatientTypeCode()){
                    ipPatient.setChecked(Boolean.TRUE);
                    opPatient.setChecked(Boolean.FALSE);
                } else{
                    ipPatient.setChecked(Boolean.FALSE);
                    opPatient.setChecked(Boolean.TRUE);
                }
            }
            if(patient.getHeight()>0)
                patientHeight.setText(patient.getHeight().toString());
            if(patient.getWeight()>0)
                patientWeight.setText(patient.getWeight().toString());

            if (null != patient.getDateOfBirthIndividual()) {
                patientDOBBtn.setText("Change");
                patientDOB.setText(CalenderUtils.formatCalendarToString(patient.getDateOfBirthIndividual(), Constants.Common.DATE_DISPLAY_FORMAT));
            } else {
                patientDOBBtn.setText("Set");
            }
            saveDetailsBtn.setText("Update");
        } else {
            report.setIncidentTime(null);
            report.setStatusCode(0);
            patientDOBBtn.setText("Set");
        }
        setPatientGenderSpinner();
        initDOBDatepicker();
    }

    private void setPatientGenderSpinner(){
        gendorAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.genders, android.R.layout.simple_dropdown_item_1line);
        // Specify the layout to use when the list of choices appears
        //personTypeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        personGender.setAdapter(gendorAdapter);
        personGender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    CharSequence selectedItem = gendorAdapter.getItem(position);
                    if (null != selectedItem) {
                        patient.setGenderCode(position);
                    }
                }else{
                    patient.setGenderCode(0);
                }
            }
        });
        if(null != patient){
            Integer selectedGender = patient.getGenderCode();
            if (null != selectedGender && 0 < selectedGender) {
                personGender.setText(gendorAdapter.getItem(selectedGender));
            }
        }
    }



    private void initDOBDatepicker() {
        patientDOBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDOBpicker();
            }
        });

        patientDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDOBpicker();
            }
        });
    }

    private void openDOBpicker(){
        Calendar date = Calendar.getInstance();
        if (null != report && null != report.getIncidentTime()) {
            date = report.getIncidentTime();
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                PatientDetailsFragment.this,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.vibrate(true);
        dpd.dismissOnPause(true);
        dpd.showYearPickerFirst(true);
        // dpd.setAccentColor(Color.parseColor("#9C27B0"));
        dpd.setTitle("Select Date of Birth");
        //Setting max date
        dpd.setMaxDate(Calendar.getInstance());
        dpd.show(getActivity().getFragmentManager(), "DOBDatepickerdialog");
    }


    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog eventTimeDpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("EventDatepickerdialog");
        if (eventTimeDpd != null) eventTimeDpd.setOnDateSetListener(this);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, monthOfYear, dayOfMonth);
        if ("DOBDatepickerdialog".equals(view.getTag())) {
            patient.setDateOfBirthIndividual(selectedDate);
            patientDOB.setText(CalenderUtils.formatCalendarToString(patient.getDateOfBirthIndividual(), Constants.Common.DATE_DISPLAY_FORMAT));
        }
    }

    public void saveEvent() {
        if (saveIncidentDetails()) {
            Snackbar.make(getActivity().findViewById(R.id.footer_view), "Drug Reaction incident added", Snackbar.LENGTH_LONG).show();
            nextScreen();
        } else {
            Snackbar.make(getActivity().findViewById(R.id.footer_view), "Correct all validation errors", Snackbar.LENGTH_LONG).show();
        }
    }

   @Subscribe
    public void onEventListened(String data){
        if (data.equals(getString(R.string.save_draft))){
            if(saveDraft() != null){
                long id = databaseHelper.insertOrUpdateAdverseDrugReaction(saveDraft());
                report.setId(id);
                if (0 < id){
                    databaseHelper.updateAdverseDrugEventPersonInvolved(saveDraft());
                }

            }else{
                ((DeleteReportListener)mActivity).onDelete();
            }
        }
    }


    private AdverseDrugEvent saveDraft(){
        String hospitalRef = PrefUtils.getFromPrefs(getActivity().getApplicationContext(), PrefUtils.PREFS_HOSP_ID, null);
        report.setHospital(hospitalRef);

        if (report.getUnitRef()==null){
            report.setUnitRef(0L);
        }

        if (TextUtils.isEmpty(report.getDescription())
                && (report.getUnitRef() <= 0L || report.getUnitRef() == null )
                && TextUtils.isEmpty(patientName.getText())){
            return null;
        }

        if (0 == report.getStatusCode()) {
            report.setCreatedOn(Calendar.getInstance());
        }
        report.setUpdated(Calendar.getInstance());
        patient.setPersonnelTypeCode(1);


        patient.setName(patientName.getText().toString().trim());
        patient.setHospitalNumber(patientNumber.getText().toString().trim());

        if (ipPatient.isChecked()) {
            patient.setPatientTypeCode(1);
        } else if (opPatient.isChecked()) {
            patient.setPatientTypeCode(2);
        }

        if (!TextUtils.isEmpty(patientHeight.getText().toString())){
            patient.setHeight(Double.valueOf(patientHeight.getText().toString()));
        }
        if (!TextUtils.isEmpty(patientWeight.getText().toString())){
            patient.setWeight(Double.valueOf(patientWeight.getText().toString()));
        }

        report.setPersonInvolved(patient);

        Log.d("PatientDetailsFragment", ListViewer.view(report));
        return report;

    }

    private boolean saveIncidentDetails() {
        if (!validatePatientDeatils()) {
            String hospitalRef = PrefUtils.getFromPrefs(getActivity().getApplicationContext(), PrefUtils.PREFS_HOSP_ID, null);
            report.setHospital(hospitalRef);
            if (0 == report.getStatusCode()) {
                report.setCreatedOn(Calendar.getInstance());
            }
            report.setUpdated(Calendar.getInstance());
            patient.setPersonnelTypeCode(1);
            report.setPersonInvolved(patient);
            long reportId = 0;
            if(null != report && (null == report.getId() || 0 >= report.getId())){
                if (report.getUnitRef()==null){
                    report.setUnitRef(0l);
                }
                reportId = databaseHelper.insertAdverseDrugReaction(report);
                report.setId(reportId);
            }else{
                reportId = report.getId();
            }
            if (0 < reportId) {
                report.getPersonInvolved().setEventRef(reportId);
                long id = databaseHelper.updateAdverseDrugEventPersonInvolved(report);
                if (0 < id) {
                    Timber.e("saveIncidentDetails " + id);
                    report.getPersonInvolved().setId(id);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validatePatientDeatils() {
        boolean error = false;
        if (TextUtils.isEmpty(patientName.getText())) {
            patientName.setError("Patient name required");
            patientName.requestFocus();
            error = true;
        } else {
            patient.setName(patientName.getText().toString().trim());
        }

        if (TextUtils.isEmpty(patientNumber.getText())) {
            patientNumber.setError("Patient hospital number required");
            patientNumber.requestFocus();
            error = true;
        } else {
            patient.setHospitalNumber(patientNumber.getText().toString().trim());
        }

        if (patient.getGenderCode() == null || patient.getGenderCode()==0){
            personGender.setError("Patient gender required");
            personGender.requestFocus();
            error = true;
        }

        if (ipPatient.isChecked()) {
            patient.setPatientTypeCode(1);
        } else if (opPatient.isChecked()) {
            patient.setPatientTypeCode(2);
        } else {
            ipPatient.setError("Patient type required");
            ipPatient.requestFocus();
            error = true;
        }
        if(null == patient.getDateOfBirthIndividual()){
            patientDOB.setError("Patient DOB required");
            error = true;
        }
        if(!TextUtils.isEmpty(patientHeight.getText())){
            Double height = Double.valueOf(patientHeight.getText().toString());
            if(height.compareTo(Double.valueOf(200.0)) > 0 || height.compareTo(Double.valueOf(0.0)) <= 0 ){
                patientHeight.setError("Invalid height value. (Height in cm.)");
                error = true;
            }else {
                patient.setHeight(height);
            }
        }else {
            patientHeight.setError("Invalid height value. (Height in cm.)");
            error = true;
            patientHeight.requestFocus();
        }
        if(!TextUtils.isEmpty(patientWeight.getText())){
            Double weight = Double.valueOf(patientWeight.getText().toString());
            if(weight.compareTo(Double.valueOf(300.0)) > 0 || weight.compareTo(Double.valueOf(0.0)) <= 0 ){
                patientWeight.setError("Invalid weight value. (Weight in kg.)");
                error = true;
            }else {
                patient.setWeight(weight);
            }
        }else {
            patientWeight.setError("Invalid weight value. (Weight in kg.)");
            error = true;
            patientWeight.requestFocus();
        }
        return error;
    }

    private void nextScreen() {
        DrugReactionDiagnosisDetailsFragment diagnosisDetailsFragment = new DrugReactionDiagnosisDetailsFragment();
        if (null != report) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.Extra.INCIDENT_ITEM, report);
            diagnosisDetailsFragment.setArguments(bundle);
        }
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.incident_report_form_container, diagnosisDetailsFragment)
                .commit();
    }

    /**
     * Used to hide the soft input n fragment start
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public interface DeleteReportListener{
        void onDelete();
    }
}
