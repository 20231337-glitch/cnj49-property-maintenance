package com.cnj49.propertymaintenance.exception;

/**
 * Nem ra khi mot quy tac nghiep vu (BR01 - BR15) bi vi pham.
 * Vi du: tao WorkOrder khi chua co Quotation duoc duyet.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
