package com.milan.codechangepresentationgenerator.shared.exception.exceptionhandler;

import com.milan.codechangepresentationgenerator.shared.exception.exceptionhandler.exception.*;
import com.milan.codechangepresentationgenerator.shared.message.MessageConstants;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class GlobalHttpErrorHandlerException {
    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<Object> invalidUserIdExceptionHandler(InvalidUserIdException e) {
        log.error("InvalidUserIdException: " + e.getMessage());
        return new ResponseEntity<>("Please input correct email amd password", NOT_FOUND);
    }

    @ExceptionHandler(NotEmptyEmailException.class)
    public ResponseEntity<Object> notEmptyEmailHandler(NotEmptyEmailException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>("Given Email still have found. Please Try with another email", BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Object> emailAlreadyExistHandler(EmailAlreadyExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Email already registered...", CONFLICT);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Object> userAlreadyExistHandler(UserAlreadyRegisteredException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("User already registered...", CONFLICT);
    }

    @ExceptionHandler(AdminAlreadyRegisteredException.class)
    public ResponseEntity<Object> userAlreadyExistHandler(AdminAlreadyRegisteredException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(" already registered...", CONFLICT);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> emailNotFoundHandler(EmailNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Email with given ID doest not exist", NOT_FOUND);
    }

    @ExceptionHandler(InvalidUserPasswordFoundException.class)
    public ResponseEntity<Object> emailNotFoundHandler(InvalidUserPasswordFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("InvalidUserPasswordFoundException: " + "Invalid entered Password. Your Password is not Matched", BAD_REQUEST);
    }


    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<Object> invalidUsernameHandler(InvalidUsernameException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Invalid username", NOT_FOUND);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFoundHandler(UsernameNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Username not found.", NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmailPasswordFoundException.class)
    public ResponseEntity<Object> invalidEmailAndPasswordFoundHandler(InvalidEmailPasswordFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Invalid Username and Password. Please enter correct password", BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> expiredJwtException(ExpiredJwtException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Expired token", UNAUTHORIZED);
    }

    //    Brands

    @ExceptionHandler(BrandAlreadyExistException.class)
    public ResponseEntity<Object> brandAlreadyExistHandler(BrandAlreadyExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(MessageConstants.BRAND_ALREADY_EXISTED_BY_NAME, CONFLICT);
    }

    @ExceptionHandler(BrandAlreadyExistByIdException.class)
    public ResponseEntity<Object> brandAlreadyExistByIdExceptionHandler(BrandAlreadyExistByIdException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("Brand Already Exists", CONFLICT);
    }


    @ExceptionHandler(BrandNotExistByIdException.class)
    public ResponseEntity<Object> brandNotExistByIdException(BrandNotExistByIdException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("Brand Not Found", BAD_REQUEST);
    }


    //    Cart
    @ExceptionHandler(CartNotExistByNameException.class)
    public ResponseEntity<Object> cartNotExistByNameException(CartNotExistByNameException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("cartNotExistByNameException: " + "Cart Already Does not Exist", CONFLICT);
    }

    @ExceptionHandler(CartNotExistByIdException.class)
    public ResponseEntity<Object> cartNotExistByIdException(CartNotExistByIdException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("Cart Not Found", BAD_REQUEST);
    }

    @ExceptionHandler(CartAlreadyExistException.class)
    public ResponseEntity<Object> cartAlreadyExistHandler(CartAlreadyExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Cart already exists...", CONFLICT);
    }


    //    Product
    @ExceptionHandler(ProductNotExistByNameException.class)
    public ResponseEntity<Object> productNotExistByNameException(ProductNotExistByNameException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("Product Already Does not Exist", BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotExistByIdException.class)
    public ResponseEntity<Object> productNotExistByIdException(ProductNotExistByIdException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("Product Not Found", BAD_REQUEST);
    }

    @ExceptionHandler(ProductAlreadyExistException.class)
    public ResponseEntity<Object> productAlreadyExistHandler(ProductAlreadyExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Product already exists...", CONFLICT);
    }

    //    order
    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ResponseEntity<Object> orderAlreadyExistHandler(OrderAlreadyExistsException e) {
        log.error("OrderAlreadyExistsException: " + e.getMessage());
        return new ResponseEntity<>("Order already exists...", BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotExistException.class)
    public ResponseEntity<Object> orderNotExistHandler(OrderNotExistException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("OrderNotExistException: " + "Order does not exist...", BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotExistByIDException.class)
    public ResponseEntity<Object> orderNotExistHandler(OrderNotExistByIDException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("OrderNotExistByIDException: " + "Order does not exist by id...", BAD_REQUEST);
    }

    //    ProductCategory
    @ExceptionHandler(ProductCategoryNotExistByNameException.class)
    public ResponseEntity<Object> productCategoryNotExistByNameException(ProductCategoryNotExistByNameException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("Product Category does not Exist", BAD_REQUEST);
    }

    @ExceptionHandler(ProductCategoryAlreadyExistByNameException.class)
    public ResponseEntity<Object> productCategoryAlreadyExistByNameHandler(ProductCategoryAlreadyExistByNameException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Product Category already existed by name...", CONFLICT);
    }

    @ExceptionHandler(ProductCategoryNotExistByIdException.class)
    public ResponseEntity<Object> productCategoryNotExistByIdException(ProductCategoryNotExistByIdException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>("Product Category Not Found by ID ", NOT_FOUND);
    }

    @ExceptionHandler(ProductCategoryAlreadyExistByIdException.class)
    public ResponseEntity<Object> productCategoryAlreadyExistByIdHandler(ProductCategoryAlreadyExistByIdException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Product Category already exists...", CONFLICT);
    }

    @ExceptionHandler(ProductCategoryNotFoundException.class)
    public ResponseEntity<Object> productCategoryNotFoundHandler(ProductCategoryNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>("Product Category is Empty...", NOT_FOUND);
    }
}



