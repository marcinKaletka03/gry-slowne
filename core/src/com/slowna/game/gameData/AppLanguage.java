package com.slowna.game.gameData;

import com.badlogic.gdx.math.MathUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum AppLanguage {
    PL(new Locale("pl","PL"),
            "POLSKI",
            "Nowa gra",
            "Ranking",
            "Ustawienia",
            "Wróć",
            "Wyjście",
            "Zatwierdź",
            "Tura",
            "Wymień",
            "Przywróć",
            "Wprowadź żądane słowo",
            "Nie znaleziono wybranego słowa",
            "Podane słowo jest za krótkie lub za długie",
            "Nowicjusz",
            "Adept",
            "Mistrz",
            "Wymień całość",
            "Pauza",
            "Udało się!",
            "Porażka",
            "Wynik",
            "Gracz",
            "Literki muszą znajdować się w jednej lini",
            "Twoja kolej",
            "Kolej Oponenta",
            "W słowie nie mogą znajdować się przerwy",
            "Nie znaleziono wybranego słowa",
            "Pierwsze słowo musi być ułożone w centrum",
            "Nowe słowo musi przylegać do pozostałych",
            "Wł dźwięk",
            "Wył dźwięk"),
    EN(Locale.ENGLISH,
            "ENGLISH",
            "Start game",
            "High scores",
            "Settings",
            "Back",
            "Exit",
            "Accept",
            "Turn",
            "Swap",
            "Restore",
            "Enter your word",
            "The word not found",
            "Word is to short or too long",
            "Easy",
            "Medium",
            "Hard",
            "Swap all",
            "Pause",
            "You won!",
            "Try again",
            "Score",
            "Player",
            "Letters must be put in one line",
            "Your Turn",
            "Opponent Turn",
            "Word must be put without brakes",
            "Word not found",
            "First world must be put in center of table",
            "New word must be append to existing words",
            "Sound on",
            "Sound off");

    public final Locale locale;
    public final String languageName;
    public final String startGame;
    public final String high_scores;
    public final String settings;
    public final String back;
    public final String exit;
    public final String accept;
    public final String turn;
    public final String swap;
    public final String restore;
    public final String put_word;
    public final String word_not_found;
    public final String bad_length;
    public final String easy;
    public final String medium;
    public final String hard;
    public final String swapAll;
    public final String pause;
    public final String won;
    public final String tryAgain;
    public final String scores;
    public final String you;
    public final String fieldsNotification;
    public final String yourTurn;
    public final String opponentTurn;
    public final String breakesInWord;
    public final String wordNotExists;
    public final String firstWord;
    public final String newWord;
    public final String soundOn;
    public final String soundOff;

    AppLanguage(Locale locale, String languageName, String startGame, String highScores, String settings, String back, String exit, String accept, String turn, String swap, String restore, String putWord, String wordNotFound, String badLength, String easy, String medium, String hard, String swapAll, String pause, String won, String tryAgain, String scores, String you, String fieldsNotification, String yourTurn, String opponentTurn, String breakesInWord, String wordNotExists, String firstWord, String newWord, String soundOn, String soundOff) {
        this.locale = locale;
        this.languageName = languageName;
        this.startGame = startGame;
        this.high_scores = highScores;
        this.settings = settings;
        this.back = back;
        this.exit = exit;
        this.accept = accept;
        this.turn = turn;
        this.swap = swap;
        this.restore = restore;
        this.put_word = putWord;
        this.word_not_found = wordNotFound;
        this.bad_length = badLength;
        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
        this.swapAll = swapAll;
        this.pause = pause;
        this.won = won;
        this.tryAgain = tryAgain;
        this.scores = scores;
        this.you = you;
        this.fieldsNotification = fieldsNotification;
        this.yourTurn = yourTurn;
        this.opponentTurn = opponentTurn;
        this.breakesInWord = breakesInWord;
        this.wordNotExists = wordNotExists;
        this.firstWord = firstWord;
        this.newWord = newWord;
        this.soundOn = soundOn;
        this.soundOff = soundOff;
    }

    public static AppLanguage ofLocaleOrEn(Locale locale) {
        return Arrays.stream(values())
                .filter(s -> s.locale.getLanguage().equals(locale.getLanguage()))
                .findFirst()
                .orElse(AppLanguage.EN);
    }

    public String getRandomName() {
        return Names.valueOf(this.name()).getRandomName();
    }

    private enum Names {
        PL(Arrays.asList("Ada", "Gracja", "Ruta", "Sabina", "Alina", "�aneta", "Andrzej", "Błażej", "Tamara", "Nina", "Yolo", "Szymon", "Myka", "Julian", "Lucja", "Sara", "Aaron", "Makary", "Oskar", "Paula", "KSSSss", "Leon", "Konrad", "Kaha", "Eliza", "Zoe", "Diana", "Edwin", "�ukasz", "Zofia", "Horacy", "Natan", "Edmund", "Romeo", "Ofelia", "Wanesa", "Monika", "Lilia", "Krzychu", "August", "Regina", "Angela", "Micha�", "Aurora", "Robert", "Leila", "Jozue", "Donald", "Efraim", "Jan", "Elwira", "Anatol", "Aleksy", "Tomasz", "Daria", "Julia", "Jeremi", "Cezary", "Larysa", "Jasiu", "Izydor", "Filip", "Daniel", "Jerzy", "Dawid", "Ilona", "Dorian", "Ignacy", "Arnold", "Liza", "Alwar", "Greta", "Cyra", "Eliasz", "Dina", "Rudolf", "Gloria", "Edyta", "Alan", "Anna", "Emilia", "Artur", "Dorota", "Nestor", "R�a", "Olaf", "Lea", "Jakub", "Nikola", "Maria", "Lidia", "Justyn", "As", "Henryk", "Miron", "Feliks", "Luiza", "Leonid", "Patryk", "Maja", "Sylwia", "Herman", "Apollo", "Joanna", "Hanna", "Paweł", "Piotr", "Teresa", "Kornel", "Nazary", "Edward", "Noemi", "Oliwia", "Ludwik", "Osmund", "Cyryl", "Roger", "Agata", "Oleg", "Alfred", "Ksenia", "Brajan", "Klara", "Emil", "Julita", "Agaton", "Laura", "Rafael", "Gerald", "Borys", "Erwin", "Karol", "Walery", "Albert", "Wanda", "Ida", "Roland", "Izolda", "Arkady", "Damian", "Hilary", "Aida", "Wac�aw", "Maryna", "Wiktor", "Helena", "Platon", "Filipa", "Wiara", "Rebeka", "�ucja", "Walter", "Kira", "Maksym", "Prokop", "Samuel", "Renata", "Janusz", "Harald", "Alicja", "Bazyli", "Igor", "Hubert", "Magnus", "Onufry", "Antoni", "Adrian", "Stella", "Marek", "Fabian", "Bruno", "Cyrus", "Hugo", "Modest", "Amanda", "�azarz", "Olga", "Stefan", "Adam", "J�zef", "Samson", "Lucjan", "Edgar", "Irma", "Ernest", "Berta", "Roman", "Estera", "Erast", "Irena", "Chloe", "Janina", "Marcin", "Kamila", "Efrem", "Ewa", "Janusz", "Marta")),
        EN(Arrays.asList("Somer", "Edmund", "Larry", "Nery", "Suzann", "Rickie", "Donte", "Delsie", "Diego", "Andres", "Rona", "Lance", "Jason", "Michal", "Tai", "Rivka", "Sandra", "Kirby", "George", "Sindy", "Isobel", "Jamaal", "Leo", "Jordan", "Adolph", "Foster", "Trista", "Dalila", "Bart", "Milo", "Callie", "Daryl", "Thomas", "Cleora", "Billie", "Denis", "Adelia", "Barry", "Chris", "Garth", "Reyna", "Saran", "Diedre", "Sook", "Doug", "Alysia", "Leslie", "Hedwig", "Na", "Richie", "Jospeh", "Kellee", "Milton", "Blake", "Darci", "Malik", "Tommy", "Muriel", "Lenny", "Yael", "Erick", "Ying", "Sharee", "Miguel", "Lloyd", "Dayle", "Autumn", "Miki", "Melony", "Rod", "Harlan", "Carman", "Isaiah", "Riley", "Tiana", "Lynn", "Holli", "Irma", "Hung", "Craig", "Rina", "Lauran", "Cammie", "Mercy", "Carlos", "Isreal", "Rory", "Michel", "Samuel", "Alene", "Alan", "Kermit", "Apryl", "Gerard", "Tad", "Danita", "Arnold", "Quinn", "Robbie", "King", "Cordie", "Eugene", "Alfred", "Derick", "Boris", "Dean", "Romeo", "Dawne", "Page", "Tomas", "Terra", "Allen", "Fabian", "Reta", "Luna", "Lane", "Mary", "Moshe", "Stacie", "Darryl", "Eddie", "Catina", "Nelson", "Tora", "Shara", "Trudi", "Mitch", "Joanna", "Ethan", "Marg", "Elisha", "Paris", "Mitsue", "Luis", "Myles", "Pat", "Sherri", "Fatima", "Gerry", "Deanna", "Dylan", "Ronny", "Armida", "Joel", "Porter", "Melda", "Cuc", "Delmer", "Jeremy", "Shon", "Lonna", "Chara", "Merlyn", "Odette", "Dina", "Gilda", "Dawn", "Logan", "Taryn", "Ty", "Opal", "Julian", "Angelo", "Julius", "Arthur", "Jacqui", "Aleida", "Kena", "Ozzie", "Leroy", "Ling", "Edgar", "Alva", "Ida", "Karena", "Yasuko", "Devin", "Carrol", "Jay", "Kenton", "James", "Lenard", "Berry", "Don", "Gale", "Will", "Collin", "Tesha", "Vernie", "Nieves", "Pearle", "Aura", "Lacy", "Bennie", "Matt", "Cheri", "Johnie", "Clark", "Burton", "Keith", "Sal", "Gema", "Hye", "Tamica", "Garret", "Dick", "Son", "Ione", "Julio", "Milda", "Annita", "Elidia", "Gary", "Genna", "Van", "Odis", "Floyd", "Mauro", "Brock", "Mellie", "Grant", "Sunday", "Sammy", "Evelyn", "Marty", "Asha", "Mariel", "Ira", "Zaida", "Ernie", "Carey", "Rafael", "Manuel", "Nydia", "Nick", "Major", "Honey", "Delcie", "Harris", "Vickie", "Ronald", "Rocco", "Austin", "Carli", "Wilber", "Verona", "Tilda", "Palmer", "Bibi", "Devona", "Eric", "Dorthy", "Tiny", "Frank", "Dudley", "Regine", "Daren", "Bari", "Noriko", "Prince", "Dewey", "Deann", "Elvera", "Vern", "Dexter", "Ahmed", "Kareen", "Jerrod", "Vaughn", "Brooks", "Ray", "Kent", "Zora", "Theron", "Bryon", "Cecily", "Anabel", "Chuck", "Mertie", "Selene", "Danika", "Essie", "Clare", "Hoa", "Rikki", "Fausto", "Kory", "Otto", "Nikita", "Eda", "Elana", "Otha", "Booker", "Nigel", "Blanca", "Colby", "Harley", "Tawnya", "Clara", "Silas", "Dirk", "Louisa", "Maisie", "Vanesa", "Terry", "Lanell", "Gil", "Anton", "Larue", "Sheryl", "Hai", "Amos", "Elenor", "Daysi", "Mel", "Ruben", "Donya", "Louise", "Ollie", "Kelvin", "Abe", "Concha", "Cesar", "Kelley", "Norris", "Jae", "Marco", "Kyle", "Trish", "Roxie", "Tyesha", "Archie", "Alex", "Dusty", "Elois", "Robert", "Delmar", "Merle", "Bert", "Norma", "Maria", "Jeff", "Scott", "Justin", "Nolan", "Dwayne", "Kip", "Stevie", "Shelby", "Gus", "Nedra", "Theda", "Earl", "Royal", "Mario", "Waldo", "Debbie", "Ron", "Jame", "Glynda", "Jamila", "Noelia", "Noreen", "Viva", "Val", "Horace", "Maximo", "Fritz", "Mazie", "Gino", "Sophie", "Donald", "Olivia", "Barbie", "Beau", "Wei", "Versie", "Mickey", "Elvis", "Kyong", "Idalia", "Suk", "Hector", "Addie", "Enoch", "Jenice", "Reita", "Galen", "Ok", "Tomi", "Erasmo", "Luann", "Cortez", "Willie", "Darell", "Mao", "Evan", "Joshua", "Kieth", "Carlee", "Trent", "Antony", "Chanel", "Janel", "Cassy", "Gavin", "Taylor", "Wenona", "Elda", "Santos", "Tod", "Laree", "Roxann", "Ryann", "Alton", "Jani", "Ressie", "Vinnie", "Nam", "Basil", "Luke", "Darrin", "Ramon", "Aaron", "Amina", "Jerry", "Sacha", "Emory", "Boyce", "Howard", "Elia", "Josef", "Jean", "Harold", "Dillon", "Summer", "Titus", "Hyun", "Serina", "Keitha", "Earlie", "Wyatt", "Kenny", "Marcy", "Debrah", "Alyse", "Isaias", "Ji", "Nell", "Bud", "Colin", "Pilar", "Cami", "Mathew", "Sybil", "Emile", "Erinn", "Marlon", "Seth", "Man", "Drew", "Shawn", "Gennie", "Bunny", "Carlo", "Karl", "Irving", "Edwina", "Marlyn", "Buford", "Nicola", "Erwin", "Thad", "Carmen", "Aline", "Alexa", "Shana", "Lyle", "Athena", "Xuan", "August", "Benita", "Elliot", "Cary", "Brady", "Kelsi", "Benny", "Many", "Lin", "Clair", "Alla", "Thi", "Kasey", "Barb", "Denyse", "Brant", "Marin", "Mei", "Otis", "Julee", "Myrtie", "Sommer", "Mikel", "Jon", "Russel", "Elmira", "Nila", "Omer", "Marc", "Jackie", "Virgen", "Shanel", "Morris", "Herma", "Orval", "Dwight", "Jewell", "Melia", "Wayne", "Laura", "Aubrey", "Bell", "Delpha", "Mistie", "Leigha", "Joan", "Kelly", "Dagny", "Merlin", "Roland", "Buck", "Leigh", "Exie", "Keiko", "Jesus", "Salina", "Rudy", "Yetta", "Latina", "Cecil", "Bobby", "Adrian", "Levi", "Tamar", "Conrad", "Lucius", "Jimmy", "Zane", "Corey", "Jim", "Altha", "Royce", "Elly", "Cris", "Nadene", "Newton", "Arlena", "Melba", "Blair", "Keva", "Juan", "Minta", "Judith", "Cordia", "Abbey", "Joesph", "Len", "Parker", "Sixta", "Tisa", "Renate", "Herb", "Dinah", "Efren", "Sonny", "Carol", "Aleta", "Denny", "Hue", "Abram", "Janise", "Jose", "Vance", "Ervin", "Idell", "Nelda", "Lavera", "Jewel", "Donny", "Caleb", "Odessa", "Karlyn", "Noel", "Eli", "Wilbur", "Sid", "Jonas", "Dario", "Edison", "Shad", "Marion", "Dane", "Wilda", "Norah", "Clyde", "Emmett", "Keneth", "Tyson", "Moises", "Jody", "Landon", "Hollis", "Rubie", "Sung", "Minna", "Dannie", "Lyman", "Tobias", "Young", "Huey", "Britt", "Rodney", "Emelia", "Felton", "Julia", "Norine", "Owen", "Lyla", "Rueben", "Fermin", "Lawana", "Launa", "Malika", "Lucila", "Nona", "Dusti", "Dino", "Julene", "Yong", "Shayne", "Reba", "Wonda", "Gordon", "Flavia", "Luigi", "Mindi", "John", "Leif", "Damion", "Ami", "Danial", "Tierra", "Tiesha", "Cierra", "Latia", "Lorene", "Elicia", "Hong", "Lue", "Lazaro", "Gerri", "Soon", "Leoma", "Wanda", "Curtis", "Karla", "Robt", "Delta", "Duncan", "Ben", "Ethyl", "Weldon", "Trudie", "Barney", "Myriam", "Chere", "Hannah", "Lorine", "Brenda", "Temeka", "Tomeka", "Corina", "Bobbie", "Lowell", "Brian", "Ute", "Glady", "Elena", "Fidel", "Joseph", "Jordon", "Vashti", "Golda", "Jeri", "Hilda", "Sammie", "Margot", "Ed", "Rickey", "David", "Raul", "Joy", "Jeffry", "Thanh", "Ma", "Ada", "Melani", "Shelli", "Willy", "Raisa", "Chang", "Maryjo", "Jarrod", "Niesha", "Lester", "Daniel", "Keila", "Marcus", "Tori", "Loni", "Makeda", "Lynell", "Reed", "Marlys", "Inell", "Elton", "Burl", "Hyon", "Alvera", "Dorcas", "Monty", "Toney", "Donn", "Ossie", "Patsy", "Ardith", "Ezra", "Luanne", "Meghan", "Rashad", "Misty", "Rolf", "Lorina", "Elmer", "Kiley", "Jan", "Juana", "Kirk", "Yvette", "Evalyn", "Glynis", "Benito", "Cristi", "Brynn", "Aron", "Layne", "Tawny", "Louann", "Delmy", "Tyler", "Benton", "Oscar", "Elease", "Thu", "Chong", "Erma", "Tommie", "Cori", "Darcie", "Ana", "Jama", "Martin", "Vesta", "Mayme", "Gail", "Ranee", "Alonso", "Letty", "Josue", "Isiah", "Mariah", "Ciera", "Polly", "Maura", "Daina", "Dan", "Jamee", "Long", "Inger", "Robin", "Temika", "Fred", "Shenna", "Eden", "Zoe", "Andrew", "Danuta", "Vernia", "Nickie", "Vickey", "Sandee", "Leora", "Velma", "Leland", "Iesha", "Marie", "Perry", "Zada", "Debora", "Angela", "Nan", "Korey", "Jessia", "Walter", "Shala", "Lala", "Davis", "Fanny", "Brent", "Danilo", "Liz", "Tandra", "Leota", "Hans", "Louis", "Stan", "Brenna", "Susana", "Mabel", "Vennie", "Dena", "Forest", "Oma", "Cindi", "Darin", "Fairy", "Les", "Xavier", "Micah", "India", "Kayla", "Emmie", "Alia", "Brett", "Sandy", "Guy", "Lauren", "Elane", "Glenn", "Marcia", "Jenni", "Dee", "Sandie", "Paul", "Elayne", "Vernon", "Heath", "Kazuko", "Wesley", "Daron", "Sam", "Stefan", "Evette", "Kurt", "Alica", "Harvey", "Graham", "Lionel", "Wilson", "Tyrone", "Randal", "Ned", "Robby", "Neoma", "Cherry", "Burt", "Olene", "Nubia", "Theola", "Shera", "Bill", "Walton", "Chu", "Bette", "Birdie", "Hester", "Cassie", "Hal", "Lavada", "Lana", "Deanne", "Virgil", "Max", "Joey", "Reyes", "Della", "Glenna", "Lyndon", "Antone", "Nanci", "Teisha", "Felipe", "Clint", "Lianne", "Bruno", "Wai", "Anette", "Valeri", "Donita", "Albert", "Sharon", "Karyn", "Reena", "Shane", "Trevor", "Louie", "Elmo", "Glen", "Nella", "Damon", "Willis", "Valene", "Marian", "Darron", "Ellis", "Judson", "Verena", "Carie", "Kevin", "Susy", "Kelle", "Cole", "Claire", "Henry", "Buster", "Ward", "Caron", "Temple", "Lacey", "Stacey", "Leana", "Linda", "Dahlia", "Daisey", "Dong", "Dorian", "Steven", "Karine", "Ardis", "Marya", "Jettie", "Debbi", "Lera", "Morgan", "Ilona", "Yang", "Marine", "Norman", "Darrel", "Lanny", "Eliz", "Asa", "Eun", "Jaclyn", "Hien", "Loreen", "Hayden", "Coral", "Vera", "Misha", "Waylon", "Alyssa", "Hana", "Miquel", "Jill", "Renae", "Farah", "Azzie", "Rema", "Usha", "Dewitt", "Arden", "Vertie", "Jack", "Mason", "Rocky", "Bo", "Joie", "Dustin", "Rob", "Dennis", "Reina", "Marnie", "Travis", "Mavis", "Felix", "Tanya", "Allan", "Sherie", "Diedra", "Erin", "Elyse", "Merry", "Ewa", "Chi", "Al", "Bret", "Jessi", "Doyle", "Jone", "Jade", "Karie", "Teri", "Rupert"));
        private final List<String> names;

        Names(List<String> names) {
            this.names = names;
        }

        public String getRandomName() {
            return names.get(MathUtils.random(names.size() - 1));
        }
    }
}
