package com.mazzone.isere.transport.ui.model;

import org.joda.time.DateTime;

public class HourItem {
    private String mLineNumber;
    private String mLineName;
    private int operatorId;
    private String mJourneyDestination;
    private int mJourneyDirection;
    private DateTime mTheoreticalDepartureDateTime;

    public HourItem(String lineNumber, String lineName, int operatorId, String journeyDestination, int journeyDirection, DateTime theoreticalDepartureDateTime) {
        mLineNumber = lineNumber;
        mLineName = lineName;
        this.operatorId = operatorId;
        mJourneyDestination = journeyDestination;
        mJourneyDirection = journeyDirection;
        mTheoreticalDepartureDateTime = theoreticalDepartureDateTime;
    }

    public String getLineNumber() {
        return mLineNumber;
    }

    public String getLineName() {
        return mLineName;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public String getJourneyDestination() {
        return mJourneyDestination;
    }

    public int getJourneyDirection() {
        return mJourneyDirection;
    }

    public DateTime getTheoreticalDepartureDateTime() {
        return mTheoreticalDepartureDateTime;
    }
}
