package com.lottery.model;

public enum CustomerBidStatus {

	NEW, ACCEPTED, CANCEL, SETTLED, REJECTED ;
	
	public static CustomerBidStatus fromValue(String v) {
        for (CustomerBidStatus status : values()) {
            if (status.toString().equals(v)) {
                return status;
            }
        }
        return CustomerBidStatus.NEW;
    }
	
}
	