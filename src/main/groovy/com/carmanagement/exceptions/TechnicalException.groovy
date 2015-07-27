package com.carmanagement.exceptions

class TechnicalException extends RuntimeException {
    ErrorCode errorCode
    String errorParameter


    public String getMessage() {
        switch (errorCode) {
            case ErrorCode.VEHICLE_NOT_FOUND:
                return "Vehicle not found with id : {errorParameter"
            case ErrorCode.USER_NOT_FOUND:
                return "User not found with id : $errorParameter"
            case ErrorCode.FULL_TANK_NOT_FOUND:
                return "Full tank not found with id : $errorParameter"
            case ErrorCode.FULL_TANK_WRONG_FORMAT:
                return "Full tank is incorrectly formatted"
            case ErrorCode.FULL_TANK_VEHICLE_NOT_MATCH:
                return "Full tank does not belong to this vehicle : $errorParameter"
        }
    }
}

public enum ErrorCode {
    VEHICLE_NOT_FOUND, USER_NOT_FOUND, FULL_TANK_NOT_FOUND, FULL_TANK_WRONG_FORMAT, FULL_TANK_VEHICLE_NOT_MATCH
}