package unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import be.ucll.model.Address;
import be.ucll.model.Stable;
import be.ucll.repository.AddressRepository;
import be.ucll.repository.StableRepository;
import be.ucll.service.AddressService;
import be.ucll.service.ServiceException;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;

    @Mock
    StableRepository stableRepository;

    @InjectMocks
    AddressService addressService;

    @Test
    public void givenAddress_whenAddAddress_thenAddressIsAdded() {
        Address address = new Address("Halensebaan", 9, "Waanrode");
        when(addressRepository.addAddress(address)).thenReturn(address);

        Address addedAddress = addressService.addAddress(address);

        Mockito.verify(addressRepository).addAddress(address);
        assertEquals(address, addedAddress);
    }

    @Test
    public void givenNewStableWithNewAddress_whenAddNewStableToNewAddress_thenStableIsCreatedWithNewAddress() {
        Stable mockedStable = Mockito.mock(Stable.class);
        Address mockAddress = Mockito.mock(Address.class);
        when(mockedStable.getAddress()).thenReturn(mockAddress);
        when(addressRepository.addAddress(mockAddress)).thenReturn(mockAddress);
        when(stableRepository.save(mockedStable)).thenReturn(mockedStable);

        Stable returned = addressService.addStableToNewAddress(mockedStable);

        Mockito.verify(addressRepository).addAddress(mockAddress);
        Mockito.verify(stableRepository).save(mockedStable);
        assertEquals(mockedStable, returned);
    }

    @Test
    public void givenStableIdAndAddressId_whenAssigningAddressToStable_thenAddressIsAssigned() {
        Address address = new Address("Halensebaan", 9, "Waanrode");
        Stable stable = new Stable("HomeStable", 5);
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));
        when(addressRepository.findAddressById(address.getId())).thenReturn(address);

        Stable retuned = addressService.addExistingStableToExistingAddress(stable.getId(), address.getId());

        assertEquals(stable, retuned);
    }

    @Test
    public void givenStableIdAndAddressId_whenStableNotExists_thenExceptionIsThrown() {
        Address address = new Address("Halensebaan", 9, "Waanrode");
        Stable mock = Mockito.mock(Stable.class);
        when(stableRepository.findById(mock.getId())).thenReturn(Optional.empty());

        Exception ex = assertThrows(ServiceException.class,
                () -> addressService.addExistingStableToExistingAddress(mock.getId(), address.getId()));

        assertEquals("Stable not found.", ex.getMessage());
    }

    @Test
    public void givenStableIdAndAddressId_whenAddressNotExists_thenExceptionIsThrown() {
        Address address = Mockito.mock(Address.class);
        Stable stable = new Stable("HomeStable", 5);

        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));
        when(addressRepository.findAddressById(address.getId())).thenReturn(null);

        Exception ex = assertThrows(ServiceException.class,
                () -> addressService.addExistingStableToExistingAddress(stable.getId(), address.getId()));

        assertEquals("Address not found.", ex.getMessage());
    }

    @Test
    public void givenStableIdAndAddressId_whenStableAlreadyHasAddress_thenExceptionIsThrown() {
        Address address = new Address("Halensebaan", 9, "Waanrode");
        Stable stable = Mockito.mock(Stable.class);
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));
        when(addressRepository.findAddressById(address.getId())).thenReturn(address);
        when(stable.getAddress()).thenThrow(new ServiceException("Stable already has an address."));

        Exception ex = assertThrows(ServiceException.class,
                () -> addressService.addExistingStableToExistingAddress(stable.getId(), address.getId()));

        assertEquals("Stable already has an address.", ex.getMessage());
    }

    @Test
    public void givenStableIdAndAddressId_whenAddressAlreadyAssignedToStable_thenExceptionIsThrown() {
        Address address = new Address("Halensebaan", 9, "Waanrode");
        Stable stable = Mockito.mock(Stable.class);
        when(stableRepository.findById(stable.getId())).thenReturn(Optional.of(stable));
        when(addressRepository.findAddressById(address.getId())).thenReturn(address);
        when(stable.getAddress()).thenReturn(null);
        when(stableRepository.findByAddressId(address.getId()))
                .thenThrow(new ServiceException("Address is already assigned to a stable."));

        Exception ex = assertThrows(ServiceException.class,
                () -> addressService.addExistingStableToExistingAddress(stable.getId(), address.getId()));

        assertEquals("Address is already assigned to a stable.", ex.getMessage());
    }

    @Test
    public void givenAddresses_whenFindStableAddressesWithMoreThan3Animals_thenReturnThoseStables() {
        List<Address> addresses = new ArrayList<>();
        Address address = new Address("Halensebaan", 9, "Waanrode");
        addresses.add(address);
        when(addressRepository.findAddressesFromStablesWithNumber(3)).thenReturn(addresses);

        List<Address> returned = addressService.getAddressesWhereStableHasMoreThan3Animals();

        Mockito.verify(addressRepository).findAddressesFromStablesWithNumber(3);
        assertEquals(addresses, returned);
    }

}
