package com.upgrade.bookingservice.service;

import com.upgrade.bookingservice.model.BookingInfoEntity;
import com.upgrade.bookingservice.model.Transaction;
import com.upgrade.bookingservice.model.exception.BookingIDNotFoundException;
import com.upgrade.bookingservice.model.exception.InvalidPaymentModeException;
import com.upgrade.bookingservice.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.StringJoiner;

@Service
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;

    //private Transaction transaction;

    private RestTemplate restTemplate;

    /*
     *This Method create booking records
     * @param BookingInfoEntity
     * @return BookingInfoEntity
     * */
    public BookingInfoEntity createBooking(BookingInfoEntity bookingInfoEntity) {
        System.out.println(bookingInfoEntity.getFromDate() + "\t" + bookingInfoEntity.getToDate());
       long noOfDays =  ChronoUnit.DAYS.between(bookingInfoEntity.getFromDate(), bookingInfoEntity.getToDate());
        bookingInfoEntity.setRoomPrice( 1000 * bookingInfoEntity.getNumOfRooms() * ((int)noOfDays) );
        bookingInfoEntity.setRoomNumbers(getRandomNumber(bookingInfoEntity.getNumOfRooms()));
        bookingInfoEntity = bookingRepository.saveAndFlush(bookingInfoEntity);

        //transaction.setBookingId(bookingInfoEntity.getBookingId()); //Get Booking Id And set in Transaction bookingId

        return  bookingInfoEntity;
    }

    /*public void bookingTransaction(Transaction paymentDetails) {
        //paymentDetails.setBookingId(transaction.getBookingId()); //set bookingid in paymentDetails

        //Call PaymentService Api
        //paymentDetails = callPaymentServiceApi(paymentDetails);

        System.out.println("Booking Room Payment Successfully : " + paymentDetails);

        Optional<BookingInfoEntity> optionalBookingInfoEntity = bookingRepository.findById(paymentDetails.getBookingId());
        if (!optionalBookingInfoEntity.isPresent()) {
            throw new BookingIDNotFoundException("Invalid Booking Id");
        }
        BookingInfoEntity bookingInfoEntity = optionalBookingInfoEntity.get();
        bookingInfoEntity.setTransactionId(paymentDetails.getTransactionId());
        System.out.println("Update Transaction Id Successfully : " + bookingRepository.save(bookingInfoEntity));

    }*/

    public BookingInfoEntity getBookingById(int id, Transaction transaction) {
        BookingInfoEntity bookingInfoEntity = null;

        if (transaction.getPaymentMode().equalsIgnoreCase("UPI") ||
                transaction.getPaymentMode().equalsIgnoreCase("CARD") ) {
                Optional<BookingInfoEntity> optionalBookingInfoEntity = bookingRepository.findById(id);
                if (!optionalBookingInfoEntity.isPresent()) {
                    throw new BookingIDNotFoundException("Invalid Booking Id");
                } else {
                     bookingInfoEntity = optionalBookingInfoEntity.get();
                     transaction.setBookingId(bookingInfoEntity.getBookingId());

                     //Call PaymentService Api
                    transaction =  callPaymentServiceApi(transaction);
                    bookingInfoEntity.setTransactionId(transaction.getTransactionId());
                    bookingInfoEntity = bookingRepository.save(bookingInfoEntity);
                }
        } else {
            throw new InvalidPaymentModeException("Invalid mode of payment");
        }
        return bookingInfoEntity;
    }

    private Transaction callPaymentServiceApi(Transaction paymentDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Transaction> entity = new HttpEntity<Transaction>(paymentDetails,headers);

        return restTemplate.exchange(
                "http://localhost:8083/transaction", HttpMethod.POST, entity, Transaction.class).getBody();
    }

    /**
     * This Method Generating room numbers, Hom many rooms are available
     * */
    private String getRandomNumber(int count) {

        Random random = new Random();
        StringJoiner stringJoiner = new StringJoiner(",");

        for (int i = 0; i < count; i++) {
            stringJoiner.add(String.valueOf(random.nextInt(100)));
        }
        return stringJoiner.toString();
    }
}
