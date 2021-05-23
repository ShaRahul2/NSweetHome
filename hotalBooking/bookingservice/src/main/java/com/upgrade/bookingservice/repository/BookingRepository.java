package com.upgrade.bookingservice.repository;

import com.upgrade.bookingservice.model.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface BookingRepository extends JpaRepository<BookingInfoEntity, Serializable> {
}
