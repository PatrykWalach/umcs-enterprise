package com.umcs.enterprise;

import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.cover.CoverRepository;
import com.umcs.enterprise.cover.CoverService;
import com.umcs.enterprise.order.BookPurchase;
import com.umcs.enterprise.order.BookPurchaseRepository;
import com.umcs.enterprise.order.Purchase;
import com.umcs.enterprise.order.PurchaseRepository;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.imageio.ImageIO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Seed {

	@NonNull
	private final CoverRepository coverRepository;

	@NonNull
	private final PurchaseRepository purchaseRepository;

	@NonNull
	private final BookPurchaseRepository bookPurchaseRepository;

	private Cover getCover(String urls) throws IOException {
		Path uploadDir = Paths.get("static/covers");
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}

		ArrayList<String> strings = new ArrayList<>(List.of(urls.split(",")));
		Collections.reverse(strings);

		return strings
			.stream()
			.map(String::strip)
			.filter(s -> !s.isBlank())
			.map(url -> {
				try {
					String filename = url.substring(url.lastIndexOf("/") + 1);
					Path file = uploadDir.resolve(filename);

					BufferedImage image;
					if (!file.toFile().exists()) {
						image = ImageIO.read(new URL("https:" + url));

						try (OutputStream outputStream = Files.newOutputStream(file)) {
							ImageIO.write(image, url.substring(url.lastIndexOf(".") + 1), outputStream);
						}
					} else {
						image = ImageIO.read(file.toFile());
					}

					Cover cover = new Cover();

					cover.setFilename(filename);
					cover.setHeight(image.getHeight());
					cover.setWidth(image.getWidth());

					return coverRepository.save(cover);
				} catch (IOException e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(null);
	}

	@Bean
	CommandLineRunner initDatabase(BookRepository repository, CoverService coverService) {
		return args -> {
			List<Book> books = List.of(
				Book
					.newBuilder()
					.cover(
						getCover(
							"//cf-taniaksiazka.statiki.pl/images/medium/A4C/9788367406710.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/A4C/9788367406710.jpg "
						)
					)
					.title("Mężczyzna imieniem Ove")
					.author("Fredrik Backman")
					.popularity(1L)
					.price(BigDecimal.valueOf(30.43))
					.build(),
				Book
					.newBuilder()
					.cover(
						getCover(
							"//cf-taniaksiazka.statiki.pl/images/medium/90B/9788381886604.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/90B/9788381886604.jpg "
						)
					)
					.title("Na Zachodzie bez zmian. Wydanie filmowe")
					.author("Erich Maria Remarque")
					.price(BigDecimal.valueOf(23.40))
					.popularity(1L)
					.build(),
				Book
					.newBuilder()
					.cover(
						getCover(
							"//cf-taniaksiazka.statiki.pl/images/medium/38C/9788367069359.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/38C/9788367069359.jpg "
						)
					)
					.title("Zaułek koszmarów")
					.author("William Lindsay Gresham")
					.popularity(1L)
					.price(BigDecimal.valueOf(26.94))
					.build()
			);

			books = repository.saveAll(books);

			Purchase purchase = purchaseRepository.save(Purchase.newBuilder().build());

			bookPurchaseRepository.saveAll(
				books
					.stream()
					.map(b -> BookPurchase.newBuilder().book(b).purchase(purchase).build())
					.toList()
			);

			repository.saveAll(
				Arrays.asList(
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/8AE/9788375781878.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/8AE/9788375781878.jpg "
							)
						)
						.title(
							"Pakiet Wiedźmin. Tomy 1-8: Ostatnie życzenie, Miecz przeznaczenia, Krew elfów, Czas pogardy, Chrzest ognia, Wieża Jaskółki, Pani Jeziora, Sezon burz"
						)
						.author("Andrzej Sapkowski")
						.price(BigDecimal.valueOf(399.00))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/CBD/ZEST_OGIENIKREW.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/CBD/ZEST_OGIENIKREW.jpg "
							)
						)
						.title("Pakiet Ogień i krew. Części 1-2. Wydanie serialowe")
						.author("George R.R. Martin")
						.price(BigDecimal.valueOf(76.44))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/6D3/9788382025965.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/6D3/9788382025965.jpg "
							)
						)
						.title("Ogień i krew. Tom 1. Wydanie serialowe")
						.author("George R.R. Martin")
						.price(BigDecimal.valueOf(38.22))
						.build(),
					Book
						.newBuilder()
						.title("Ogień i krew. Tom 2. Wydanie serialowe")
						.author("George R.R. Martin")
						.price(BigDecimal.valueOf(38.22))
						.build(),
					Book
						.newBuilder()
						.title("Wyrwa")
						.author("Wojciech Chmielarz")
						.price(BigDecimal.valueOf(29.59))
						.build(),
					Book
						.newBuilder()
						.title("Oppenheimer. Triumf i tragedia ojca bomby atomowej")
						.author("Martin J. Sherwin Kai Bird")
						.price(BigDecimal.valueOf(62.67))
						.build(),
					Book
						.newBuilder()
						.title("DC Black Label Kraina Snów. Sandman. Tom 3")
						.author("Neil Gaiman")
						.price(BigDecimal.valueOf(50.01))
						.build(),
					Book
						.newBuilder()
						.title("DC Black Label Dom lalki. Sandman. Tom 2")
						.author("Neil Gaiman")
						.price(BigDecimal.valueOf(57.44))
						.build(),
					Book
						.newBuilder()
						.title("DC Black Label Preludia i nokturny. Sandman. Tom 1")
						.author("Neil Gaiman Sam Kieth Malcolm  Jones Iii Mike  Dringenberg")
						.price(BigDecimal.valueOf(64.88))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/E30/9788383211916.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/E30/9788383211916.jpg "
							)
						)
						.title("Wioska wdów. Szokująca historia morderczyń z wioski Nagyrév")
						.author("Patti  McCracken")
						.price(BigDecimal.valueOf(30.44))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/324/9788367639224.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/324/9788367639224.jpg "
							)
						)
						.title("Świat Słowian. Kultura, przyroda, duchowość")
						.author("Leonard J. Pełka")
						.price(BigDecimal.valueOf(38.65))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/043/9788383211909.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/043/9788383211909.jpg "
							)
						)
						.title("Pandora")
						.author("Susan  Stokes-Chapman")
						.price(BigDecimal.valueOf(31.84))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/3E1/9788328724570.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/3E1/9788328724570.jpg "
							)
						)
						.title("Pomornica")
						.author("Ewa Przydryga")
						.price(BigDecimal.valueOf(28.06))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/543/9788327163851.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/543/9788327163851.jpg "
							)
						)
						.title("Podróż w niechciane")
						.author("Danuta  Chlupowa")
						.price(BigDecimal.valueOf(29.79))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/98B/9788367426633.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/98B/9788367426633.jpg "
							)
						)
						.title("Klucz Sary")
						.author("Tatiana De Rosnay")
						.price(BigDecimal.valueOf(30.66))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/665/9788382028515.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/665/9788382028515.jpg "
							)
						)
						.title("Jutro, jutro i znów jutro")
						.author("Gabrielle Zevin")
						.price(BigDecimal.valueOf(36.61))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/BF9/9788383211862.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/BF9/9788383211862.jpg "
							)
						)
						.title("W ogrodzie czarownicy. Jak stworzyć i uprawiać magiczny roślinny zakątek")
						.author("Arin  Murphy-Hiscock")
						.price(BigDecimal.valueOf(28.94))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/F5B/9788328726239.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/F5B/9788328726239.jpg "
							)
						)
						.title("Królewna. Rodzina Monet. Tom 2. Część 2")
						.author("Weronika Marczak")
						.price(BigDecimal.valueOf(26.72))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/E56/9788383212746.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/E56/9788383212746.jpg "
							)
						)
						.title("Beltane. Rytuały, przepisy i zaklęcia na święto kwiatów")
						.author("Melanie Marquis")
						.price(BigDecimal.valueOf(24.88))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/75B/9788367510653.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/75B/9788367510653.jpg "
							)
						)
						.title("Ten drugi")
						.author("Książę Harry")
						.price(BigDecimal.valueOf(38.79))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/292/9788326841088.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/292/9788326841088.jpg "
							)
						)
						.title("Maxima Culpa. Jan Paweł II wiedział")
						.author("Ekke Overbeek")
						.price(BigDecimal.valueOf(36.65))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/943/9788382805222.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/943/9788382805222.jpg "
							)
						)
						.title("Kabalista")
						.author("Remigiusz Mróz")
						.price(BigDecimal.valueOf(33.00))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/38A/9788382952872.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/38A/9788382952872.jpg "
							)
						)
						.title("Z tej strony Sam")
						.author("Dustin  Thao")
						.price(BigDecimal.valueOf(28.25))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/B7D/9788383211855.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/B7D/9788383211855.jpg "
							)
						)
						.title("Do zakochania jeden urok. Czarownice z Inverness. Tom 1")
						.author("Ludka Skrzydlewska")
						.price(BigDecimal.valueOf(28.94))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/253/9788382027501.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/253/9788382027501.jpg "
							)
						)
						.title("Sydonia. Słowo się rzekło")
						.author("Elżbieta Cherezińska")
						.price(BigDecimal.valueOf(37.90))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/4E7/9788383212005.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/4E7/9788383212005.jpg "
							)
						)
						.title("Orszak śmierci. Bieszczadzkie demony. Tom 3")
						.author("Emilia Szelest")
						.price(BigDecimal.valueOf(24.88))
						.build(),
					Book
						.newBuilder()
						.cover(
							getCover(
								"//cf-taniaksiazka.statiki.pl/images/medium/FA2/9788328725706.jpg , //cf-taniaksiazka.statiki.pl/images/medium-x2/FA2/9788328725706.jpg "
							)
						)
						.title("Wszystko, czego ci nie mówią, gdy jesteś nastolatkiem")
						.author("Mikołaj Marcela")
						.price(BigDecimal.valueOf(25.53))
						.build(),
					Book
						.newBuilder()
						.title("Wiersze wszystkie")
						.author("Wisława Szymborska")
						.price(BigDecimal.valueOf(64.27))
						.build(),
					Book
						.newBuilder()
						.title("Trzy kobiety")
						.author("Joanna Jax")
						.price(BigDecimal.valueOf(37.92))
						.build(),
					Book
						.newBuilder()
						.title("Inkarnator. Honoriusz Mond. Tom 3")
						.author("Max Czornyj")
						.price(BigDecimal.valueOf(29.87))
						.build(),
					Book
						.newBuilder()
						.title("Najlepsze miejsce na świecie. Gdzie Wschód zderza się z Zachodem")
						.author("Jacek Bartosiak")
						.price(BigDecimal.valueOf(46.58))
						.build(),
					Book
						.newBuilder()
						.title("A jeśli jesteśmy złoczyńcami")
						.author("M I Rio")
						.price(BigDecimal.valueOf(32.67))
						.build(),
					Book
						.newBuilder()
						.title("O kocie, który ratował książki")
						.author("Sosuke Natsukawa")
						.price(BigDecimal.valueOf(35.70))
						.build(),
					Book
						.newBuilder()
						.title("Wzorowe Jajo. Food Group")
						.author("John Jory")
						.price(BigDecimal.valueOf(21.30))
						.build(),
					Book
						.newBuilder()
						.title("Akademia mądrego dziecka. Co czujesz?")
						.author("Roger Priddy")
						.price(BigDecimal.valueOf(24.83))
						.build(),
					Book
						.newBuilder()
						.title("Swiftowie")
						.author("Beth Lincoln")
						.price(BigDecimal.valueOf(37.95))
						.build(),
					Book
						.newBuilder()
						.title("Urodzinowe przyjęcie. Staś Pętelka")
						.author("Barbara Supeł")
						.price(BigDecimal.valueOf(20.45))
						.build(),
					Book
						.newBuilder()
						.title("Księga dźwięków")
						.author("Bravi Soledad")
						.price(BigDecimal.valueOf(27.84))
						.build(),
					Book
						.newBuilder()
						.title("To nadal pestka!")
						.author("Kirsten Bradley")
						.price(BigDecimal.valueOf(34.04))
						.build(),
					Book
						.newBuilder()
						.title("Franklin i ukochany kocyk")
						.author("Paulette Bourgeois")
						.price(BigDecimal.valueOf(10.97))
						.build(),
					Book
						.newBuilder()
						.title("Rok w domu")
						.author("Marta Kulesza")
						.price(BigDecimal.valueOf(42.25))
						.build(),
					Book
						.newBuilder()
						.title(
							"Zagadka zwierciadła Twardowskiego. Detektywi z Tajemniczej 5 kontra duchy. Tom 3"
						)
						.author("Marta Guzowska")
						.price(BigDecimal.valueOf(19.48))
						.build(),
					Book
						.newBuilder()
						.title("Dziewczynka, która rozmawiała z drzewami")
						.author("Natasha  Farrant Lydia Corry")
						.price(BigDecimal.valueOf(42.34))
						.build(),
					Book
						.newBuilder()
						.title("Małe Licho i krok w nieznane")
						.author("Marta Kisiel")
						.price(BigDecimal.valueOf(26.70))
						.build(),
					Book
						.newBuilder()
						.title("Pomocne bajki na codzienne troski")
						.author("Ewa  Czajka")
						.price(BigDecimal.valueOf(23.41))
						.build(),
					Book
						.newBuilder()
						.title("Maxima Culpa. Jan Paweł II wiedział")
						.author("Ekke Overbeek")
						.price(BigDecimal.valueOf(36.65))
						.build(),
					Book
						.newBuilder()
						.title("Babilon. Kryminalna historia kościoła")
						.author("Stanisław Obirek Artur Nowak")
						.price(BigDecimal.valueOf(31.57))
						.build(),
					Book
						.newBuilder()
						.title("Bielmo. Co wiedział Jan Paweł II")
						.author("Marcin Gutowski")
						.price(BigDecimal.valueOf(31.31))
						.build(),
					Book
						.newBuilder()
						.title("Zniknięty ksiądz. Moja historia")
						.author("Marcin Adamiec")
						.price(BigDecimal.valueOf(30.73))
						.build(),
					Book
						.newBuilder()
						.title("Kościół płonie")
						.author("Andrea Riccardi Augustyn Edward")
						.price(BigDecimal.valueOf(32.39))
						.build(),
					Book
						.newBuilder()
						.title("Wiara skazanych. Rozmowy z Bogiem w więzieniu")
						.author("Katarzyna Borowska")
						.price(BigDecimal.valueOf(29.09))
						.build(),
					Book
						.newBuilder()
						.title("Ludowy antyklerykalizm")
						.author("Michał Rauszer")
						.price(BigDecimal.valueOf(39.59))
						.build(),
					Book
						.newBuilder()
						.title("Byłam katolickim księdzem")
						.author("Tomasz  Maćkowiak")
						.price(BigDecimal.valueOf(36.60))
						.build(),
					Book
						.newBuilder()
						.title(
							"Ja nie mam duszy. Sprawa Barbary Ubryk, uwięzionej zakonnicy, której historią żyła cała Polska"
						)
						.author("Natalia Budzyńska")
						.price(BigDecimal.valueOf(30.17))
						.build(),
					Book
						.newBuilder()
						.title("Pod sztandarem nieba. Wiara, która zabija")
						.author("Jon Krakauer Jan Dzierzgowski")
						.price(BigDecimal.valueOf(30.67))
						.build(),
					Book
						.newBuilder()
						.title("Gomora. Władza, strach i pieniądze w polskim Kościele")
						.author("Stanisław Obirek Artur Nowak")
						.price(BigDecimal.valueOf(30.73))
						.build(),
					Book
						.newBuilder()
						.title("Sodoma. Hipokryzja i władza w Watykanie")
						.author("Frederic Martel")
						.price(BigDecimal.valueOf(34.15))
						.build(),
					Book
						.newBuilder()
						.title("Królewna. Rodzina Monet. Tom 2. Część 2")
						.author("Weronika Marczak")
						.price(BigDecimal.valueOf(26.72))
						.build(),
					Book
						.newBuilder()
						.title("Królewna. Rodzina Monet. Tom 2. Część 1")
						.author("Weronika Marczak")
						.price(BigDecimal.valueOf(26.72))
						.build(),
					Book
						.newBuilder()
						.title("Skarb. Rodzina Monet. Tom 1")
						.author("Weronika Marczak")
						.price(BigDecimal.valueOf(26.72))
						.build(),
					Book
						.newBuilder()
						.title("Przędza. W poszukiwaniu wewnętrznej wolności")
						.author("Natalia de Barbaro")
						.price(BigDecimal.valueOf(29.83))
						.build(),
					Book
						.newBuilder()
						.title("Ten drugi")
						.author("Książę Harry")
						.price(BigDecimal.valueOf(38.79))
						.build(),
					Book
						.newBuilder()
						.title("Kicia Kocia.Wiosna")
						.author("Anita Głowińska")
						.price(BigDecimal.valueOf(6.49))
						.build(),
					Book
						.newBuilder()
						.title("All Of Your Flaws. Przypomnij mi naszą przeszłość. Flaw(less). Tom 2")
						.author("Marta Łabęcka")
						.price(BigDecimal.valueOf(29.33))
						.build(),
					Book
						.newBuilder()
						.title("Opowiedz mi naszą historię. Flaw(less). Tom 1")
						.author("Marta Łabęcka")
						.price(BigDecimal.valueOf(26.06))
						.build(),
					Book
						.newBuilder()
						.title("Atomowe nawyki. Drobne zmiany, niezwykłe efekty")
						.author("James  Clear")
						.price(BigDecimal.valueOf(25.64))
						.build(),
					Book
						.newBuilder()
						.title(
							"Despite Your (im)perfections. Dotrzymaj złożonej mi obietnicy. Flaw(less) . Tom 3"
						)
						.author("Marta Łabęcka")
						.price(BigDecimal.valueOf(29.33))
						.build(),
					Book
						.newBuilder()
						.title("Czuła przewodniczka. Kobieca droga do siebie")
						.author("Natalia de Barbaro")
						.price(BigDecimal.valueOf(23.99))
						.build(),
					Book
						.newBuilder()
						.title("11 papierowych serc")
						.author("Kelsey Hartwell")
						.price(BigDecimal.valueOf(25.14))
						.build(),
					Book
						.newBuilder()
						.title("Frostpunk. Gra planszowa")
						.author("Rebel")
						.price(BigDecimal.valueOf(353.48))
						.build(),
					Book
						.newBuilder()
						.title("Marvel United. X-men")
						.author("Portal Games")
						.price(BigDecimal.valueOf(180.45))
						.build(),
					Book.newBuilder().title("Kryptyda Lucrum Games").price(BigDecimal.valueOf(92.63)).build(),
					Book.newBuilder().title("Ostry Dyżur").price(BigDecimal.valueOf(159.88)).build(),
					Book
						.newBuilder()
						.title("Mindbug Portal Games")
						.author("Portal Games")
						.price(BigDecimal.valueOf(55.96))
						.build(),
					Book
						.newBuilder()
						.title("Najlepsza gra o psach 2 FoxGames")
						.price(BigDecimal.valueOf(52.26))
						.build(),
					Book
						.newBuilder()
						.title("Bitoku")
						.author("Portal Games")
						.price(BigDecimal.valueOf(224.96))
						.build(),
					Book
						.newBuilder()
						.title("Eksplodujące Kotki. Wersja dla 2 graczy Rebel")
						.author("Rebel")
						.price(BigDecimal.valueOf(37.46))
						.build(),
					Book
						.newBuilder()
						.title("Talisman: Harry Potter (edycja polska)")
						.price(BigDecimal.valueOf(313.64))
						.build(),
					Book
						.newBuilder()
						.title("Monopoly. Konie i kucyki")
						.author("Winning Moves")
						.price(BigDecimal.valueOf(95.94))
						.build(),
					Book
						.newBuilder()
						.title("Brzdęk! Nie drażnij smoka Lucrum Games")
						.price(BigDecimal.valueOf(207.73))
						.build(),
					Book
						.newBuilder()
						.title("Nemesis. Lockdown. Edycja polska")
						.author("Rebel")
						.price(BigDecimal.valueOf(541.94))
						.build(),
					Book
						.newBuilder()
						.title("Doll story Outlet")
						.author("Michał Paweł Urbaniak")
						.price(BigDecimal.valueOf(26.45))
						.build(),
					Book
						.newBuilder()
						.title("Księżniczka dusz")
						.author("Alexandra Christo")
						.price(BigDecimal.valueOf(27.00))
						.build(),
					Book
						.newBuilder()
						.title("Koniec Światów. Sandman. Tom 8")
						.author("Mark Buckingham Neil Gaiman Harris Tony Michael  Allred")
						.price(BigDecimal.valueOf(57.67))
						.build(),
					Book
						.newBuilder()
						.title("Trzy kobiety")
						.author("Joanna Jax")
						.price(BigDecimal.valueOf(37.92))
						.build(),
					Book
						.newBuilder()
						.title("Babel, czyli o konieczności przemocy")
						.author("Rebecca F. Kuang")
						.price(BigDecimal.valueOf(57.76))
						.build(),
					Book
						.newBuilder()
						.title("Florystki")
						.author("Alicja Sinicka")
						.price(BigDecimal.valueOf(34.99))
						.build(),
					Book
						.newBuilder()
						.title("Zguba")
						.author("Natalia  Szostak")
						.price(BigDecimal.valueOf(41.62))
						.build(),
					Book
						.newBuilder()
						.title("Doll story")
						.author("Michał Paweł Urbaniak")
						.price(BigDecimal.valueOf(29.10))
						.build(),
					Book
						.newBuilder()
						.title("Dzień nastania nocy. Tom 1")
						.author("Samantha Shannon")
						.price(BigDecimal.valueOf(37.96))
						.build(),
					Book
						.newBuilder()
						.title("Inkarnator. Honoriusz Mond. Tom 3")
						.author("Max Czornyj")
						.price(BigDecimal.valueOf(29.87))
						.build(),
					Book
						.newBuilder()
						.title("Urodzinowe przyjęcie. Staś Pętelka")
						.author("Barbara Supeł")
						.price(BigDecimal.valueOf(20.45))
						.build(),
					Book
						.newBuilder()
						.title("Głód")
						.author("Martin Caparros Marta  Szafrańska-Brandt")
						.price(BigDecimal.valueOf(60.52))
						.build(),
					Book
						.newBuilder()
						.title("Najlepsze miejsce na świecie. Gdzie Wschód zderza się z Zachodem")
						.author("Jacek Bartosiak")
						.price(BigDecimal.valueOf(46.58))
						.build(),
					Book
						.newBuilder()
						.title("Atlas. Historia Pa Salta. Siedem sióstr")
						.author("Lucinda Riley Harry Whittaker")
						.price(BigDecimal.valueOf(46.53))
						.build(),
					Book
						.newBuilder()
						.title("Kult. Mentalista. Tom 2")
						.author("Camilla Läckberg Henrik Fexeus")
						.price(BigDecimal.valueOf(40.49))
						.build(),
					Book
						.newBuilder()
						.title("Babel, czyli o konieczności przemocy")
						.author("Rebecca F. Kuang")
						.price(BigDecimal.valueOf(73.42))
						.build(),
					Book
						.newBuilder()
						.title("Flock")
						.author("Kate Stewart")
						.price(BigDecimal.valueOf(37.95))
						.build(),
					Book
						.newBuilder()
						.title("Na beczce prochu. Śmierć frajerom. Tom 5")
						.author("Grzegorz Kalinowski")
						.price(BigDecimal.valueOf(26.96))
						.build(),
					Book
						.newBuilder()
						.title("Księga zaginionych opowieści. Historia Śródziemia. Tom 2")
						.author("J. R. R. Tolkien")
						.price(BigDecimal.valueOf(55.61))
						.build(),
					Book
						.newBuilder()
						.title("Nie odkryjesz prawdy. Prokurator Gabriela Sawicka. Tom 4")
						.author("Diana Brzezińska")
						.price(BigDecimal.valueOf(37.50))
						.build(),
					Book
						.newBuilder()
						.title("Łańcuch z cierni. Ostatnie godziny. Tom 3")
						.author("Cassandra Clare")
						.price(BigDecimal.valueOf(59.00))
						.build(),
					Book
						.newBuilder()
						.title("Dom bez wspomnień")
						.author("Donato Carrisi")
						.price(BigDecimal.valueOf(39.29))
						.build(),
					Book
						.newBuilder()
						.title("Electric Idol. Dark Olympus. Tom 2")
						.author("Katee Robert")
						.price(BigDecimal.valueOf(41.33))
						.build(),
					Book
						.newBuilder()
						.title("Pomornica")
						.author("Ewa Przydryga")
						.price(BigDecimal.valueOf(28.06))
						.build()
				)
			);
		};
	}
}
