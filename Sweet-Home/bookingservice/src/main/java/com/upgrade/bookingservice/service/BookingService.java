package com.upgrade.bookingservice.service;

import com.upgrade.bookingservice.configuration.*;
import com.upgrade.bookingservice.model.BookingInfoEntity;
import com.upgrade.bookingservice.model.Transaction;
import com.upgrade.bookingservice.model.exception.BookingIDNotFoundException;
import com.upgrade.bookingservice.model.exception.InvalidPaymentModeException;
import com.upgrade.bookingservice.repository.BookingRepository;
import lombok.AllArgsConstructor;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


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
        bookingInfoEntity = bookingRepository.save(bookingInfoEntity);        
        return  bookingInfoEntity;
    }
    
    /**
     * this method is used to fetch records from the transaction api and update it into booking table
     * then push the message into the kafka topic 
     * First it will check and validate the request the request data like if the bookingID is not exists 
     * then it will throw Bad_Request exception and return below response
     * **/
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
                    try {
            			runProducer(1, bookingInfoEntity.toString());
            		} catch (InterruptedException e) {
            			e.printStackTrace(); //Internally throw 500 error
            		}
                }
        } else {
            throw new InvalidPaymentModeException("Invalid mode of payment");
        }
        return bookingInfoEntity;
    }
    
    /**
     * This help us to produce the Kafka Messages on EC2 kafka server 
     * 
     * @param sendMessageCount is used to set the iteration for the message, means how many time you wanted this message to be 
     * repeated
     * 
     *  @param Message: bookingentityinfo toString details will be passed in this as a successfull response after updating the
     *  transaction id in Booking Table
     * **/
    public static void runProducer(final int sendMessageCount, String message) throws InterruptedException {
    	
    	final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);
    	try (Producer<Long, String> producer = Configuration.createProducer()) {
    		long time = System.currentTimeMillis();
    	       for (long index = time; index < time + sendMessageCount; index++) {
                   final ProducerRecord<Long, String> record =
                           new ProducerRecord<>(Configuration.TOPIC, index, message);
                   producer.send(record, (metadata, exception) -> {
                       long elapsedTime = System.currentTimeMillis() - time;
                       if (metadata != null) {
                           System.out.printf("sent record(key=%s value=%s) " +
                                           "meta(partition=%d, offset=%d) time=%d\n",
                                   record.key(), record.value(), metadata.partition(),
                                   metadata.offset(), elapsedTime);
                       } else {
                           exception.printStackTrace();
                       }
                       countDownLatch.countDown();
                   });
               }
               countDownLatch.await(25, TimeUnit.SECONDS);
        }
    }
    
    
    /**
     * callPaymentServiceApi method is used to pass the transaction json raw data to the Payment api
     * then it will fetch the transaction api in response
     * 
     * @param Transaction modal will be passed in raw body as json
     * **/
    private Transaction callPaymentServiceApi(Transaction paymentDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Transaction> entity = new HttpEntity<Transaction>(paymentDetails,headers);

        return restTemplate.exchange(
                "http://localhost:8083/transaction", HttpMethod.POST, entity, Transaction.class).getBody();
    }

    /**
     * This Method Generating random room numbers
     * @param Count: number of rooms required.
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
