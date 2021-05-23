package com.upgrade.bookingservice.service;

import com.upgrade.bookingservice.configuration.*;
import com.upgrade.bookingservice.model.BookingInfoEntity;
import com.upgrade.bookingservice.model.Transaction;
import com.upgrade.bookingservice.model.exception.BookingIDNotFoundException;
import com.upgrade.bookingservice.model.exception.InvalidPaymentModeException;
import com.upgrade.bookingservice.repository.BookingRepository;
import lombok.AllArgsConstructor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
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
        try {
			runProducer(1,"hi "+ bookingInfoEntity.toString());
		} catch (InterruptedException e) {
			e.printStackTrace(); //Internally throw 500 error
		}
        return  bookingInfoEntity;
    }
    
    /**
     * 
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
                }
        } else {
            throw new InvalidPaymentModeException("Invalid mode of payment");
        }
        return bookingInfoEntity;
    }
    
    /**
     * 
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
    	
    	
    	/*
        final Producer<Long, String> producer = Configuration.createProducer();
        long time = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);
        
        try {
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
        }finally {
            producer.flush();
            producer.close();
        }
        */
    }
    
    
    /**
     * 
     * **/
    private Transaction callPaymentServiceApi(Transaction paymentDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Transaction> entity = new HttpEntity<Transaction>(paymentDetails,headers);

        return restTemplate.exchange(
                "http://localhost:8083/transaction", HttpMethod.POST, entity, Transaction.class).getBody();
    }

    /**
     * This Method Generating random room numbers, to get the randomly generated room
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
