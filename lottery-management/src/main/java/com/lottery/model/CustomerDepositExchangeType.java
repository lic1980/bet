package com.lottery.model;

public enum CustomerDepositExchangeType {
	DEPOIT, ENCHASHMENT, BID, WITHDRAW, AWARD, ACCEPT, MARGIN ;
	
	public static CustomerDepositExchangeType fromValue(String v) {
        for (CustomerDepositExchangeType status : values()) {
            if (status.toString().equals(v)) {
                return status;
            }
        }
        return CustomerDepositExchangeType.DEPOIT;
    }

	
}
