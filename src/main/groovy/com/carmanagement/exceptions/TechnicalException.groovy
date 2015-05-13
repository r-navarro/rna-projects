package com.carmanagement.exceptions

class TechnicalException extends RuntimeException {
    ErrorCode errorCode
    String errorParameter


    public String getMessage() {
        switch (errorCode) {
            case ErrorCode.VEHICLE_NOT_FOUND:
                return "Vehicle not found with id : ${errorParameter}"
            case ErrorCode.USER_NOT_FOUND:
                return "User not found with id : ${errorParameter}"
        }
    }
}

public enum ErrorCode {
    VEHICLE_NOT_FOUND, USER_NOT_FOUND
}