package com.pact.healthapp.components.appointment;

import java.io.Serializable;

/**
 * Created by zhangyl on 2016/1/7.
 */
public class ChoiceDateBean implements Serializable {


    /**
     * startDate : 02/27/2016 09:00
     * eventId : c3cafba2-6c09-4f09-920d-15dccd32441b
     * name : 排期时段
     * peopleAvailableNo : 51
     * endDate : 02/27/2016 12:00
     * daytimeId : 326287d03b1b40a58abf969c63cee0b9
     */

    private String startDate;
    private String eventId;
    private String name;
    private int peopleAvailableNo;
    private String endDate;
    private String daytimeId;

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeopleAvailableNo(int peopleAvailableNo) {
        this.peopleAvailableNo = peopleAvailableNo;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDaytimeId(String daytimeId) {
        this.daytimeId = daytimeId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public int getPeopleAvailableNo() {
        return peopleAvailableNo;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDaytimeId() {
        return daytimeId;
    }
}
