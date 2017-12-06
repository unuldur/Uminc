package com.unuldur.uminc;

import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.unuldur.uminc.model.IEvent;

import java.text.SimpleDateFormat;

/**
 * Created by julie on 06/12/2017.
 */

public class PopupEvent extends Dialog implements View.OnClickListener {
    private IEvent event;

    public PopupEvent(@NonNull Context context, IEvent event) {
        super(context);
        this.event = event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_event);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        TextView loc = findViewById(R.id.popup_localisation_tv);
        loc.setText("Localisation: " + event.getLocalisation());
        TextView deb = findViewById(R.id.popup_date_debut_tv);
        deb.setText("Debut: " + sdf.format(event.getStartDate().getTime()));
        TextView fin = findViewById(R.id.popup_date_fin_tv);
        fin.setText("Fin: " + sdf.format(event.getEndDate().getTime()));
        Button b = findViewById(R.id.ok_popup_button);
        TextView title = findViewById(R.id.popup_title);
        title.setText(event.getTitre());
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
