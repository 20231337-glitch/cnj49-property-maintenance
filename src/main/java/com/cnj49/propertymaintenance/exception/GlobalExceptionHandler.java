package com.cnj49.propertymaintenance.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Xu ly loi tap trung: khong bao gio de trang Whitelabel Error Page xuat hien khi demo.
 * ResourceNotFoundException -> 404, BusinessException -> hien loi than thien tren trang hien tai,
 * cac loi khac -> trang 500.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandler(Model model) {
        model.addAttribute("message", "Không tìm thấy trang yêu cầu.");
        return "errors/404";
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessError(BusinessException ex, HttpServletRequest request, Model model) {
        log.warn("Business rule violation at {}: {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("backUrl", request.getHeader("Referer"));
        return "errors/business-error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnexpected(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("message", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
        return "errors/500";
    }
}
