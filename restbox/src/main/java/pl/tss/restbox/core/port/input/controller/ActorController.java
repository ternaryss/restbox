package pl.tss.restbox.core.port.input.controller;

import pl.tss.restbox.core.domain.dto.PersonDto;

/**
 * Definition of actor controller.
 *
 * @author TSS
 */
public interface ActorController<R> {

  R addActor(PersonDto payload);

  R editActor(Integer perId, PersonDto payload);

  R getActors(String firstName, String lastName, Integer rate, Integer page, Integer size, String sort);

}
