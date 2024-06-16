package be.ucll.service;

import java.util.List;

import org.springframework.stereotype.Service;

import be.ucll.model.Address;
import be.ucll.model.Stable;
import be.ucll.repository.AddressRepository;
import be.ucll.repository.StableRepository;

@Service
public class AddressService {
    AddressRepository addressRepository;
    StableRepository stableRepository;

    public AddressService(AddressRepository addressRepository, StableRepository stableRepository) {
        this.addressRepository = addressRepository;
        this.stableRepository = stableRepository;
    }

    public Address addAddress(Address address) {
        return addressRepository.addAddress(address);
    }

    public Stable addStableToNewAddress(Stable stable) {
        Address address = stable.getAddress();
        addressRepository.addAddress(address);
        stableRepository.save(stable);
        return stable;
    }

    public Stable addExistingStableToExistingAddress(Long stableId, Long addressId) {
        Stable stable = stableRepository.findById(stableId)
                .orElseThrow(() -> new ServiceException("Stable not found."));

        Address address = addressRepository.findAddressById(addressId);
        if (address == null) {
            throw new ServiceException("Address not found.");
        }

        if (stable.getAddress() != null) {
            throw new ServiceException("Stable already has an address.");
        }

        Stable existingStableWithAddress = stableRepository.findByAddressId(addressId);
        if (existingStableWithAddress != null) {
            throw new ServiceException("Address is already assigned to a stable.");
        }

        stable.setAddress(address);
        addressRepository.assignExistingAddressToExistingStable(addressId, stableId);

        return stable;
    }

    public List<Address> getAddressesWhereStableHasMoreThan3Animals() {
        return addressRepository.findAddressesFromStablesWithNumber(3);
    }

}
