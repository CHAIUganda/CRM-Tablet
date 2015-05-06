package com.omnitech;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by victor on 2/12/15.
 */
public class SummaryReport {
    public static void createSummaryReport(Schema schema){
        Entity summaryReport = schema.addEntity("SummaryReport");
        summaryReport.addStringProperty("uuid").notNull().unique().primaryKey();
        summaryReport.addStringProperty("item");
        summaryReport.addStringProperty("week");
        summaryReport.addStringProperty("month");
        summaryReport.addStringProperty("teamAverageThisWeek");
        summaryReport.addStringProperty("teamAverageThisMonth");
    }
}
