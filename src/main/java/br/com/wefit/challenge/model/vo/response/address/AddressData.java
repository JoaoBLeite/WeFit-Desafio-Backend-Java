package br.com.wefit.challenge.model.vo.response.address;

import java.util.UUID;

public record AddressData(
    UUID id,
    String cep,
    String state,
    String city,
    String neighborhood,
    String street,
    String number,
    String complement) {}
