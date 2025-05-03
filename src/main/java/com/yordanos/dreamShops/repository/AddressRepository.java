package com.yordanos.dreamShops.repository;

import com.yordanos.dreamShops.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
