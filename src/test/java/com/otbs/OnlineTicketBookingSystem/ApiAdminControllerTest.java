package com.otbs.OnlineTicketBookingSystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otbs.OnlineTicketBookingSystem.entity.Genres;
import com.otbs.OnlineTicketBookingSystem.entity.Movie;
import com.otbs.OnlineTicketBookingSystem.model.HallDTO;
import com.otbs.OnlineTicketBookingSystem.repository.HallRepository;
import com.otbs.OnlineTicketBookingSystem.repository.MovieRepository;
import com.otbs.OnlineTicketBookingSystem.service.HallServiceI;
import com.otbs.OnlineTicketBookingSystem.service.PosterServiceI;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Log4j2
public class ApiAdminControllerTest {

    @Value("${download.path}")
    private String uploadDir;

    @Autowired
    private PosterServiceI posterServiceI;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HallServiceI hallServiceI;

    private static MockMultipartFile getMultipartFile() {
        String fileName = "Квадрат.jpg";


        byte[] imageBytes = new byte[]{
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
        };

        return new MockMultipartFile("img", fileName, MediaType.IMAGE_JPEG_VALUE, imageBytes);
    }

    private static MockMultipartFile getEmptyMultipartFile() {
        String fileName = "Квадрат.jpg";


        byte[] imageBytes = new byte[]{};

        return new MockMultipartFile("img", fileName, MediaType.IMAGE_JPEG_VALUE, imageBytes);
    }

    @BeforeEach void createDataBaseEntities() {
        log.info("Список фильмов - " + movieRepository.findAll());
        log.info("Список залов - " + hallRepository.findAll());
    }

    @AfterEach void showMoviesList() {
        log.info("Список залов - " + hallRepository.findAll());
        log.info("Список фильмов - " + movieRepository.findAll());
        movieRepository.deleteAll();
        hallRepository.deleteAll();
        log.info("Список залов очищен - " + hallRepository.findAll());
        log.info("Список фильмов очищен - " + movieRepository.findAll());
    }

