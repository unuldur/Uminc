package com.unuldur.uminc.model;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;

import com.unuldur.uminc.R;
import com.unuldur.uminc.connection.Connection;
import com.unuldur.uminc.connection.IConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Unuldur on 06/12/2017.
 */

public class DbufrConnection implements IDbufrConnection {
    private IConnection connection;
    private Context context;
    public DbufrConnection(Context context) {
        connection = new Connection(context);
        this.context = context;
    }

    public DbufrConnection(IConnection connection, Context context) {
        this.connection = connection;
        this.context = context;
    }

    @Override
    public IEtudiant connect(String numEtu, String mdp) {
        final String adress = "https://www-dbufr.ufr-info-p6.jussieu.fr/lmd/2004/master/auths/seeStudentMarks.php";
        String dbufrHtmlStr = connection.getPage(adress, numEtu, mdp);
        if(dbufrHtmlStr == null) return null;
        Document doc = Jsoup.parse(dbufrHtmlStr);
        Element body = doc.body();
        Elements tables = body.getElementsByClass("Table");
        return new Etudiant(numEtu,mdp, getUEs(tables.get(0)));
    }


    private List<UE> getUEs(Element ues){
        List<UE> uesL = new ArrayList<>();
        Elements rows = ues.getElementsByTag("tr");
        for (Element row: rows){
            Elements cols = row.getElementsByTag("td");
            if(cols.size() == 0){
                continue;
            }
            TypedArray ta = context.getResources().obtainTypedArray(R.array.colors_calendars);
            int[] colors = new int[ta.length()];
            for (int i = 0; i < ta.length(); i++) {
                colors[i] = ta.getColor(i, 0);
            }
            ta.recycle();
            Random random = new Random();
            uesL.add(new UE(cols.get(1).text(),cols.get(3).text(),cols.get(0).text(), colors[random.nextInt(colors.length)]));
        }
        return uesL;
    }
}
