package com.itb.demo;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

/**
 *
 * @author mgmat
 */
@Component
public class GraphQLDataFetchers {

    private static List<Map<String, String>> film = Arrays.asList(
            ImmutableMap.of("id", "1",
                    "name", "Star Wars: The phantom menace",
                    "year", "1999",
                    "directorId", "1"),
            ImmutableMap.of("id", "2",
                    "name", "Star Wars: The Attack of the Clones",
                    "year", "2002",
                    "directorId", "1"),
            ImmutableMap.of("id", "2",
                    "name", "Star Wars: The Revenge of the sith",
                    "year", "2005",
                    "directorId", "1"),
            ImmutableMap.of("id", "4",
                    "name", "Star Wars: A new Hope",
                    "year", "1977",
                    "directorId", "1"),
            ImmutableMap.of("id", "5",
                    "name", "Star Wars: The Empire Strikes Back",
                    "year", "1980",
                    "directorId", "2"),
            ImmutableMap.of("id", "6",
                    "name", "Star Wars: The Return of the Jedi",
                    "year", "1986",
                    "directorId", "3")
    );

    private static List<Map<String, String>> director = Arrays.asList(
            ImmutableMap.of("id", "1",
                    "name", "George Lucas",
                    "countryId", "1"),
            ImmutableMap.of("id", "2",
                    "name", "Irvin Kershner",
                    "countryId", "1"),
            ImmutableMap.of("id", "3",
                    "name", "Richard Marquand",
                    "countryId", "2")
    );

    private static List<Map<String, String>> country = Arrays.asList(
            ImmutableMap.of("id", "1",
                    "name", "Estados Unidos",
                    "code", "USA"),
            ImmutableMap.of("id", "2",
                    "name", "Reino Unido",
                    "code", "UK")
    );

    private static List<Map<String, String>> character = Arrays.asList(
            ImmutableMap.of("id", "1",
                    "name", "Luke Skywalker"),
            ImmutableMap.of("id", "2",
                    "name", "Leia Organa"),
            ImmutableMap.of("id", "3",
                    "name", "Darth Vader"),
            ImmutableMap.of("id", "4",
                    "name", "Qui Gon jinn"),
            ImmutableMap.of("id", "5",
                    "name", "Obi Wan Kenobi"),
            ImmutableMap.of("id", "6",
                    "name", "Darth Sidious"),
            ImmutableMap.of("id", "7",
                    "name", "Padme Amidala"),
            ImmutableMap.of("id", "8",
                    "name", "C3PO"),
            ImmutableMap.of("id", "9",
                    "name", "R2D2"),
            ImmutableMap.of("id", "10",
                    "name", "Yoda"),
            ImmutableMap.of("id", "11",
                    "name", "Darth Maul")
    );

    private static List<Map<String, String>> cast = Arrays.asList(
            ImmutableMap.of("film", "4",
                    "characterId", "1"),
            ImmutableMap.of("film", "4",
                    "characterId", "2"),
            ImmutableMap.of("film", "4",
                    "characterId", "3")
    );

    public DataFetcher getFilmsDataFetcher() {
        return dataFetchingEnvironment -> {
            String filmId = dataFetchingEnvironment.getArgument("id");
            return film;
        };
    }

    public DataFetcher getFilmByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String filmId = dataFetchingEnvironment.getArgument("id");
            return film
                    .stream()
                    .filter(film -> film.get("id").equals(filmId)).findFirst().orElse(null);
        };
    }

    public DataFetcher getDirectorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> film = dataFetchingEnvironment.getSource();
            String directorId = film.get("directorId");
            return director
                    .stream()
                    .filter(director -> director.get("id").equals(directorId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getCountryDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> film = dataFetchingEnvironment.getSource();
            String directorId = film.get("countryId");
            return country
                    .stream()
                    .filter(country -> country.get("id").equals(directorId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getCastDataFetcher() {
        return dataFetchingEnvironment -> {
            String filmId = dataFetchingEnvironment.getArgument("film");
            List<Map> chars = cast.stream().filter(cs -> cs.get("film").equals(filmId)).collect(Collectors.toList());
            List cdata = new ArrayList();
            Object[] result = new Object[chars.size()];
            int i=0;
            for (Map c : chars) {
                Object[] rs = character.stream().filter(ch -> ch.get("id").equals(c.get("characterId"))).toArray();
                result[i]=rs[0];
                i++;
            }
            return result;
        };
    }

    public DataFetcher getCharacterDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> cast = dataFetchingEnvironment.getSource();
            String characterId = cast.get("characterId");
            return character
                    .stream()
                    .filter(character -> character.get("id").equals(characterId)).toArray();
        };
    }
}
