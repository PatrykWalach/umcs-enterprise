package com.umcs.enterprise;

import com.cloudinary.Cloudinary;
import com.umcs.enterprise.book.Book;
import com.umcs.enterprise.book.BookRepository;
import com.umcs.enterprise.cover.Cover;
import com.umcs.enterprise.cover.CoverService;
import com.umcs.enterprise.purchase.*;
import com.umcs.enterprise.user.User;
import com.umcs.enterprise.user.UserService;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Seed {

	@NonNull
	private final PurchaseService purchaseRepository;

	@NonNull
	private final BookPurchaseRepository bookPurchaseRepository;

	@NonNull
	private final UserService userRepository;

	private Cover toCover(String url, String uuid) {
		//		try {
		//			cloudinary.uploader().upload(url, Map.of("public_id", uuid));
		//		} catch (IOException e) {
		//			return null;
		//		}
		return (Cover.newBuilder().uuid(uuid).build());
	}

	@NonNull
	private Cloudinary cloudinary;

	@Bean
	CommandLineRunner initDatabase(BookRepository repository, CoverService coverService) {
		return args -> {
			userRepository.save(
				User
					.newBuilder()
					.authorities(Collections.singletonList("ADMIN"))
					.username("admin")
					.password("admin")
					.build()
			);

			List<Book> books = List.of(
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/0F5/9788383180977.jpg",
							"9788383180977"
						)
					)
					.title("Prawdziwa katastrofa. Iga i zaczarowany miś")
					.synopsis(
						"Iga jest uroczą dziewczynką. W dniu urodzin dostaje od dziadka wyjątkowy prezent. To wspaniała przytulanka ? miś Nastka. Szybko się okazuje, że nie jest to zwykły pluszak. Od tej pory będzie on pomagał dziewczynce mierzyć się z małymi i dużymi problemami. Nadeszła wiosna ? ulubiona pora roku Igi...."
					)
					.author("Anna  Podsiadło")
					.price(BigDecimal.valueOf(26.31))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/6FF/9788383181448.jpg",
							"9788383181448"
						)
					)
					.title("Dookoła świata. Hiszpania, ole! Emi i Tajny Klub Superdziewczyn")
					.synopsis(
						"Przyjaciele z Tajnego Klubu Superdziewczyn lądują w Maladze, urokliwym śródziemnomorskim mieście, z zamiarem wylegiwania się na złocistych plażach i podziwiania malowniczych zachodów słońca. Ich plan rujnuje jednak podejrzany incydent w muzeum. Leniwe wakacje w Hiszpanii przynoszą nową tajną misję,..."
					)
					.author("Agnieszka Mielech")
					.price(BigDecimal.valueOf(24.88))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/4E9/9788328097520.jpg",
							"9788328097520"
						)
					)
					.title("Alex Neptun. Złodziej smoków")
					.synopsis(
						"Alex Neptun ma tylko jednego zaciekłego wroga, a jest nim? ocean, który przy każdej okazji próbuje wciągnąć chłopca w śmiertelną pułapkę. Alex trzyma się więc z dala od wody, co nie jest łatwe, gdy mieszka się w nadmorskim miasteczku? Wkrótce jednak będzie musiał pokonać swój lęk, by uratować..."
					)
					.author("Owen David")
					.price(BigDecimal.valueOf(24.49))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/E8C/9788383212739.jpg",
							"9788383212739"
						)
					)
					.title("Zaklinaczka burz")
					.synopsis(
						"Piękna powieść Sophie Anderson, autorki takich czarujących baśni, jak „Dom na kurzych łapach”, „Baśnie Śnieżnego Lasu” i „Zaczarowany zamek”! Linnet wraz z ojcem została zesłana na Żałosne Bagna, ponieważ Magia jest zabroniona na wyspie. Dziewczyna tęskni za swoim dawnym życiem i marzy o ponownym..."
					)
					.author("Sophie Anderson")
					.price(BigDecimal.valueOf(30.11))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/F90/9788383211299.jpg",
							"9788383211299"
						)
					)
					.title("Dom na kurzych łapach")
					.synopsis(
						"Magiczny świat, w którym budząca grozę \"Baba Jaga\" spotyka \"Anię z Zielonego Wzgórza\" Fantastyczna baśń o przeznaczeniu i sile przyjaźni oraz rodzinnych więzów. Dwunastoletnia Marinka mieszka z babcią w niewielkiej chatce. Co może być w tym niezwykłego? Cóż, nie każdy mieszka z prawdziwą Babą Jagą..."
					)
					.author("Sophie Anderson")
					.price(BigDecimal.valueOf(28.51))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/7BA/9788382654196.jpg",
							"9788382654196"
						)
					)
					.title("Kicia Kocia. Wiosna")
					.synopsis(
						"Kicia Kocia wybrała się z mamą do parku, żeby podziwiać budzącą się wiosnę, pąki na drzewach ipierwsze kwiatki. Spotkały tam Packa i jego mamę – przyjaciele razem zaczęli obserwować przyrodę. Znaleźli też chore zwierzątko, któremu udzielili pomocy."
					)
					.author("Anita Głowińska")
					.price(BigDecimal.valueOf(6.45))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/B6A/9788367335881.jpg",
							"9788367335881"
						)
					)
					.title("Wołodymyr Zełenski. Ukraina we krwi")
					.synopsis(
						"Wołodymyr Zełenski. Ukraina we krwi Kiedyś komik – dziś polityk, na którego skierowane są oczy całego świata. Wołodymyr Zełenski dorastał w żydowskiej rodzinie w górniczym miasteczku. Jest synem inżynierki i profesora cybernetyki. Miał być prawnikiem, ale wybrał świat rozrywki. Występować zaczął..."
					)
					.author("Gallagher  Fenwick")
					.price(BigDecimal.valueOf(26.94))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/A25/9788326841026.jpg",
							"9788326841026"
						)
					)
					.title("Przesłanie z Ukrainy")
					.synopsis(
						"SŁOWA CZŁOWIEKA. PRZESŁANIE NARODU. Oto zbiór najmocniejszych przemówień Wołodymyra Zełenskiego z czasu wojny (oraz jedno Ołeny Zełenskiej), osobiście przez niego wybranych, aby opowiedzieć historię narodu ukraińskiego. To opowieść o kraju mężnie broniącym się przed rosyjską agresją. To także..."
					)
					.author("Wołodymyr Zełenski")
					.price(BigDecimal.valueOf(30.24))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/5C1/9788326840142.jpg",
							"9788326840142"
						)
					)
					.title(
						"Walka naszego życia. Moja praca z Zełenskim, ukraińskie zmagania o demokrację i co to wszystko znaczy dla świata"
					)
					.synopsis(
						"Kiedy ukraińska dziennikarka Julia Mendel otrzymała telefon z propozycją pracy dla prezydenta Wołodymyra Zełenskiego, nie miała pojęcia, co ją czeka. W swojej szczerej i poruszającej relacji była sekretarz prasowa Zełenskiego opowiada historię jego nieprawdopodobnej kariery od popularnego komika do..."
					)
					.author("Julia  Mendel")
					.price(BigDecimal.valueOf(40.32))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/E30/9788383211916.jpg",
							"9788383211916"
						)
					)
					.title("Wioska wdów. Szokująca historia morderczyń z wioski Nagyrév")
					.synopsis(
						"Poprzedzona dokładnym researchem powieść o jednej z najbardziej morderczych wiosek z początku XX wieku. Horror miał miejsce w wiejskiej osadzie rolniczej na Węgrzech, w której zwykłe kobiety – żony, matki i córki – doprowadziły do śmierci co najmniej 160 mężczyzn. W centrum wydarzeń znajdowała się..."
					)
					.author("Patti  McCracken")
					.price(BigDecimal.valueOf(27.63))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/859/9788382899054.jpg",
							"9788382899054"
						)
					)
					.title("Sława zniesławia. Rozmowa rzeka")
					.synopsis(
						"Poruszająca opowieść o Krzysztofie Krawczyku dwóch najbliższych mu osób: żony, Ewy Krawczyk oraz menedżera – Andrzeja Kosmali. Obszerne, emocjonalne opisy radzenia sobie z żałobą i zainteresowaniem mediów, przedstawiające konflikt z dawnymi przyjaciółmi i dążenie do ochrony spuścizny piosenkarza..."
					)
					.author("Andrzej Kosmala Ewa Krawczyk")
					.price(BigDecimal.valueOf(32.86))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/043/9788383211909.jpg",
							"9788383211909"
						)
					)
					.title("Pandora")
					.synopsis(
						"Przepiękna, klimatyczna i trzymająca w napięciu opowieść o tajemnicach i oszustwach, miłości i spełnieniu, przeznaczeniu i nadziei. Londyn, 1799 rok. Dora Blake jest początkującą jubilerką. Mieszka z wujem w miejscu, w którym wcześniej znajdował się sklep z antykami prowadzony przez jej rodziców...."
					)
					.author("Susan  Stokes-Chapman")
					.price(BigDecimal.valueOf(31.84))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/DE6/9788367338639.jpg",
							"9788367338639"
						)
					)
					.title("Cudowne lata")
					.synopsis(
						"Jedna z 15 najlepszych powieści 2022 roku według Oprah Winfrey! Jedna z 10 najlepszych książek 2021 roku według magazynu \"Le Parisien”! Jedna ze 100 najlepszych książek 2021 roku według magazynu literackiego \"Lire”! 300 000 sprzedanych egzemplarzy w samej Francji. Finalistka Grand Prix blogerów..."
					)
					.author("Valerie Perrin")
					.price(BigDecimal.valueOf(41.68))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/DD2/9788367513227.jpg",
							"9788367513227"
						)
					)
					.title("Córka z Włoch. Utracone córki. Tom 1")
					.synopsis(
						"Pierwszy tom zaplanowanego na osiem powieści cyklu „Utracone córki”. W londyńskiej kancelarii prawnej spotyka się kilka osób. Nie łączy ich nic, dopóki każda nie otrzyma pudełka z pamiątkami znalezionego w domu zmarłej kobiety, która przed laty prowadziła placówkę dla samotnych matek. Czy będą..."
					)
					.author("Soraya Lane")
					.price(BigDecimal.valueOf(34.22))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/A29/9788326841422.jpg",
							"9788326841422"
						)
					)
					.title("Przędza. W poszukiwaniu wewnętrznej wolności")
					.synopsis(
						"Autorka „Czułej Przewodniczki”, fenomenu wydawniczego ostatnich lat, powraca z nową inspirującą książką dla kobiet. Ta książka rodzi się we mnie z mieszanki uczuć. Pierwszy jest smutek - bo patrzę z bardzo bliska na kobiety, które trwają w znieruchomieniu, chociaż, paradoksalnie, są w ciągłym..."
					)
					.author("Natalia de Barbaro")
					.price(BigDecimal.valueOf(28.99))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/034/9788308080719.jpg",
							"9788308080719"
						)
					)
					.title("Najlepsze miejsce na świecie. Gdzie Wschód zderza się z Zachodem")
					.synopsis(
						"Najnowsza publikacja Jacka Bartosiaka nie jest poświęcona USA, Chinom czy Rosji – to książka o Rzeczpospolitej i o tym, czym ona jest i jakie mamy wobec niej marzenia. Konflikt o panowanie nad światem stał się faktem. Czy Polska ma pomysł na swoją pozycję w tej rozgrywce? Z jednej strony mamy..."
					)
					.author("Jacek Bartosiak")
					.price(BigDecimal.valueOf(46.19))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/B7D/9788383211855.jpg",
							"9788383211855"
						)
					)
					.title("Do zakochania jeden urok. Czarownice z Inverness. Tom 1")
					.synopsis(
						"Jedno niewinne zaklęcie potrafi odmienić lub nawet zniszczyć życie. Margo McKenzie pochodzi z jednej z najpotężniejszych magicznych rodzin w Szkocji. Ku rozpaczy krewnych odrzuciła czary po tragicznym w skutkach rytuale z udziałem jej matki. Dziewczyna stara się prowadzić zwyczajne życie. Pracuje w..."
					)
					.author("Ludka Skrzydlewska")
					.price(BigDecimal.valueOf(28.94))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/A7B/9788382028492.jpg",
							"9788382028492"
						)
					)
					.title("Księga zaginionych opowieści. Historia Śródziemia. Tom 2")
					.synopsis(
						"Historia Śródziemia to najbardziej oczekiwany przez fanów na całym świecie (w całości ukazała się tylko po angielsku, wybrane tomy natomiast opublikowano po polsku, francusku, niemiecku, szwedzku, hiszpańsku, węgiersku i rosyjsku) dwunastotomowy cykl książek wydanych przez Christophera Tolkiena na..."
					)
					.author("J. R. R. Tolkien")
					.price(BigDecimal.valueOf(58.63))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/46B/9788383212029.jpg",
							"9788383212029"
						)
					)
					.title("Lekarz kwantowy")
					.synopsis(
						"WNIKLIWE SPOJRZENIE NA ZWIĄZEK FIZYKI KWANTOWEJ ZE ŚWIADOMOŚCIĄ I UZDRAWIANIEM Niewątpliwie zależy ci na zdrowiu, dobrym samopoczuciu oraz utrzymaniu ciała w pełni sił witalnych i w świetnej kondycji. W obliczu choroby często chwytasz się wszelkich sposobów powrotu do zdrowia i zadajesz sobie..."
					)
					.author("Amit Goswami")
					.price(BigDecimal.valueOf(28.40))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/9F3/9788367512855.jpg",
							"9788367512855"
						)
					)
					.title("Po drugiej stronie różdżki")
					.synopsis(
						"#1 na liście bestsellerów „New York Timesa” natychmiast po premierze! Bestseller „Sunday Timesa”! Fenomen Tik Toka! Najlepsza książka wspomnieniowa 2022 roku amerykańskiej sieci księgarń Barnes & Noble. Książka ze wstępem Emmy Watson. Lektura obowiązkowa dla każdego Potteromaniaka. Ulubiony czarny..."
					)
					.author("Tom Felton")
					.price(BigDecimal.valueOf(42.41))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/9B2/9788382953565.jpg",
							"9788382953565"
						)
					)
					.title("Zgłoba. Lipowo. Tom 15")
					.synopsis(
						"Do Lipowa przybywa tajemnicza kobieta. Daniel Podgórski zgadza się pomóc jej w śledztwie dotyczącym mężczyzny niesłusznie oskarżonego o cztery morderstwa. Mieszkańcy maleńkiego Puszczykowa boją się zapuszczać do lasów wokół wsi. Kryje się tam wąwóz od lat uważany za nawiedzony. W dziewiętnastym..."
					)
					.author("Katarzyna Puzyńska")
					.price(BigDecimal.valueOf(34.72))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/F27/9788328725720.jpg",
							"9788328725720"
						)
					)
					.title("Gild")
					.synopsis(
						"Złoto. Złote posadzki i ściany. Złote meble i szaty. W zamku króla Midasa – wzniesionym na grani ośnieżonych gór – złote jest wszystko! Mit  o królu Midasie powraca w nowej odsłonie. Kobieta, na której władca złożył swój złotodajny dotyk, ukaże nam mroczną stronę chciwości. Oto pierwszy tom..."
					)
					.author("Raven Kennedy")
					.price(BigDecimal.valueOf(28.40))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/38A/9788382952872.jpg",
							"9788382952872"
						)
					)
					.title("Z tej strony Sam")
					.synopsis(
						"Poruszająca opowieść dla fanów \"Gwiazd naszych wina\" Johna Greena i \"Zostawiłeś mi tylko przeszłość\" Adama Silvery. Siedemnastoletnia Julie Clarke ma wielki apetyt na życie i pełne nadziei marzenia: wyprowadzić się z małego miasteczka, studiować w college’u, spędzić lato w Japonii… A to wszystko..."
					)
					.author("Dustin  Thao")
					.price(BigDecimal.valueOf(27.95))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/4E7/9788383212005.jpg",
							"9788383212005"
						)
					)
					.title("Orszak śmierci. Bieszczadzkie demony. Tom 3")
					.synopsis(
						"W Bieszczadach licho nigdy nie śpi, a tajemnice rodzą kolejne tajemnice Sprawa morderstwa dziennikarki, której ciało zostało pogrzebane zgodnie z dawnym obrzędem wampirycznego pochówku, porusza bieszczadzką społeczność. W śledztwo angażuje się Damian, były policjant, który wciąż nie uporał się z..."
					)
					.author("Emilia Szelest")
					.price(BigDecimal.valueOf(24.88))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/733/9788366219755.jpg",
							"9788366219755"
						)
					)
					.title("Wataha Putina")
					.synopsis(
						"Witold Jurasz, autor bestsellerowych „Demonów Rosji” i Hieronim Grala w swojej książce odmalowują portret Putina i rosyjskiej elity władzy: tych, którzy z Władimirem Putinem współrządzą dzisiaj Rosją, tym samym odpowiadając za napaść na Ukrainę. Kim są „bojarzy” Putina? Mordercami z kontami w..."
					)
					.author("Hieronim Grala Witold  Jurasz")
					.price(BigDecimal.valueOf(40.19))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/E06/9788383211961.jpg",
							"9788383211961"
						)
					)
					.title("Przysięga. Hope. Tom 2")
					.synopsis(
						"Nic nie daje więcej siły do walki niż miłość. Hope sądziła, że odnalazła sens życia w Pilot Point. Powróciła do jedynego miejsca, w którym czuła się bezpieczna w dzieciństwie, poznała nowych przyjaciół, a dzięki Jaxowi znów otworzyła się na uczucia. Niewiele później jej świat niestety znów obrócił..."
					)
					.author("Monika Skabara")
					.price(BigDecimal.valueOf(24.88))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/8E8/9788328726567.jpg",
							"9788328726567"
						)
					)
					.title("Urodzony morderca")
					.synopsis(
						"Co sprawiło, że w chorym umyśle zrodził się plan zemsty? Dlaczego znany biznesmen musiał zginąć? Katarzyna Bonda wciąga czytelnika w wyrafinowaną grę, w której znajdziemy wszystko to, co powinien mieć idealny kryminał: krew, szaleństwo i skrzętnie skrywane tajemnice! W nadrzecznych chaszczach..."
					)
					.author("Katarzyna Bonda")
					.price(BigDecimal.valueOf(28.08))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/5C6/9788381916707.jpg",
							"9788381916707"
						)
					)
					.title("Martwy klif. Nina Warwiłow. Tom 6")
					.synopsis(
						"Rybacy znajdują ciało mężczyzny dryfujące u brzegów Karsiboru. To tylko wierzchołek góry lodowej: ktoś po kolei morduje wychowawców starego domu dziecka na wyspie Wolin. Trop poprowadzi Ninę Warwiłow do idyllicznej miejscowości położonej pomiędzy lasem a wodami Zalewu Szczecińskiego. W zamkniętej..."
					)
					.author("Jędrzej Pasierski")
					.price(BigDecimal.valueOf(27.06))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/FEC/9788367616874.jpg",
							"9788367616874"
						)
					)
					.title("Langer")
					.synopsis("Piotr Langer nie wymaga opisu.")
					.author("Remigiusz Mróz")
					.price(BigDecimal.valueOf(34.44))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/6EC/9788383290935.jpg",
							"9788383290935"
						)
					)
					.title("Burza")
					.synopsis(
						"Oskar nie jest taki jak inni ludzie. Potrafi zmieniać rzeczywistość. Tylko że wtedy zjawiają się ONI i próbują go zabić. Dlatego Oskar oficjalnie nie istnieje. Kiedy w jego życiu pojawia się tajemniczy chłopiec i prosi Oskara o pomoc, mężczyzna nie ma wyboru, chociaż oznacza to dla niego początek..."
					)
					.author("Mieczysław  Gorzka")
					.price(BigDecimal.valueOf(28.23))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/F5B/9788328726239.jpg",
							"9788328726239"
						)
					)
					.title("Królewna. Rodzina Monet. Tom 2. Część 2")
					.synopsis(
						"Choć ma kilkanaście lat i nie grzeszy pewnością siebie, owinęła sobie Willa, Vincenta, Dylana, Shane’a i Tony’ego wokół palca. Starsi bracia Hailie pójdą za siostrą w ogień i zrobią dla niej wszystko. A ponieważ nie ma silniejszego człowieka od tego, za którym stoi rodzina, najmłodsza z Monetów..."
					)
					.author("Weronika Marczak")
					.price(BigDecimal.valueOf(26.49))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/CF9/9788328726123.jpg",
							"9788328726123"
						)
					)
					.title("Królewna. Rodzina Monet. Tom 2. Część 1")
					.synopsis(
						"Nieważne, jakiej wagi ciężarki podniesiesz na siłowni lub jak często zapłaczesz w samotności. Siła to coś więcej, to determinacja, a ty, moja królewno, determinację masz we krwi. Jesteś Monet. Wydawałoby się, że życie młodej i poukładanej Hailie nareszcie będzie prostsze. Nastolatka powoli oswaja..."
					)
					.author("Weronika Marczak")
					.price(BigDecimal.valueOf(26.49))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/027/9788328725089.jpg",
							"9788328725089"
						)
					)
					.title("Skarb. Rodzina Monet. Tom 1")
					.synopsis(
						"Zagubiona i odnaleziona jesteś jak skarb Hailie Monet ma niespełna piętnaście lat, gdy w wypadku samochodowym traci dwie najukochańsze osoby: mamę i babcię. Ze skromnego, ale pełnego miłości i ciepła domu trafia do luksusowej willi w Pensylwanii zamieszkanej przez pięciu władczych i zdystansowanych..."
					)
					.author("Weronika Marczak")
					.price(BigDecimal.valueOf(26.49))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/A29/9788326841422.jpg",
							"9788326841422"
						)
					)
					.title("Przędza. W poszukiwaniu wewnętrznej wolności")
					.synopsis(
						"Autorka „Czułej Przewodniczki”, fenomenu wydawniczego ostatnich lat, powraca z nową inspirującą książką dla kobiet. Ta książka rodzi się we mnie z mieszanki uczuć. Pierwszy jest smutek - bo patrzę z bardzo bliska na kobiety, które trwają w znieruchomieniu, chociaż, paradoksalnie, są w ciągłym..."
					)
					.author("Natalia de Barbaro")
					.price(BigDecimal.valueOf(28.99))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/F00/9788367247054.jpg",
							"9788367247054"
						)
					)
					.title("11 papierowych serc")
					.synopsis(
						"11 utraconych tygodni. 11 szans, żeby je odzyskać. Rok temu: Życie Elli było idealne. Miała cudownych przyjaciół i niesamowitego chłopaka. Wszystko się zmieniło w momencie wypadku samochodowego. Kiedy Ella obudziła się w szpitalu, nie pamiętała ani tragicznego wydarzenia, ani tygodni przed nim. Nie..."
					)
					.author("Kelsey Hartwell")
					.price(BigDecimal.valueOf(25.14))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/D2B/9788328392670.jpg",
							"9788328392670"
						)
					)
					.title("All Of Your Flaws. Przypomnij mi naszą przeszłość. Flaw(less). Tom 2")
					.synopsis(
						"Weszła drugi raz do tej samej rzeki. Teraz próbuje w niej nie utonąć Wyjeżdżając na studia do Bostonu, Josephine nie tylko wypełniała narzuconą jej rolę perfekcyjnej córki. Ten wyjazd był także ucieczką. Od rodziny, która nie umiała albo nie chciała dać dziewczynie miłości, od małomiasteczkowej..."
					)
					.author("Marta Łabęcka")
					.price(BigDecimal.valueOf(28.03))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/A2A/9788328392687.jpg",
							"9788328392687"
						)
					)
					.title(
						"Despite Your (im)perfections. Dotrzymaj złożonej mi obietnicy. Flaw(less) . Tom 3"
					)
					.synopsis(
						"Opowiedz mi naszą historię. Jest taka piękna... Josephine i Chase wreszcie są szczęśliwi. Po wszystkich burzach, sztormach i zawieruchach, których nie szczędził im los, wciąż są razem, połączeni uczuciem, a w dorosłość wkracza właśnie najpiękniejszy owoc ich miłości... osiemnastoletnia Destiny...."
					)
					.author("Marta Łabęcka")
					.price(BigDecimal.valueOf(28.03))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/7BA/9788382654196.jpg",
							"9788382654196"
						)
					)
					.title("Kicia Kocia. Wiosna")
					.synopsis(
						"Kicia Kocia wybrała się z mamą do parku, żeby podziwiać budzącą się wiosnę, pąki na drzewach ipierwsze kwiatki. Spotkały tam Packa i jego mamę – przyjaciele razem zaczęli obserwować przyrodę. Znaleźli też chore zwierzątko, któremu udzielili pomocy."
					)
					.author("Anita Głowińska")
					.price(BigDecimal.valueOf(6.45))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/ECC/9788328377431.jpg",
							"9788328377431"
						)
					)
					.title("Opowiedz mi naszą historię. Flaw(less). Tom 1")
					.synopsis(
						"Ta dziewczyna. Ten chłopak. Nie ten czas... Dwoje licealistów z małego miasteczka nieopodal Londynu: Josephine, wzorowa dziewczyna z tak zwanego dobrego domu, oraz Chase, chłopak, o którym można powiedzieć wiele, ale nie to, że jest ideałem. Dom rodzinny Chase`a też trudno byłoby nazwać dobrym ......"
					)
					.author("Marta Łabęcka")
					.price(BigDecimal.valueOf(27.92))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/034/9788308080719.jpg",
							"9788308080719"
						)
					)
					.title("Najlepsze miejsce na świecie. Gdzie Wschód zderza się z Zachodem")
					.synopsis(
						"Najnowsza publikacja Jacka Bartosiaka nie jest poświęcona USA, Chinom czy Rosji – to książka o Rzeczpospolitej i o tym, czym ona jest i jakie mamy wobec niej marzenia. Konflikt o panowanie nad światem stał się faktem. Czy Polska ma pomysł na swoją pozycję w tej rozgrywce? Z jednej strony mamy..."
					)
					.author("Jacek Bartosiak")
					.price(BigDecimal.valueOf(46.19))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/99D/9788327730855.jpg",
							"9788327730855"
						)
					)
					.title("Życie na pełnej petardzie, czyli wiara, polędwica i miłość")
					.synopsis(
						"Najbardziej lubiany polski ksiądz w rozmowie życia Ceniony za swój autentyzm, odwagę i szczerość. Podziwiany zarówno przez katolików, jak i niewierzących. Sam o sobie mówi, że jest onkocelebrytą, czyli człowiekiem znanym z tego, że ma raka. Zanim się o tym dowiedział, wybudował hospicjum w Pucku. W..."
					)
					.author("Jan Kaczkowski Piotr Żyłka")
					.price(BigDecimal.valueOf(26.55))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/6AF/9788375797381.jpg",
							"9788375797381"
						)
					)
					.title("Atomowe nawyki. Drobne zmiany, niezwykłe efekty")
					.synopsis(
						"Atomowe nawyki to sprawdzona i poparta naukowymi dowodami metoda kształtowania dobrych przyzwyczajeń i wyzbywania się złych nawyków. Fundamentem książki jest model nawyków składający się z czterech kroków (wskazówki, pragnienia, reakcji i nagrody) oraz czterech praw zmiany zachowania, które z tych..."
					)
					.author("James  Clear")
					.price(BigDecimal.valueOf(25.43))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/99D/9788327730855.jpg",
							"9788327730855"
						)
					)
					.title("Życie na pełnej petardzie, czyli wiara, polędwica i miłość")
					.synopsis(
						"Najbardziej lubiany polski ksiądz w rozmowie życia Ceniony za swój autentyzm, odwagę i szczerość. Podziwiany zarówno przez katolików, jak i niewierzących. Sam o sobie mówi, że jest onkocelebrytą, czyli człowiekiem znanym z tego, że ma raka. Zanim się o tym dowiedział, wybudował hospicjum w Pucku. W..."
					)
					.author("Jan Kaczkowski Piotr Żyłka")
					.price(BigDecimal.valueOf(26.55))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/C2B/9788324065691.jpg",
							"9788324065691"
						)
					)
					.title("Zamiast czekać, zacznij żyć")
					.synopsis(
						"NIE TRAĆ CZASU, ZACZNIJ KOCHAĆ! Ks. Jan Kaczkowski uczył nas czułości, bliskości i miłości do drugiego człowieka. Jego słowa zawsze trafiały prosto w najdelikatniejsze struny naszej duszy i nierzadko poruszały najgłębiej ukryte lęki i pragnienia. Zdecydowanie za wcześnie zabrakło nam jego mądrego..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(33.20))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/79C/9788327711632.jpg",
							"9788327711632"
						)
					)
					.title("Dasz radę")
					.synopsis(
						"Ostatnia rozmowa z księdzem Janem Kaczkowskim. Był inspiracją dla milionów ludzi - wierzących i niewierzących. Każdy, kto go słuchał, czuł: ON mnie rozumie. Ksiądz Jan nie oferował tanich rad ani łatwych pocieszeń. Swoim życiem mówił: nie odpuszczaj sobie, a dasz radę, wstaniesz. Do końca żył na..."
					)
					.author("Jan Kaczkowski Joanna Podsadecka")
					.price(BigDecimal.valueOf(24.98))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/1FA/9788327710376.jpg",
							"9788327710376"
						)
					)
					.title("Grunt pod nogami")
					.synopsis(
						"Ksiądz Jan Kaczkowski to nie tylko Życie na pełnej petardzie, ale też setki kazań wygłoszonych w całej Polsce i poza jej granicami. Książka ta to wybór tych, które jego zdaniem najlepiej pokazują najbliższe mu tematy: wierność sumieniu, odwaga, wewnętrzna uczciwość, pokonywanie własnych słabości,..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(24.98))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/126/9788365424068.jpg",
							"9788365424068"
						)
					)
					.title("Żyć aż do końca")
					.synopsis(
						"Ks. Jan Kaczkowski nie obrażał się, gdy nazywano go onkocelebrytą. Przeciwnie - świadomie wykorzystał swoją chorobę i wiedzę zdobytą przy pracy w hospicjum, aby zwrócić naszą uwagę na rolę medycyny paliatywnej. To dzieło wieńczy książka Żyć aż do końca - rzetelny, ale też pełen ciepła i empatii..."
					)
					.author("Jan Kaczkowski Katarzyna Jabłońska")
					.price(BigDecimal.valueOf(33.59))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/081/@9788364855481_31.jpg",
							"@9788364855481_31"
						)
					)
					.title("Samarytanin, kapelusznice i kot, czyli jak skutecznie pomagać?")
					.synopsis(
						"Trzy historie z życia Jezusa, kilka współczesnych wydarzeń i jeden człowiek - to punkt wyjścia do rozmowy na bardzo aktualne tematy związane z pomaganiem, m.in.: Kiedy pomaganie ma sens? Dlaczego Jezus był Mistrzem skutecznej pomocy? Czy można pomagać \"brudnymi pieniędzmi\"? Zapraszamy Cię na..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(25.06))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/559/9788324064755.jpg",
							"9788324064755"
						)
					)
					.title("Sztuka życia bez ściemy")
					.synopsis(
						"Życie trwa tylko chwilę. Nie zepsuj tego! Ksiądz Jan Kaczkowski potrafił rozpalić serca i ostudzić emocje. Nikt tak mądrze jak on nie mówił o współczesnym Kościele, o cierpieniu, potrzebie kochania albo chociaż lubienia ludzi, którzy nas otaczają. Czy bliskości da się nauczyć? Co to znaczy być..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(33.21))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/BDC/@9788377672662_31.jpg",
							"@9788377672662_31"
						)
					)
					.title("Życie na pełnej petardzie")
					.synopsis(
						"Wielki powrót bestsellera. Niezapomniany ks. Jan Kaczkowski w rozmowie życia. W szkole nie chodził na religię. Gdy już zyskał pewność co do swojego powołania - odrzucili go jezuici (Niech żałują!). Kłopoty ze wzrokiem prawie uniemożliwiły mu święcenia (wszystko jednak skończyło się dobrze: A..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(26.92))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/2F0/@9788327703569_40.jpg",
							"@9788327703569_40"
						)
					)
					.title("Życie na pełnej petardzie czyli wiara, polędwica i miłość")
					.synopsis(
						"Wielki powrót bestsellera. Niezapomniany ks. Jan Kaczkowski w rozmowie życia. W szkole nie chodził na religię. Gdy już zyskał pewność co do swojego powołania - odrzucili go jezuici (Niech żałują!). Kłopoty ze wzrokiem prawie uniemożliwiły mu święcenia (wszystko jednak skończyło się dobrze: A..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(28.06))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/48C/9788367290203.jpg",
							"9788367290203"
						)
					)
					.title("A człowieka widzi?")
					.synopsis(
						"Nie chcieli mnie święcić. Cały czas był problem z tym moim wzrokiem. (...) Tak więc dyskutują, święcić czy nie święcić (...). No i on [ksiądz Kownacki] mówi (a mówił bardzo brzydko): ?Kurrrrde, święcić kaczkę, święcić, kaczka w porządku, święcić?. Na co arcybiskup pyta drugiego profesora, księdza..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(39.63))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/EAC/9788377672624.jpg",
							"9788377672624"
						)
					)
					.title("Życie na pełnej petardzie czyli wiara... CD")
					.synopsis(
						"Wielki powrót bestsellera. Niezapomniany ks. Jan Kaczkowski w rozmowie życia. W szkole nie chodził na religię. Gdy już zyskał pewność co do swojego powołania - odrzucili go jezuici (Niech żałują!). Kłopoty ze wzrokiem prawie uniemożliwiły mu święcenia (wszystko jednak skończyło się dobrze: A..."
					)
					.author("undefined")
					.price(BigDecimal.valueOf(25.42))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/22F/@9788364855641_31.jpg",
							"@9788364855641_31"
						)
					)
					.title("Dość Kato-lipy. Ks. Jan Kaczkowski o Jezusie celebrycie")
					.synopsis(
						"\"Nie ma zgody na to, aby katolicyzm kojarzył się z lipą.\" ks. Jan Kaczkowski\"Dość kato-lipy! Ks. Jan Kaczkowski o Jezusie celebrycie\"pomaga katolikom, którzy chcą podnieść jakość swojegożycia, żyć nieprzeciętnie i chcą być dumni z tego, że sąkatolikami, którzy są kojarzeni z dążeniem do..."
					)
					.author("Jan Kaczkowski")
					.price(BigDecimal.valueOf(25.06))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/847/5902650617582.jpg",
							"5902650617582"
						)
					)
					.title("Frostpunk. Gra planszowa")
					.synopsis(
						"Postapokaliptyczna gra strategiczna. Witaj w postapokaliptycznym świecie, gdzie los ostatnich ocalałych leży w Twoich rękach! Czy potraktujesz mieszkańców swojej kolonii jak zwyczajny zasób? Podążysz drogą inspirujących architektów, nieustraszonych odkrywców czy błyskotliwych naukowców? Twoje rządy..."
					)
					.author("Rebel")
					.price(BigDecimal.valueOf(353.49))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/5BC/5902560387131.jpg",
							"5902560387131"
						)
					)
					.title("Marvel United. X-men")
					.synopsis(
						"X-Meni wkraczają do Marvel United, popularnej, dynamicznej gry kooperacyjnej. Zbuduj drużynę zmutowanych Bohaterów i połącz siły, aby pokrzyżować plany najpotężniejszych Złoczyńców we wszechświecie. Teraz możesz także przejąć bezpośrednią kontrolę nad Superłotrem, stając do walki z Superbohaterami..."
					)
					.author("Portal Games")
					.price(BigDecimal.valueOf(149.88))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/08E/5904305400273.jpg",
							"5904305400273"
						)
					)
					.title("Kryptyda")
					.synopsis(
						"Znajdź kryptydę i zamknij usta niedowiarkom!   Kryptyda: Hipotetyczne zwierzę, którego istnienie nie zostało dowiedzione. Udało ci się przeanalizować dostępną dokumentację, połączyć kropki i zgromadzić tyle materiałów, ile tylko było możliwe. Czujesz, że jesteś blisko, a dzięki tobie świat już..."
					)
					.author("undefined")
					.price(BigDecimal.valueOf(91.89))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/C3D/5902259206620.jpg",
							"5902259206620"
						)
					)
					.title("Ostry Dyżur")
					.synopsis(
						"Jesteście lekarzami, którzy właśnie zostali zatrudnieni w świeżo otwartym, nowoczesnym Centrum Medycznym. Będziecie musieli współpracować z kolegami po fachu, aby przyjmować, diagnozować i leczyć pacjentów, którzy wymagają pomocy. Ostry Dyżur to innowacyjna, kooperacyjna gra rozgrywana w czasie..."
					)
					.author("undefined")
					.price(BigDecimal.valueOf(178.34))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/FC6/5902560387414.jpg",
							"5902560387414"
						)
					)
					.title("Mindbug")
					.synopsis(
						"Mindbug łączy w sobie przystępność i prostotę zasad ze strategiczną głębią rozbudowanej gry karcianej. Rezultatem jest oparta na umiejętnościach karciana gra pojedynkowa - powiew świeżości, który zmieni twoje spojrzenie na tego typu gry. Mimo, że Mindbug jest niezwykle łatwy do opanowania, bez..."
					)
					.author("Portal Games")
					.price(BigDecimal.valueOf(55.96))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/6CA/5907078167961.jpg",
							"5907078167961"
						)
					)
					.title("Najlepsza gra o psach 2")
					.synopsis(
						"Pies jest najlepszym przyjacielem człowieka, nic więc dziwnego, że świat pełen jest miłośników tych czworonogów! W Najlepszej grze o psach 2 będziecie rywalizować między sobą o tytuł największego psiarza. Pamiętajcie, by zapewnić swoim podopiecznym wszystko, czego potrzebują. W tym celu będziecie..."
					)
					.author("undefined")
					.price(BigDecimal.valueOf(52.26))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/B58/5902560384901.jpg",
							"5902560384901"
						)
					)
					.title("Bitoku")
					.synopsis(
						"W grze wcielicie się w duchy Bitoku, próbujące zasłużyć na przejęcie schedy po Wielkim Duchu Kniei. Z szeregu dostępnych akcji będziecie korzystać na przestrzeni zaledwie 4 rund, dlatego też Bitoku cechuje się krótką kołderką, sałatką punktową i sporą ilością negatywnej interakcji, kiedy to..."
					)
					.author("Portal Games")
					.price(BigDecimal.valueOf(189.98))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/F7B/810083041841.jpg",
							"810083041841"
						)
					)
					.title("Eksplodujące Kotki. Wersja dla 2 graczy")
					.synopsis(
						"Spróbuj swoich sił w tej wysoce strategicznej, w pełni bezpiecznej wersji rosyjskiej ruletki! Podczas rozgrywek gracze co turę dobierają po jednej karcie, aż do momentu, gdy któryś trafi na puchatą bombę. Ów nieszczęśnik rozlatuje się na tysiąc kawałeczków i wypada z gry. Pozostałe karty służą do..."
					)
					.author("Rebel")
					.price(BigDecimal.valueOf(39.96))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/B01/5902259206637.jpg",
							"5902259206637"
						)
					)
					.title("Talisman: Harry Potter (edycja polska)")
					.synopsis(
						"Przeżyj niesamowitą przygodę i odkryj moc przyjaźni, lojalności i prawdziwej magii w tej nowej edycji gry Talisman! W grze Talisman: Harry Potter Lord Voldemort oczekuje od wiernych mu najpotężniejszych czarownic i czarodziejów dowodów na ich całkowite posłuszeństwo! Wcielcie się w role odważnych..."
					)
					.author("undefined")
					.price(BigDecimal.valueOf(313.64))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/2A0/5036905032995.jpg",
							"5036905032995"
						)
					)
					.title("Monopoly. Konie i kucyki")
					.synopsis(
						"Monopoly Konie i kucyki Monopoly Konie i kucyki to rodzinna gra planszowa, która spodoba się wszystkim fanom czworonożnych przyjaciół. Poznaj i zdobądź różne gatunki koni i kucyków, aby wygrać grę! Na wykupionych polach buduj boksy i stajnie. Przemieszczaj się po planszy wyjątkowymi pionkami..."
					)
					.author("Winning Moves")
					.price(BigDecimal.valueOf(95.94))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/B36/5902650617070.jpg",
							"5902650617070"
						)
					)
					.title("Nemesis. Lockdown. Edycja polska")
					.synopsis(
						"Czy w obliczu nadciągającego horroru zaufasz swoim towarzyszom? Za każdym razem, gdy przykładam ucho do grudki czerwonej skały, słyszę jakieś odgłosy. Jądro Marsa rozbrzmiewa dźwiękami przepełnionymi głodem i wściekłością. Wkrótce przebudzimy mściwe duchy, które niegdyś zamieszkiwały tę planetę......"
					)
					.author("Rebel")
					.price(BigDecimal.valueOf(541.94))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/711/824968211359.jpg",
							"824968211359"
						)
					)
					.title("Wsiąść do Pociągu Widmo")
					.synopsis(
						"Witaj w Koszmarkowie – miejscu pełnym strasznych sekretów! Sprawdź, co kryje się w zakurzonych zakamarkach posiadłości na wzgórzu, wybierz się na spacer po przerażającym bagnie, a jeśli masz dość odwagi, sprawdź, czy w ukrytej jaskini naprawdę mieszka wiedźma! Wsiąść do Pociągu Widmo to łatwa do..."
					)
					.author("Rebel")
					.price(BigDecimal.valueOf(112.86))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/368/9788324065813.jpg",
							"9788324065813"
						)
					)
					.title("Potworem rodzisz się albo stajesz")
					.synopsis(
						"Przez całe życie walczyła z potworami, ale nikt nie przygotował jej na okrucieństwo ludzi, którzy staną na jej drodze. Koral każdego dnia ryzykuje życiem, aby zapewnić swojej rodzinie bezpieczeństwo i zdobyć środki na leczenie swojej młodszej siostry Lirii. Morskie bestie, na które poluje,..."
					)
					.author("Tanvi Berwah")
					.price(BigDecimal.valueOf(38.09))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/4C4/9788383290478.jpg",
							"9788383290478"
						)
					)
					.title("Na beczce prochu. Śmierć frajerom. Tom 5")
					.synopsis(
						"Bestsellerowy cykl „Śmierć frajerom” w kolejnej odsłonie. Heniek rusza w przymusową, pełną przygód i romansów podróż po Europie. Zaczyna od zaokrętowania na transatlantyk Batory i wyruszenia w dziewiczy rejs legendarnego statku. Na pokładzie spotyka szkolnego kolegę – Tadeusza Meissnera, oraz..."
					)
					.author("Grzegorz Kalinowski")
					.price(BigDecimal.valueOf(24.82))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/5B7/9788328391581.jpg",
							"9788328391581"
						)
					)
					.title("Blizny zapisane w duszy. Scars. Tom 2")
					.synopsis(
						"Ona za bardzo siebie nienawidziła, on zbyt mocno tęsknił za swoją dawną miłością Vafara i Royce spotkali się w momencie, w którym żadne z nich nie było gotowe na szczęście. Ona miała rodzinne kłopoty, on nie radził sobie z żałobą po ukochanej Maxine. Oboje mieli sporo do przepracowania. Dlatego,..."
					)
					.author("FortunateEm")
					.price(BigDecimal.valueOf(30.16))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/BDE/9788367016520.jpg",
							"9788367016520"
						)
					)
					.title("Mein Gott, jak pięknie")
					.synopsis(
						"W maju 1787 roku do portu w Swinemünde przybija statek z sekretnym ładunkiem. Chwilę później szyper Winfried Koschke zaczyna zdradzać pierwsze oznaki obłędu. Krótko po powrocie ze swojej wielkiej podróży do Ameryki Południowej słynny Alexander von Humboldt staje na szczycie Rosengarten i mówi, że..."
					)
					.author("Filip Springer")
					.price(BigDecimal.valueOf(30.74))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/14E/9788328157019.jpg",
							"9788328157019"
						)
					)
					.title("Nowe sąsiedztwo. Batman Detective Comics. Tom 1")
					.synopsis(
						"Pierwszy tom docenionego przez krytyków cyklu opowieści o Batmanie „Detective Comics” ukazującego się w ramach linii wydawniczej Uniwersum DC. W Gotham zachodzą poważne zmiany, a nowy burmistrz Christopher Nakano chce się pozbyć Batmana. Mroczny Rycerz opuszcza posiadłość Wayne’ów i Jaskinię..."
					)
					.author("Mariko Tamaki Dan Mora")
					.price(BigDecimal.valueOf(51.77))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/917/9788328170018.jpg",
							"9788328170018"
						)
					)
					.title("Rzymianie obejdą się smakiem! Idefiks i Nieugięci. Tom 2")
					.synopsis(
						"Jest 52 rok p.n.e. Cała Lutecja jest podbita przez Rzymian… Cała? Nie! Grupa nieugiętych zwierząt dowodzona przez Idefiksa nadal opiera się najeźdźcy! Po pokonaniu galijskiego przywódcy Kamulogena przez generała Labienusa dzielny piesek Idefiks organizuje ruch oporu, żeby bronić galijskiego miasta..."
					)
					.author(
						"Philippe Fenech Herve Benedetti Jean  Bastide Michel  Coulon Robin  Nicolas Simon  Lecocq"
					)
					.price(BigDecimal.valueOf(20.63))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/F50/9788383212708.jpg",
							"9788383212708"
						)
					)
					.title("Sparrow")
					.synopsis(
						"Czy odnajdzie wolność i szczęście w tej złotej klatce? Sparrow w najgorszych chwilach nie podejrzewała, że jej życie potoczy się w taki sposób. O Troyu Brennanie wiedziała tyle, co wszyscy mieszkańcy Bostonu – że nie należy z nim zadzierać. Był synem największego gangstera w mieście i ciężko..."
					)
					.author("L. J. Shen")
					.price(BigDecimal.valueOf(27.78))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/324/9788367639224.jpg",
							"9788367639224"
						)
					)
					.title("Świat Słowian. Kultura, przyroda, duchowość")
					.synopsis(
						"Unikatowe materiały o kulturze i wierzeniach dawnych Słowian autora książki Polska demonologia ludowa Dr Leonard J. Pełka, niekwestionowany autorytet w dziedzinie badań nad słowiańszczyzną, oprócz pisania książek zajmował się również publicystyką. Niniejsza pozycja to zbiór jego tekstów, które w..."
					)
					.author("Leonard J. Pełka")
					.price(BigDecimal.valueOf(37.59))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/8FD/9788367513296.jpg",
							"9788367513296"
						)
					)
					.title("Czynnik królika")
					.synopsis(
						"Antti Tuomainen powraca z pierwszą częścią trylogii, której skazaną na sukces ekranizację przygotowuje spółka Amazon Studios. W roli głównej zagra Steve Carrell! Starannie uporządkowane życie matematyka ubezpieczeniowego wywraca się do góry nogami, gdy niespodziewanie traci pracę i dziedziczy park..."
					)
					.author("Antti Tuomainen")
					.price(BigDecimal.valueOf(33.67))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/FC6/9788328166172.jpg",
							"9788328166172"
						)
					)
					.title("Skulldigger i Kostek. Czarny Młot")
					.synopsis(
						"Kolejna, tym razem bardzo mroczna i nawiązująca do stylistyki batmanowskiej, opowieść rozgrywająca się w świecie wyróżnionej Nagrodą Eisnera serii o Czarnym Młocie. W Spiral City szerzą się zbrodnia, korupcja i przemoc, na szczęście miasto nie pozostaje całkowicie bezbronne: czarne charaktery muszą..."
					)
					.author("Jeff Lemire Tonci Zonjic")
					.price(BigDecimal.valueOf(45.50))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/292/9788326841088.jpg",
							"9788326841088"
						)
					)
					.title("Maxima Culpa. Jan Paweł II wiedział")
					.synopsis(
						"Czy Karol Wojtyła wiedział o pedofilii wśród księży, zanim został papieżem? Czy pomagał im uniknąć odpowiedzialności? Odpowiedź niestety brzmi: tak. Znał problem z czasów, kiedy był arcybiskupem krakowskim. Jeszcze jako biskup krakowski Karol Wojtyła nieraz stał oko w oko z księżmi, którzy..."
					)
					.author("Ekke Overbeek")
					.price(BigDecimal.valueOf(36.34))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/52E/9788381353045.jpg",
							"9788381353045"
						)
					)
					.title("Nie odkryjesz prawdy. Prokurator Gabriela Sawicka. Tom 4")
					.synopsis(
						"Do prokuratury zgłasza się Antoni Madej. Twierdzi, że jest seryjnym zabójcą prostytutek. Chce rozmawiać tylko z Sawicką. Robi wszystko, by oskarżono go o te morderstwa. Prokurator od początku nie wierzy w jego winę. Zastanawia się, kto za tym stoi, dlaczego mężczyźnie tak zależy na uznaniu go za..."
					)
					.author("Diana Brzezińska")
					.price(BigDecimal.valueOf(36.90))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/99D/9788327730855.jpg",
							"9788327730855"
						)
					)
					.title("Życie na pełnej petardzie, czyli wiara, polędwica i miłość")
					.synopsis(
						"Najbardziej lubiany polski ksiądz w rozmowie życia Ceniony za swój autentyzm, odwagę i szczerość. Podziwiany zarówno przez katolików, jak i niewierzących. Sam o sobie mówi, że jest onkocelebrytą, czyli człowiekiem znanym z tego, że ma raka. Zanim się o tym dowiedział, wybudował hospicjum w Pucku. W..."
					)
					.author("Jan Kaczkowski Piotr Żyłka")
					.price(BigDecimal.valueOf(25.21))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/059/9788324066421.jpg",
							"9788324066421"
						)
					)
					.title(
						"Co ze mną nie tak? O życiu w dysfunkcyjnym domu, środowisku w Polsce i o tym, jak sobie z tym (nie) radzimy"
					)
					.synopsis(
						"Co jest ze mną nie tak, jak sobie z tym poradzić i mieć wystarczająco dobre życie? \"Co ze mną nie tak?\" to pytanie, które terapeutka Joanna Flis często słyszy w swoim gabinecie od pacjentów. \"Tworzę udany związek, a czuję się niekochana\". \"Mam sukcesy w życiu zawodowym, a uważam siebie za osobę..."
					)
					.author("Joanna  Flis")
					.price(BigDecimal.valueOf(35.40))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/FEC/9788367616874.jpg",
							"9788367616874"
						)
					)
					.title("Langer")
					.synopsis("Piotr Langer nie wymaga opisu.")
					.author("Remigiusz Mróz")
					.price(BigDecimal.valueOf(34.44))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/B42/9788381438704.jpg",
							"9788381438704"
						)
					)
					.title("Kult. Mentalista. Tom 2")
					.synopsis(
						"Kult to druga część trylogii autorskiego duetu Läckberg & Fexeus. W sztokholmskiej dzielnicy Södermalm uprowadzony zostaje pięcioletni Ossian. Natychmiast rusza dochodzenie. Do policyjnego zespołu Miny Dabiri dołącza negocjator Adam Blom, który dostrzega podobieństwa do wcześniejszej sprawy –..."
					)
					.author("Camilla Läckberg Henrik Fexeus")
					.price(BigDecimal.valueOf(38.72))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/8E8/9788328726567.jpg",
							"9788328726567"
						)
					)
					.title("Urodzony morderca")
					.synopsis(
						"Co sprawiło, że w chorym umyśle zrodził się plan zemsty? Dlaczego znany biznesmen musiał zginąć? Katarzyna Bonda wciąga czytelnika w wyrafinowaną grę, w której znajdziemy wszystko to, co powinien mieć idealny kryminał: krew, szaleństwo i skrzętnie skrywane tajemnice! W nadrzecznych chaszczach..."
					)
					.author("Katarzyna Bonda")
					.price(BigDecimal.valueOf(28.08))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/F8C/9788367512695.jpg",
							"9788367512695"
						)
					)
					.title("Atlas. Historia Pa Salta. Siedem sióstr")
					.synopsis(
						"Długo wyczekiwany finał najpopularniejszej na świecie serii obyczajowej „Siedem sióstr”! Historia siedmiu sióstr została odkryta. Ale sekrety z przeszłości ich ojca wciąż mogą zmienić ich przyszłość… Paryż, rok 1928. Pewna rodzina przyjmuje pod swój dach przypadkowo znalezionego, wycieńczonego..."
					)
					.author("Lucinda Riley Harry Whittaker")
					.price(BigDecimal.valueOf(46.71))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/9B2/9788382953565.jpg",
							"9788382953565"
						)
					)
					.title("Zgłoba. Lipowo. Tom 15")
					.synopsis(
						"Do Lipowa przybywa tajemnicza kobieta. Daniel Podgórski zgadza się pomóc jej w śledztwie dotyczącym mężczyzny niesłusznie oskarżonego o cztery morderstwa. Mieszkańcy maleńkiego Puszczykowa boją się zapuszczać do lasów wokół wsi. Kryje się tam wąwóz od lat uważany za nawiedzony. W dziewiętnastym..."
					)
					.author("Katarzyna Puzyńska")
					.price(BigDecimal.valueOf(34.72))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/009/9788396465825.jpg",
							"9788396465825"
						)
					)
					.title("Zakazana prawda. Ucieczka od wolności")
					.synopsis(
						"Przełomowa książka majora ABW Tomasza Budzyńskiego i dziennikarza śledczego Wojciecha Sumlińskiego, ujawniająca, co niejawne i nigdy nieujawniane – fakty tak nieprawdopodobnie prawdziwe, że całkowicie zmieniające sposób patrzenia na naszą rzeczywistość i w oparciu o logikę, trudne do pojęcia. Jak..."
					)
					.author("Tomasz Budzyński Wojciech Sumliński")
					.price(BigDecimal.valueOf(30.87))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/52E/9788381353045.jpg",
							"9788381353045"
						)
					)
					.title("Nie odkryjesz prawdy. Prokurator Gabriela Sawicka. Tom 4")
					.synopsis(
						"Do prokuratury zgłasza się Antoni Madej. Twierdzi, że jest seryjnym zabójcą prostytutek. Chce rozmawiać tylko z Sawicką. Robi wszystko, by oskarżono go o te morderstwa. Prokurator od początku nie wierzy w jego winę. Zastanawia się, kto za tym stoi, dlaczego mężczyźnie tak zależy na uznaniu go za..."
					)
					.author("Diana Brzezińska")
					.price(BigDecimal.valueOf(36.90))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/64D/9788324087921.jpg",
							"9788324087921"
						)
					)
					.title("Wielkie żniwa. Jak PiS ukradł Polskę")
					.synopsis(
						"Polska jest chora, trawi ją zło W książce tej spisaliśmy czyny i rozmowy, przywołaliśmy autentyczne sytuacje i dokumenty, pokazaliśmy miejsca zdarzeń i ludzi, którzy w nich uczestniczyli. Nie było łatwo. W państwie PiS dostęp do informacji jest bowiem reglamentowany - tak jak dostęp do wpływów,..."
					)
					.author("Michał Szczerba Dariusz  Joński")
					.price(BigDecimal.valueOf(44.27))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/7E6/9788382525960.jpg",
							"9788382525960"
						)
					)
					.title("Układ")
					.synopsis(
						"Radosław Grot – były komisarz wrocławskiego wydziału zabójstw, a obecnie prywatny detektyw w szponach alkoholizmu – otrzymuje sprawę, która będzie miała wpływ na jego dalsze życie. Po pomoc do niego zgłasza się kobieta poszukująca zaginionego brata, dziennikarza śledczego, który w ostatnim czasie..."
					)
					.author("Piotr Kościelny")
					.price(BigDecimal.valueOf(29.51))
					.build(),
				Book
					.newBuilder()
					.cover(
						toCover(
							"https://bigimg.taniaksiazka.pl/images/popups/6C1/9788367353441.jpg",
							"9788367353441"
						)
					)
					.title("Łańcuch z cierni. Ostatnie godziny. Tom 3")
					.synopsis(
						"Cordelia Carstairs straciła wszystko, co było dla niej ważne. W ciągu zaledwie kilku tygodni zamordowano jej ojca, uniemożliwiono stworzenie więzi parabatai z najlepszą przyjaciółką, Lucie, a małżeństwo z Jamesem Herondale’em rozpadło się na jej oczach. Co jeszcze gorsze, jest w tej chwili..."
					)
					.author("Cassandra Clare")
					.price(BigDecimal.valueOf(59.00))
					.build()
			);

			repository.saveAll(books);

			var purchases = purchaseRepository.saveAll(
				Stream.generate(Purchase.newBuilder()::build).limit(20).toList()
			);

			Random random = new Random(0);

			bookPurchaseRepository.saveAll(
				purchases
					.stream()
					.sequential()
					.flatMap(purchase ->
						books
							.stream()
							.sequential()
							.skip(random.nextInt(purchases.size()))
							.limit(random.nextInt(purchases.size()))
							.map(book -> {
								book.setPopularity(Optional.ofNullable(book.getPopularity()).orElse(0L) + 1L);

								return BookPurchase
									.newBuilder()
										.databaseId(new BookPurchaseId(	purchase.getDatabaseId(),
book.getDatabaseId()

										))
									.book(repository.save(book))
									.purchase(purchase)
									.build();
							})
					)
					.toList()
			);
		};
	}
}
