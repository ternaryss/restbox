package pl.tss.restbox.core.handler.movie;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import pl.tss.restbox.core.domain.command.Cmd;
import pl.tss.restbox.core.domain.command.movie.GetMovieCmd;
import pl.tss.restbox.core.domain.dto.MovieDetailsDto;
import pl.tss.restbox.core.domain.dto.PersonDto;
import pl.tss.restbox.core.domain.entity.Movie;
import pl.tss.restbox.core.domain.entity.Person;
import pl.tss.restbox.core.handler.CommandHandler;
import pl.tss.restbox.core.port.output.repo.MovieRepo;

/**
 * Get single movie details.
 *
 * @author TSS
 */
@Slf4j
public class GetMovie extends CommandHandler {

  private final MovieRepo movieRepo;

  public GetMovie(MovieRepo movieRepo) {
    this.movieRepo = movieRepo;
  }

  @Override
  public Cmd<?, ?> handle(Cmd<?, ?> command) {
    Integer input = (Integer) command.getInput();

    log.info("Getting movie [movId = {}]", input);

    Movie movie = movieRepo.findFirstByMovId(input);

    List<PersonDto> actorsDto = new LinkedList<>();
    movie.getActors().forEach(act -> {
      if (act.isAct()) {
        Person actor = act.getPerson();

        if (actor.isAct()) {
          PersonDto actorDto = PersonDto.builder().perId(actor.getPerId()).firstName(actor.getFirstName())
              .secondName(actor.getSecondName()).lastName(actor.getLastName())
              .birthday(actor.getBirthday().withNano(0).toString()).age(actor.getAge()).rate(actor.getRate())
              .act(actor.isAct()).build();
          actorsDto.add(actorDto);
        }
      }
    });

    Person director = movie.getDirector();
    PersonDto directorDto = null;

    if (director.isAct()) {
      directorDto = PersonDto.builder().perId(director.getPerId()).firstName(director.getFirstName())
          .secondName(director.getSecondName()).lastName(director.getLastName())
          .birthday(director.getBirthday().withNano(0).toString()).age(director.getAge()).rate(director.getRate())
          .act(director.isAct()).build();
    }

    MovieDetailsDto movieDto = MovieDetailsDto.builder().movId(movie.getMovId()).title(movie.getTitle())
        .genere(movie.getGenere().isAct() ? movie.getGenere().getName() : null).description(movie.getDescription())
        .premiere(movie.getPremiere().withNano(0).toString()).rate(movie.getRate()).length(movie.getLength())
        .country(movie.getCountry().isAct() ? movie.getCountry().getName() : null).act(movie.isAct())
        .director(directorDto).actors(actorsDto).build();

    ((GetMovieCmd) command).setOutput(movieDto);
    log.info("Movie got [movId = {}]", movie.getMovId());

    return super.handle(command);
  }

}
