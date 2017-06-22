package com.synnefx.cqms.event.ui.drugreaction;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.synnefx.cqms.event.R;
import com.synnefx.cqms.event.core.Constants;
import com.synnefx.cqms.event.core.modal.event.drugreaction.AdverseDrugEvent;
import com.synnefx.cqms.event.ui.base.AlternatingColorListAdapter;
import com.synnefx.cqms.event.util.CalenderUtils;

import java.util.List;

/**
 * Created by Josekutty on 2/9/2017.
 */
public class DrugReactionListAdapter extends AlternatingColorListAdapter<AdverseDrugEvent> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public DrugReactionListAdapter(final LayoutInflater inflater, final List<AdverseDrugEvent> items,
                                   final boolean selectable) {
        super(R.layout.incident_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     * @param items
     */
    public DrugReactionListAdapter(final LayoutInflater inflater, final List<AdverseDrugEvent> items) {
        super(R.layout.incident_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.incident_type, R.id.event_description, R.id.event_unit,
                R.id.reported_by, R.id.event_status, R.id.event_time};
    }

    @Override
    protected void update(final int position, final AdverseDrugEvent item) {
        //super.update(position, item);
       // setText(0, item.getIncidentTypeName());
        String description = item.getDescription();
        if (!TextUtils.isEmpty(description) && description.length() > 250) {
            description = String.format("%s..", description.substring(0, 249));
        }
        setText(1, description);
        setText(2, item.getDepartment());
        if (null != item.getReportedBy()) {
            setText(3, item.getReportedBy().getLastName());
        } else {
            textView(3).setVisibility(View.GONE);
        }
        if (null != item.getIncidentTime()) {
            setText(5, "On : " + CalenderUtils.formatCalendarToString(item.getIncidentTime(), Constants.Common.DATE_DISPLAY_FORMAT));
        }

        if (0 == item.getStatusCode()) {
            imageView(4).setBackground(ContextCompat.getDrawable(imageView(4).getContext(), R.drawable.primary_status_icon_background));
            imageView(4).setImageResource(R.drawable.ic_edit_white_24dp);
        } else if (1 == item.getStatusCode()) {
            imageView(4).setBackground(ContextCompat.getDrawable(imageView(4).getContext(), R.drawable.success_status_icon_background));
            imageView(4).setImageResource(R.drawable.ic_done_white_24dp);
        } else if (2 == item.getStatusCode()) {
            imageView(4).setBackground(ContextCompat.getDrawable(imageView(4).getContext(), R.drawable.reviewed_status_icon_background));
            imageView(4).setImageResource(R.drawable.ic_done_white_24dp);
        }
    }

    public interface CustomButtonListener {
        public void onButtonClickListner(int position, AdverseDrugEvent value);
    }

    private CustomButtonListener customListner;

    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customListner = listener;
    }
}