package com.example.logsearch.entities;

public enum CorrectionCheckResult {
    ERROR1(1, "DateFrom exceeds DateTo", "Нижняя граница превосходит верхнюю"),
    ERROR18(18, "DateFrom exceeds PresentTime", "Некорректное значение нижней границы"),
    ERROR19(19, "Incorrect time format", "Недопустимый формат временного интервала"),
    ERROR37(37, "Missed mandatory parameter", "Не все обязательные поля заполнены"),
    ERROR44(44, "Incorrect resource name", "Неверно указано расположение логов"),
    ERROR3701(3701, "Missed async method file extension", "Не указано расширение файла для асинхронного метода");

    private long errorCode;
    private String errorMessage;
    private String errorComment;

    CorrectionCheckResult(long errorCode, String errorMessage, String errorComment) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorComment = errorComment;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorComment() {
        return errorComment;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorComment(String errorComment) {
        this.errorComment = errorComment;
    }
}
