package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		Optional<Customer> optionalcustomer=customerRepository2.findById(customerId);
		if(optionalcustomer.isPresent()==false)return null;
		Customer customer = optionalcustomer.get();
		List<Driver> driverList = driverRepository2.findAll();
		for(Driver driver:driverList){
			Cab cab=driver.getCab();
			if(cab.isAvailability()){
				cab.setAvailability(false);
				TripBooking trip = new TripBooking();
				trip.setBill(distanceInKm*cab.getPerKmRate());
				trip.setDistance(distanceInKm);
				trip.setDriver(driver);
				trip.setFromLocation(fromLocation);
				trip.setToLocation(toLocation);
				trip.setTripStatus(TripStatus.CONFIRMED);
				TripBooking savedTrip = tripBookingRepository2.save(trip);
				driver.getTripBookins().add(savedTrip);
				customer.getTripBookingsList().add(savedTrip);
				driverRepository2.save(driver);
				customerRepository2.save(customer);
				return savedTrip;
			}
		}
		throw new RuntimeException("No cab available!");
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> optionalTrip = tripBookingRepository2.findById(tripId);
		if(optionalTrip.isPresent()==false)return;
		TripBooking trip = optionalTrip.get();
		trip.setTripStatus(TripStatus.CANCELED);
		Driver driver = trip.getDriver();
		driver.getCab().setAvailability(true);
		trip.setBill(0);
		driverRepository2.save(driver);
		tripBookingRepository2.save(trip);


	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> optionaltrip = tripBookingRepository2.findById(tripId);
		if(optionaltrip.isPresent()==false)return;
		TripBooking trip = optionaltrip.get();
		trip.setTripStatus(TripStatus.COMPLETED);
		Driver driver = trip.getDriver();
		driver.getCab().setAvailability(true);
		trip.setBill(0);
		driverRepository2.save(driver);
		tripBookingRepository2.save(trip);


	}
}
