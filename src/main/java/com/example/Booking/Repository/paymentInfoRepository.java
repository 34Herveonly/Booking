package com.example.Booking.Repository;

import com.example.Booking.Model.paymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface paymentInfoRepository extends JpaRepository<paymentInformation,Long> {

}
