package com.umcs.enterprise;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Seed {

	private final CoverRepository coverRepository;

	private BookCover getCover(String filename) throws IOException {
		Path uploadDir = Paths.get("static/covers");
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}

		Path file = uploadDir.resolve(filename);

		BufferedImage image = null;
		try {
			image = ImageIO.read(file.toFile());
		} catch (IOException e) {
			return null;
		}

		BookCover bookCover = new BookCover();
		bookCover.setFilename(filename);
		bookCover.setHeight(image.getHeight());
		bookCover.setWidth(image.getWidth());

		return coverRepository.save(bookCover);
	}

	@Bean
	CommandLineRunner initDatabase(BookRepository repository, BookCoverService coverService) {
		return args -> {
			ZonedDateTime date = ZonedDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Paris"));

			repository.saveAll(
				Arrays.asList(
					new Book(
						null,
						"Erich Maria Remarque",
						"Na Zachodzie bez zmian. Wydanie filmowe",
						getCover(""),
						2340,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Fredrik Backman",
						"Mężczyzna imieniem Ove",
						getCover(""),
						3118,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"William Lindsay Gresham",
						"Zaułek koszmarów",
						getCover(""),
						2694,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Andrzej Sapkowski",
						"Pakiet Wiedźmin. Tomy 1-8: Ostatnie życzenie, Miecz przeznaczenia, Krew elfów, Czas pogardy, Chrzest ognia, Wieża Jaskółki, Pani Jeziora, Sezon burz",
						getCover(""),
						39900,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"George R.R. Martin",
						"Pakiet Ogień i krew. Części 1-2. Wydanie serialowe",
						getCover(""),
						7644,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"George R.R. Martin",
						"Ogień i krew. Tom 1. Wydanie serialowe",
						getCover(""),
						3822,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"George R.R. Martin",
						"Ogień i krew. Tom 2. Wydanie serialowe",
						getCover(""),
						3822,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Wojciech Chmielarz",
						"Wyrwa",
						getCover(""),
						2908,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Martin J. Sherwin Kai Bird",
						"Oppenheimer. Triumf i tragedia ojca bomby atomowej",
						getCover(""),
						6267,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Neil Gaiman",
						"DC Black Label Kraina Snów. Sandman. Tom 3",
						getCover(""),
						5001,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Neil Gaiman",
						"DC Black Label Dom lalki. Sandman. Tom 2",
						getCover(""),
						5744,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Neil Gaiman Sam Kieth Malcolm  Jones Iii Mike  Dringenberg",
						"DC Black Label Preludia i nokturny. Sandman. Tom 1",
						getCover(""),
						6488,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Patti  McCracken",
						"Wioska wdów. Szokująca historia morderczyń z wioski Nagyrév",
						getCover("9788383211916.jpg"),
						3044,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Leonard J. Pełka",
						"Świat Słowian. Kultura, przyroda, duchowość",
						getCover("9788367639224.jpg"),
						3865,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Susan  Stokes-Chapman",
						"Pandora",
						getCover("9788383211909.jpg"),
						3184,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Ewa Przydryga",
						"Pomornica",
						getCover("9788328724570.jpg"),
						2806,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Danuta  Chlupowa",
						"Podróż w niechciane",
						getCover("9788327163851.jpg"),
						2979,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Tatiana De Rosnay",
						"Klucz Sary",
						getCover("9788367426633.jpg"),
						3066,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Gabrielle Zevin",
						"Jutro, jutro i znów jutro",
						getCover("9788382028515.jpg"),
						3661,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Arin  Murphy-Hiscock",
						"W ogrodzie czarownicy. Jak stworzyć i uprawiać magiczny roślinny zakątek",
						getCover("9788383211862.jpg"),
						2894,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Weronika Marczak",
						"Królewna. Rodzina Monet. Tom 2. Część 2",
						getCover("9788328726239.jpg"),
						2694,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Melanie Marquis",
						"Beltane. Rytuały, przepisy i zaklęcia na święto kwiatów",
						getCover("9788383212746.jpg"),
						2488,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Książę Harry",
						"Ten drugi",
						getCover("9788367510653.jpg"),
						3879,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Ekke Overbeek",
						"Maxima Culpa. Jan Paweł II wiedział",
						getCover("9788326841088.jpg"),
						3758,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Remigiusz Mróz",
						"Kabalista",
						getCover("9788382805222.jpg"),
						3382,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Dustin  Thao",
						"Z tej strony Sam",
						getCover("9788382952872.jpg"),
						2848,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Ludka Skrzydlewska",
						"Do zakochania jeden urok. Czarownice z Inverness. Tom 1",
						getCover("9788383211855.jpg"),
						2894,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Elżbieta Cherezińska",
						"Sydonia. Słowo się rzekło",
						getCover("9788382027501.jpg"),
						3886,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Emilia Szelest",
						"Orszak śmierci. Bieszczadzkie demony. Tom 3",
						getCover("9788383212005.jpg"),
						2488,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Mikołaj Marcela",
						"Wszystko, czego ci nie mówią, gdy jesteś nastolatkiem",
						getCover("9788328725706.jpg"),
						2574,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Wisława Szymborska",
						"Wiersze wszystkie",
						getCover(""),
						6589,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Joanna Jax",
						"Trzy kobiety",
						getCover(""),
						3794,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Max Czornyj",
						"Inkarnator. Honoriusz Mond. Tom 3",
						getCover(""),
						3062,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Jacek Bartosiak",
						"Najlepsze miejsce na świecie. Gdzie Wschód zderza się z Zachodem",
						getCover(""),
						4775,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"M I Rio",
						"A jeśli jesteśmy złoczyńcami",
						getCover(""),
						3294,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Sosuke Natsukawa",
						"O kocie, który ratował książki",
						getCover(""),
						3660,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"John Jory",
						"Wzorowe Jajo. Food Group",
						getCover(""),
						2130,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Roger Priddy",
						"Akademia mądrego dziecka. Co czujesz?",
						getCover(""),
						2483,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Beth Lincoln",
						"Swiftowie",
						getCover(""),
						3795,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Barbara Supeł",
						"Urodzinowe przyjęcie. Staś Pętelka",
						getCover(""),
						2045,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Bravi Soledad",
						"Księga dźwięków",
						getCover(""),
						2784,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Kirsten Bradley",
						"To nadal pestka!",
						getCover(""),
						3404,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Paulette Bourgeois",
						"Franklin i ukochany kocyk",
						getCover(""),
						1097,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marta Kulesza",
						"Rok w domu",
						getCover(""),
						4225,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marta Guzowska",
						"Zagadka zwierciadła Twardowskiego. Detektywi z Tajemniczej 5 kontra duchy. Tom 3",
						getCover(""),
						1948,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Natasha  Farrant Lydia Corry",
						"Dziewczynka, która rozmawiała z drzewami",
						getCover(""),
						4234,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marta Kisiel",
						"Małe Licho i krok w nieznane",
						getCover(""),
						2670,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Ewa  Czajka",
						"Pomocne bajki na codzienne troski",
						getCover(""),
						2341,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Ekke Overbeek",
						"Maxima Culpa. Jan Paweł II wiedział",
						getCover(""),
						3758,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Stanisław Obirek Artur Nowak",
						"Babilon. Kryminalna historia kościoła",
						getCover(""),
						3157,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marcin Gutowski",
						"Bielmo. Co wiedział Jan Paweł II",
						getCover(""),
						3157,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marcin Adamiec",
						"Zniknięty ksiądz. Moja historia",
						getCover(""),
						3073,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Andrea Riccardi Augustyn Edward",
						"Kościół płonie",
						getCover(""),
						3239,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Katarzyna Borowska",
						"Wiara skazanych. Rozmowy z Bogiem w więzieniu",
						getCover(""),
						2909,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Michał Rauszer",
						"Ludowy antyklerykalizm",
						getCover(""),
						3959,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Tomasz  Maćkowiak",
						"Byłam katolickim księdzem",
						getCover(""),
						3660,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Natalia Budzyńska",
						"Ja nie mam duszy. Sprawa Barbary Ubryk, uwięzionej zakonnicy, której historią żyła cała Polska",
						getCover(""),
						3017,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Jon Krakauer Jan Dzierzgowski",
						"Pod sztandarem nieba. Wiara, która zabija",
						getCover(""),
						3067,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Stanisław Obirek Artur Nowak",
						"Gomora. Władza, strach i pieniądze w polskim Kościele",
						getCover(""),
						3073,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Frederic Martel",
						"Sodoma. Hipokryzja i władza w Watykanie",
						getCover(""),
						3415,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Weronika Marczak",
						"Królewna. Rodzina Monet. Tom 2. Część 2",
						getCover(""),
						2694,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Weronika Marczak",
						"Królewna. Rodzina Monet. Tom 2. Część 1",
						getCover(""),
						2694,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Weronika Marczak",
						"Skarb. Rodzina Monet. Tom 1",
						getCover(""),
						2694,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Natalia de Barbaro",
						"Przędza. W poszukiwaniu wewnętrznej wolności",
						getCover(""),
						2983,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Książę Harry",
						"Ten drugi",
						getCover(""),
						3879,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Anita Głowińska",
						"Kicia Kocia.Wiosna",
						getCover(""),
						665,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marta Łabęcka",
						"All Of Your Flaws. Przypomnij mi naszą przeszłość. Flaw(less). Tom 2",
						getCover(""),
						2958,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marta Łabęcka",
						"Opowiedz mi naszą historię. Flaw(less). Tom 1",
						getCover(""),
						2628,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"James  Clear",
						"Atomowe nawyki. Drobne zmiany, niezwykłe efekty",
						getCover(""),
						2629,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Marta Łabęcka",
						"Despite Your (im)perfections. Dotrzymaj złożonej mi obietnicy. Flaw(less) . Tom 3",
						getCover(""),
						2958,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Natalia de Barbaro",
						"Czuła przewodniczka. Kobieca droga do siebie",
						getCover(""),
						2399,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Kelsey Hartwell",
						"11 papierowych serc",
						getCover(""),
						2321,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Rebel",
						"Frostpunk. Gra planszowa",
						getCover(""),
						35348,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Portal Games",
						"Marvel United. X-men",
						getCover(""),
						18045,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"undefined",
						"Kryptyda Lucrum Games",
						getCover(""),
						9263,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"undefined",
						"Ostry Dyżur",
						getCover(""),
						15988,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Portal Games",
						"Mindbug Portal Games",
						getCover(""),
						5596,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"undefined",
						"Najlepsza gra o psach 2 FoxGames",
						getCover(""),
						5226,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Portal Games",
						"Bitoku",
						getCover(""),
						22496,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Rebel",
						"Eksplodujące Kotki. Wersja dla 2 graczy Rebel",
						getCover(""),
						3746,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"undefined",
						"Talisman: Harry Potter (edycja polska)",
						getCover(""),
						31364,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Winning Moves",
						"Monopoly. Konie i kucyki",
						getCover(""),
						9594,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"undefined",
						"Brzdęk! Nie drażnij smoka Lucrum Games",
						getCover(""),
						20773,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Rebel",
						"Nemesis. Lockdown. Edycja polska",
						getCover(""),
						54194,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Martin Caparros Marta  Szafrańska-Brandt",
						"Głód",
						getCover(""),
						6052,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Alexandra Christo",
						"Księżniczka dusz",
						getCover(""),
						2697,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Benjamin Zephaniah",
						"Ślad natury",
						getCover(""),
						2488,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Rebecca F. Kuang",
						"Babel, czyli o konieczności przemocy",
						getCover(""),
						5825,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Michał Paweł Urbaniak",
						"Doll story",
						getCover(""),
						2910,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Natalia  Szostak",
						"Zguba",
						getCover(""),
						4162,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Barbara Supeł",
						"Urodzinowe przyjęcie. Staś Pętelka",
						getCover(""),
						2045,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Książę Harry",
						"Ten drugi",
						getCover(""),
						3879,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Max Czornyj",
						"Inkarnator. Honoriusz Mond. Tom 3",
						getCover(""),
						3062,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Chloe Gong",
						"Nikczemna fortuna. Foul Lady Fortune. Tom 1",
						getCover(""),
						3636,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Samantha Shannon",
						"Dzień nastania nocy. Tom 1",
						getCover(""),
						3828,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Alicja Sinicka",
						"Florystki",
						getCover(""),
						3499,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Jacek Bartosiak",
						"Najlepsze miejsce na świecie. Gdzie Wschód zderza się z Zachodem",
						getCover(""),
						4775,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Lucinda Riley Harry Whittaker",
						"Atlas. Historia Pa Salta. Siedem sióstr",
						getCover(""),
						4653,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Camilla Läckberg Henrik Fexeus",
						"Kult. Mentalista. Tom 2",
						getCover(""),
						4049,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Wojciech Koronkiewicz",
						"Nie zbiera się jabłek z tego sadu. Podróż do grobów, duchów i ukrytych skarbów Podlasia",
						getCover(""),
						3827,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Rebecca F. Kuang",
						"Babel, czyli o konieczności przemocy",
						getCover(""),
						7342,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Wojciech Koronkiewicz",
						"Podlasie zdrowo zakręcone. Podróż po krainie niezwykłych ludzi i zapomnianych smaków",
						getCover(""),
						4300,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(null, "Kate Stewart", "Flock", getCover(""), 3795, ZonedDateTime.now(), 0, date),
					new Book(
						null,
						"Grzegorz Kalinowski",
						"Na beczce prochu. Śmierć frajerom. Tom 5",
						getCover(""),
						2696,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"J. R. R. Tolkien",
						"Księga zaginionych opowieści. Historia Śródziemia. Tom 2",
						getCover(""),
						5561,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Diana Brzezińska",
						"Nie odkryjesz prawdy. Prokurator Gabriela Sawicka. Tom 4",
						getCover(""),
						3750,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Cassandra Clare",
						"Łańcuch z cierni. Ostatnie godziny. Tom 3",
						getCover(""),
						5900,
						ZonedDateTime.now(),
						0,
						date
					),
					new Book(
						null,
						"Katee Robert",
						"Electric Idol. Dark Olympus. Tom 2",
						getCover(""),
						4133,
						ZonedDateTime.now(),
						0,
						date
					)
				)
			);
		};
	}
}
