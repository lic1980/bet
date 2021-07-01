package com.lottery.model;

public enum CustomerBidResult {

	PENDING, WIN, LOSE ;
	
	public static CustomerBidResult fromValue(String v) {
        for (CustomerBidResult status : values()) {
            if (status.toString().equals(v)) {
                return status;
            }
        }
        return CustomerBidResult.PENDING;
    }
	
}
	