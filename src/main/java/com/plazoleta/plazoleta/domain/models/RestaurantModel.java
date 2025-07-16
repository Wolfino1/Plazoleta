package com.plazoleta.plazoleta.domain.models;

import com.plazoleta.plazoleta.domain.exceptions.EmptyException;
import com.plazoleta.plazoleta.domain.exceptions.MaxSizeExceededException;
import com.plazoleta.plazoleta.domain.exceptions.NullException;
import com.plazoleta.plazoleta.domain.exceptions.WrongArgumentException;
import com.plazoleta.plazoleta.domain.util.constants.DomainConstants;

public class RestaurantModel {
    private Long id;
    private String name;
    private String nit;
    private String address;
    private String phoneNumber;
    private String logoUrl;
    private Long ownerId;

    public RestaurantModel(Long id, String name, String nit, String address, String phoneNumber, String logoUrl,
                           Long ownerId) {
        this.id = id;
        setName(name);
        setNit(nit);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        setLogoUrl(logoUrl);
        setOwnerId(ownerId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new NullException(DomainConstants.NAME_NULL_MESSAGE);
        if (name.trim().isEmpty()) throw new EmptyException(DomainConstants.NAME_EMPTY_MESSAGE);
        if (name.trim().matches("^\\d+$")) {
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_NAME);
        }

        this.name = name;
    }


    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        if (nit == null) throw new NullException(DomainConstants.NIT_NULL_MESSAGE);
        if (nit.trim().isEmpty()) throw new EmptyException(DomainConstants.NIT_EMPTY_MESSAGE);

        if (!nit.matches("\\d+")) throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_NIT_MESSAGE);

        this.nit = nit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null) throw new NullException(DomainConstants.ADDRESS_NULL_MESSAGE);
        if (address.trim().isEmpty()) throw new EmptyException(DomainConstants.ADDRESS_EMPTY_MESSAGE);
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) throw new NullException(DomainConstants.PHONENUMBER_NULL_MESSAGE);
        if (phoneNumber.trim().isEmpty()) throw new EmptyException(DomainConstants.PHONENUMBER_EMPTY_MESSAGE);
        if (phoneNumber.length() > 13)
            throw new MaxSizeExceededException(DomainConstants.MAX_SIZE_EXCEEDED_PHONE_NUMBER);
        String phoneRegex = "^\\+?[0-9]{2}[0-9]{10}$";
        if (!phoneNumber.matches(phoneRegex)) {
            throw new WrongArgumentException(DomainConstants.WRONG_ARGUMENT_PHONE_MESSAGE);
        }
        this.phoneNumber = phoneNumber;

    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        if (logoUrl == null) throw new NullException(DomainConstants.LOGOURL_NULL_MESSAGE);
        if (logoUrl.trim().isEmpty()) throw new EmptyException(DomainConstants.LOGOURL_EMPTY_MESSAGE);
        this.logoUrl = logoUrl;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        if (ownerId == null) throw new NullException(DomainConstants.OWNERID_NULL_MESSAGE);

        if (ownerId <= 0) {
            throw new WrongArgumentException(DomainConstants.WRONG_OWNER_ID);
        }
        this.ownerId = ownerId;
    }
}