    @AfterAll void clearPosterDirectory() throws IOException {
        log.info("Путь к постерам - " + uploadDir);
        try (Stream<Path> walk = Files.walk(Path.of(uploadDir))) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestSaveMovieExpectReturnStatusOk() throws Exception {
        Movie movie = Movie.builder()
                .name("Геркулес")
                .director("Джеймс Кэмерон")
                .year(1997)
                .genres(List.of(Genres.HISTORY, Genres.THRILLER, Genres.DRAMA, Genres.MELODRAMA))
                .synopsis(
                        "В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту. Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, а айсберг, вставший на пути считавшегося непотопляемым лайнера.")
                .duration(Duration.ofHours(3).plusMinutes(14))
                .build();
        MockMultipartFile file = getMultipartFile();

        String movieJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(movie);
        MockMultipartFile jsonPart = new MockMultipartFile("movie", "json", "application/json", movieJson.getBytes(
                StandardCharsets.UTF_8));

        System.out.println(mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/save-movie")
                        .file(jsonPart)
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestSaveMovieWithValidErrorOfYearFieldExpectStatusBadRequest() throws Exception {
        Movie movie = Movie.builder()
                .name("Геркулес")
                .director("Джеймс Кэмерон")
                .year(800)
                .genres(List.of(Genres.HISTORY, Genres.THRILLER, Genres.DRAMA, Genres.MELODRAMA))
                .synopsis(
                        "В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту. Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, а айсберг, вставший на пути считавшегося непотопляемым лайнера.")
                .duration(Duration.ofHours(3).plusMinutes(14))
                .build();
        MockMultipartFile file = getMultipartFile();

        String movieJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(movie);
        MockMultipartFile jsonPart = new MockMultipartFile("movie", "json", "application/json", movieJson.getBytes(
                StandardCharsets.UTF_8));

        System.out.println(mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/save-movie")
                        .file(jsonPart)
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestSaveMovieWithValidErrorOfMultipartFileExpectStatusBadRequest() throws Exception {
        Movie movie = Movie.builder()
                .name("Геркулес")
                .director("Джеймс Кэмерон")
                .year(1989)
                .genres(List.of(Genres.HISTORY, Genres.THRILLER, Genres.DRAMA, Genres.MELODRAMA))
                .synopsis(
                        "В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту. Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, а айсберг, вставший на пути считавшегося непотопляемым лайнера.")
                .duration(Duration.ofHours(3).plusMinutes(14))
                .build();
        MockMultipartFile file = getEmptyMultipartFile();


        String movieJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(movie);
        MockMultipartFile jsonPart = new MockMultipartFile("movie", "json", "application/json", movieJson.getBytes(
                StandardCharsets.UTF_8));

        System.out.println(mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/save-movie")
                        .file(jsonPart)
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestSaveMovieWithAlreadyExistMovieExpectStatusOk() throws Exception {
        Movie movie = Movie.builder()
                .name("Титаник")
                .director("Джеймс Кэмерон")
                .year(1997)
                .genres(List.of(Genres.HISTORY, Genres.THRILLER, Genres.DRAMA, Genres.MELODRAMA))
                .synopsis(
                        "В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту. Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, а айсберг, вставший на пути считавшегося непотопляемым лайнера.")
                .duration(Duration.ofHours(3).plusMinutes(14))
                .build();
        MockMultipartFile file = getMultipartFile();
        posterServiceI.save(movie, file);
        log.info("Movie object clone saved in database");


        String movieJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(movie);
        MockMultipartFile jsonPart = new MockMultipartFile("movie", "json", "application/json", movieJson.getBytes(
                StandardCharsets.UTF_8));

        System.out.println(mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/save-movie")
                        .file(jsonPart)
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestUpdateMovieExpectStatusOk() throws Exception {
        Movie movie = Movie.builder()
                .name("Титаник")
                .director("Джеймс Кэмерон")
                .year(1997)
                .genres(List.of(Genres.HISTORY, Genres.THRILLER, Genres.DRAMA, Genres.MELODRAMA))
                .synopsis(
                        "В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту. Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, а айсберг, вставший на пути считавшегося непотопляемым лайнера.")
                .duration(Duration.ofHours(3).plusMinutes(14))
                .build();
        MockMultipartFile file = getMultipartFile();
        posterServiceI.save(movie, file);
        log.info("Movie object clone saved in database");

        movie.setId(1L);
        movie.setName("Остров проклятых");
        String movieJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(movie);
        MockMultipartFile jsonPart = new MockMultipartFile("movie", "json", "application/json", movieJson.getBytes(
                StandardCharsets.UTF_8));

        log.info("Список фильмов - " + movieRepository.findAll());
        System.out.println(mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/save-movie")
                        .file(jsonPart)
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestSaveMovieWithNullMovieRequestExpectStatusBadRequest() throws Exception {
        MockMultipartFile file = getMultipartFile();


        String movieJson = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(null);
        MockMultipartFile jsonPart = new MockMultipartFile("movie", "json", "application/json", movieJson.getBytes(
                StandardCharsets.UTF_8));

        System.out.println(mockMvc.perform(MockMvcRequestBuilders.multipart("/api/admin/save-movie")
                        .file(jsonPart)
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestDeleteMovieById1ExpectStatusNoContent() throws Exception {
        Movie movie = Movie.builder()
                .name("Gитаник")
                .director("Джеймс Кэмерон")
                .year(1997)
                .genres(List.of(Genres.HISTORY, Genres.THRILLER, Genres.DRAMA, Genres.MELODRAMA))
                .synopsis(
                        "В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту. Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, а айсберг, вставший на пути считавшегося непотопляемым лайнера.")
                .duration(Duration.ofHours(3).plusMinutes(14))
                .build();
        movie = posterServiceI.save(movie, getMultipartFile());
        log.info(movieRepository.findAll());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/delete-movie").param("id", movie.getId().toString()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }


    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestDeleteMovieByWrongIdExpectStatusNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/delete-movie").param("id", "-1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestDeleteMovieByIdNullExpectStatusNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/delete-movie").param("id", "null"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }


    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestSaveHallExpectStatusOk() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        HallDTO hallDTO = new HallDTO(1, 20, 20);
        String hallJson = objectMapper.writeValueAsString(hallDTO);
        log.info(hallJson);
        HallDTO hallDTO1 = objectMapper.readValue(hallJson, HallDTO.class);
        log.info(hallDTO1);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/save-hall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hallJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestDeleteHallByIdExpectStatusNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/delete-hall").param("id", "-1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestDeleteHallByIdExpectStatusNoContent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        HallDTO hallDTO = new HallDTO(4, 20, 20);
        log.info("Список залов: " + hallRepository.findAll());
        hallServiceI.save(hallDTO);
        String hallJson = objectMapper.writeValueAsString(hallDTO);
        log.info(hallJson);
        log.info("Список залов: " + hallRepository.findAll());
        log.info("Список залов: " + hallRepository.findAll());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/delete-hall").param("id", "4"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }


    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestDeleteHallByIdNullExpectStatusNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/delete-hall").param("id", "null"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @WithMockUser(username = "Abama@gmail.com")
    void testRequestSaveHallExpectValidError() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        HallDTO hallDTO = new HallDTO(null, 20, 20);
        String hallJson = objectMapper.writeValueAsString(hallDTO);
        log.info(hallJson);
        HallDTO hallDTO1 = objectMapper.readValue(hallJson, HallDTO.class);
        log.info(hallDTO1);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/save-hall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hallJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }
}

