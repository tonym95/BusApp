package com.antonio.busapplication.Model;

import android.location.Location;

import java.util.List;

/**
 * Created by Antonio on 5/28/2017.
 */

public class AndCriteria implements Criteria {
    private Criteria criteria;
    private Criteria otherCriteria;

    public AndCriteria(Criteria criteria, Criteria otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<Bus> meetCriteria(List<Bus> buses, Location proximityLocation) {

        List<Bus> firstCriteriaPersons = criteria.meetCriteria(buses, proximityLocation);
        return otherCriteria.meetCriteria(firstCriteriaPersons, proximityLocation);
    }
}
