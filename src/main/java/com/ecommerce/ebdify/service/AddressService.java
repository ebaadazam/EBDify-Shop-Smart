package com.ecommerce.ebdify.service;

import com.ecommerce.ebdify.models.dtos.request.AddressDTO;
import com.ecommerce.ebdify.models.entities.User;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressesById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);

}
