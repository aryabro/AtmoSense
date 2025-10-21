package edu.uiuc.cs427app;

import java.util.*;
import java.util.stream.Collectors;

public class LocalCityValidator {

    // Comprehensive list of major world cities
    private static final Set<String> VALID_CITIES = new HashSet<>(Arrays.asList(
            // United States
            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
            "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville",
            "Fort Worth", "Columbus", "Charlotte", "San Francisco", "Indianapolis",
            "Seattle", "Denver", "Washington", "Boston", "El Paso", "Nashville",
            "Detroit", "Oklahoma City", "Portland", "Las Vegas", "Memphis", "Louisville",
            "Baltimore", "Milwaukee", "Albuquerque", "Tucson", "Fresno", "Sacramento",
            "Mesa", "Kansas City", "Atlanta", "Long Beach", "Colorado Springs", "Raleigh",
            "Miami", "Virginia Beach", "Omaha", "Oakland", "Minneapolis", "Tulsa",
            "Arlington", "Tampa", "New Orleans", "Wichita", "Cleveland", "Bakersfield",
            "Aurora", "Anaheim", "Honolulu", "Santa Ana", "Corpus Christi", "Riverside",
            "Lexington", "Stockton", "Henderson", "Saint Paul", "St. Louis", "Milwaukee",

            // Canada
            "Toronto", "Montreal", "Vancouver", "Calgary", "Edmonton", "Ottawa",
            "Winnipeg", "Quebec City", "Hamilton", "Kitchener", "London", "Victoria",
            "Halifax", "Oshawa", "Windsor", "Saskatoon", "Regina", "Sherbrooke",
            "St. John's", "Barrie", "Kelowna", "Abbotsford", "Sudbury", "Kingston",

            // United Kingdom
            "London", "Birmingham", "Manchester", "Glasgow", "Liverpool", "Leeds",
            "Sheffield", "Edinburgh", "Bristol", "Cardiff", "Belfast", "Leicester",
            "Coventry", "Bradford", "Nottingham", "Hull", "Newcastle", "Stoke",
            "Southampton", "Derby", "Portsmouth", "Brighton", "Plymouth", "Northampton",

            // France
            "Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes", "Strasbourg",
            "Montpellier", "Bordeaux", "Lille", "Rennes", "Reims", "Le Havre",
            "Saint-Étienne", "Toulon", "Angers", "Grenoble", "Dijon", "Nîmes",

            // Germany
            "Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt", "Stuttgart",
            "Düsseldorf", "Dortmund", "Essen", "Leipzig", "Bremen", "Dresden",
            "Hannover", "Nuremberg", "Duisburg", "Bochum", "Wuppertal", "Bielefeld",

            // Italy
            "Rome", "Milan", "Naples", "Turin", "Palermo", "Genoa", "Bologna",
            "Florence", "Bari", "Catania", "Venice", "Verona", "Messina", "Padua",
            "Trieste", "Brescia", "Parma", "Taranto", "Prato", "Modena",

            // Spain
            "Madrid", "Barcelona", "Valencia", "Seville", "Zaragoza", "Málaga",
            "Murcia", "Palma", "Las Palmas", "Bilbao", "Alicante", "Córdoba",
            "Valladolid", "Vigo", "Gijón", "L'Hospitalet", "Vitoria", "A Coruña",

            // China
            "Beijing", "Shanghai", "Guangzhou", "Shenzhen", "Tianjin", "Wuhan",
            "Xi'an", "Chengdu", "Nanjing", "Chongqing", "Hangzhou", "Suzhou",
            "Shenyang", "Qingdao", "Dalian", "Ningbo", "Xiamen", "Fuzhou",
            "Jinan", "Harbin", "Changsha", "Zhengzhou", "Kunming", "Taiyuan",

            // Japan
            "Tokyo", "Yokohama", "Osaka", "Nagoya", "Sapporo", "Fukuoka", "Kobe",
            "Kawasaki", "Kyoto", "Saitama", "Hiroshima", "Sendai", "Kitakyushu",
            "Chiba", "Sakai", "Niigata", "Hamamatsu", "Okayama", "Sagamihara",

            // India
            "Mumbai", "Delhi", "Bangalore", "Hyderabad", "Ahmedabad", "Chennai",
            "Kolkata", "Surat", "Pune", "Jaipur", "Lucknow", "Kanpur", "Nagpur",
            "Indore", "Thane", "Bhopal", "Visakhapatnam", "Pimpri", "Patna",

            // Australia
            "Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide", "Gold Coast",
            "Newcastle", "Canberra", "Sunshine Coast", "Wollongong", "Hobart",
            "Geelong", "Townsville", "Cairns", "Darwin", "Toowoomba", "Ballarat",

            // Brazil
            "São Paulo", "Rio de Janeiro", "Brasília", "Salvador", "Fortaleza",
            "Belo Horizonte", "Manaus", "Curitiba", "Recife", "Porto Alegre",
            "Belém", "Goiânia", "Guarulhos", "Campinas", "São Luís", "São Gonçalo",

            // Russia
            "Moscow", "Saint Petersburg", "Novosibirsk", "Yekaterinburg", "Nizhny Novgorod",
            "Kazan", "Chelyabinsk", "Omsk", "Samara", "Rostov-on-Don", "Ufa",
            "Krasnoyarsk", "Perm", "Voronezh", "Volgograd", "Krasnodar", "Saratov",

            // Mexico
            "Mexico City", "Guadalajara", "Monterrey", "Puebla", "Tijuana", "León",
            "Juárez", "Torreón", "Querétaro", "San Luis Potosí", "Mérida", "Mexicali",
            "Aguascalientes", "Acapulco", "Cancún", "Saltillo", "Hermosillo", "Villahermosa",

            // South Korea
            "Seoul", "Busan", "Incheon", "Daegu", "Daejeon", "Gwangju", "Suwon",
            "Ulsan", "Changwon", "Goyang", "Yongin", "Seongnam", "Bucheon", "Ansan",
            "Anyang", "Namyangju", "Hwaseong", "Cheongju", "Jeonju", "Cheonan",

            // Turkey
            "Istanbul", "Ankara", "Izmir", "Bursa", "Antalya", "Adana", "Konya",
            "Gaziantep", "Mersin", "Diyarbakır", "Kayseri", "Eskişehir", "Urfa",
            "Malatya", "Erzurum", "Van", "Batman", "Elazığ", "Isparta", "Trabzon",

            // Egypt
            "Cairo", "Alexandria", "Giza", "Shubra El Kheima", "Port Said", "Suez",
            "Luxor", "Mansoura", "El Mahalla El Kubra", "Tanta", "Asyut", "Ismailia",
            "Faiyum", "Zagazig", "Aswan", "Damietta", "Minya", "Beni Suef",

            // South Africa
            "Johannesburg", "Cape Town", "Durban", "Pretoria", "Port Elizabeth",
            "Bloemfontein", "East London", "Pietermaritzburg", "Nelspruit", "Kimberley",
            "Polokwane", "Rustenburg", "Welkom", "Potchefstroom", "Klerksdorp",

            // Argentina
            "Buenos Aires", "Córdoba", "Rosario", "Mendoza", "La Plata", "Tucumán",
            "Mar del Plata", "Salta", "Santa Fe", "San Juan", "Resistencia", "Neuquén",
            "Santiago del Estero", "Corrientes", "Bahía Blanca", "Posadas", "Paraná",

            // Thailand
            "Bangkok", "Nonthaburi", "Nakhon Ratchasima", "Chiang Mai", "Hat Yai",
            "Udon Thani", "Pak Kret", "Khon Kaen", "Ubon Ratchathani", "Nakhon Si Thammarat",
            "Nakhon Sawan", "Phitsanulok", "Nakhon Pathom", "Lampang", "Ratchaburi",

            // Indonesia
            "Jakarta", "Surabaya", "Bandung", "Medan", "Semarang", "Makassar",
            "Palembang", "Tangerang", "Depok", "Bekasi", "Padang", "Malang",
            "Samarinda", "Denpasar", "Bandar Lampung", "Banjarmasin", "Pekanbaru",

            // Vietnam
            "Ho Chi Minh City", "Hanoi", "Da Nang", "Hai Phong", "Can Tho", "Bien Hoa",
            "Hue", "Nha Trang", "Buon Ma Thuot", "Qui Nhon", "Vung Tau", "Thai Nguyen",
            "Thanh Hoa", "Nam Dinh", "Vinh", "Long Xuyen", "Rach Gia", "My Tho",

            // Philippines
            "Manila", "Quezon City", "Caloocan", "Davao", "Cebu City", "Zamboanga",
            "Antipolo", "Pasig", "Taguig", "Valenzuela", "Parañaque", "Las Piñas",
            "Makati", "Bacolod", "General Santos", "Marikina", "Muntinlupa", "Calamba",

            // Pakistan
            "Karachi", "Lahore", "Faisalabad", "Rawalpindi", "Multan", "Gujranwala",
            "Hyderabad", "Peshawar", "Islamabad", "Quetta", "Bahawalpur", "Sargodha",
            "Sialkot", "Sukkur", "Larkana", "Sheikhupura", "Rahim Yar Khan", "Gujrat",

            // Bangladesh
            "Dhaka", "Chittagong", "Khulna", "Rajshahi", "Sylhet", "Barisal",
            "Rangpur", "Mymensingh", "Comilla", "Narayanganj", "Gazipur", "Jessore",
            "Bogra", "Dinajpur", "Cox's Bazar", "Kushtia", "Nawabganj", "Saidpur",

            // Nigeria
            "Lagos", "Kano", "Ibadan", "Benin City", "Port Harcourt", "Jos",
            "Ilorin", "Abuja", "Kaduna", "Maiduguri", "Zaria", "Aba", "Iba",
            "Ogbomoso", "Mushin", "Maiduguri", "Enugu", "Ede", "Abeokuta",

            // Kenya
            "Nairobi", "Mombasa", "Kisumu", "Nakuru", "Eldoret", "Malindi",
            "Kitale", "Garissa", "Kakamega", "Nyeri", "Meru", "Thika", "Machakos",
            "Bungoma", "Busia", "Homa Bay", "Kisii", "Kericho", "Embu",

            // Morocco
            "Casablanca", "Rabat", "Fez", "Marrakech", "Agadir", "Tangier",
            "Meknes", "Oujda", "Kenitra", "Tetouan", "Safi", "Mohammedia",
            "Khouribga", "Beni Mellal", "El Jadida", "Taza", "Nador", "Settat",

            // Algeria
            "Algiers", "Oran", "Constantine", "Annaba", "Blida", "Batna",
            "Djelfa", "Sétif", "Sidi Bel Abbès", "Biskra", "Tébessa", "El Oued",
            "Skikda", "Tiaret", "Béjaïa", "Tlemcen", "Ouargla", "Mostaganem",

            // Tunisia
            "Tunis", "Sfax", "Sousse", "Kairouan", "Bizerte", "Gabès",
            "Ariana", "Gafsa", "Monastir", "Ben Arous", "Kasserine", "Medenine",
            "Nabeul", "Tataouine", "Béja", "Jendouba", "Kef", "Mahdia",

            // Israel
            "Jerusalem", "Tel Aviv", "Haifa", "Rishon LeZion", "Petah Tikva",
            "Ashdod", "Netanya", "Beer Sheva", "Holon", "Bnei Brak", "Ramat Gan",
            "Ashkelon", "Rehovot", "Bat Yam", "Herzliya", "Kfar Saba", "Hadera",

            // Saudi Arabia
            "Riyadh", "Jeddah", "Mecca", "Medina", "Dammam", "Khobar", "Dhahran",
            "Taif", "Buraidah", "Tabuk", "Khamis Mushait", "Hail", "Najran",
            "Al Jubail", "Yanbu", "Abha", "Jazan", "Al Khobar", "Sakaka",

            // UAE
            "Dubai", "Abu Dhabi", "Sharjah", "Al Ain", "Ajman", "Ras Al Khaimah",
            "Fujairah", "Umm Al Quwain", "Khor Fakkan", "Dibba Al Fujairah",

            // Qatar
            "Doha", "Al Rayyan", "Umm Salal", "Al Khor", "Al Wakrah", "Dukhan",
            "Al Shamal", "Mesaieed", "Lusail", "Al Daayen",

            // Kuwait
            "Kuwait City", "Al Ahmadi", "Hawalli", "Al Farwaniyah", "Al Jahra",
            "Mubarak Al Kabeer", "Al Asimah", "Al Wafra", "Al Zour", "Al Nuwaiseeb",

            // Jordan
            "Amman", "Zarqa", "Irbid", "Russeifa", "Wadi as Sir", "Aqaba",
            "Salt", "Madaba", "Jerash", "Mafraq", "Karak", "Tafilah", "Ma'an",

            // Lebanon
            "Beirut", "Tripoli", "Sidon", "Tyre", "Zahle", "Baalbek", "Jounieh",
            "Byblos", "Nabatieh", "Batroun", "Jbeil", "Zgharta", "Halba", "Marjayoun",

            // Syria
            "Damascus", "Aleppo", "Homs", "Hama", "Latakia", "Deir ez-Zor",
            "Raqqa", "Al-Hasakah", "Qamishli", "Tartus", "Idlib", "Daraa",
            "As-Suwayda", "Al-Qunaytirah", "Al-Bab", "Manbij", "Afrin", "Kobani",

            // Iraq
            "Baghdad", "Basra", "Mosul", "Erbil", "Najaf", "Karbala", "Hillah",
            "Kirkuk", "Nasiriyah", "Amara", "Samarra", "Ramadi", "Fallujah",
            "Tikrit", "Kut", "Diyala", "Sulaymaniyah", "Dohuk", "Zakho",

            // Iran
            "Tehran", "Mashhad", "Isfahan", "Tabriz", "Shiraz", "Karaj", "Ahvaz",
            "Qom", "Kermanshah", "Urmia", "Rasht", "Zahedan", "Hamadan", "Kerman",
            "Yazd", "Ardabil", "Bandar Abbas", "Arak", "Eslamshahr", "Zanjan",

            // Afghanistan
            "Kabul", "Kandahar", "Herat", "Mazar-i-Sharif", "Jalalabad", "Kunduz",
            "Ghazni", "Balkh", "Baghlan", "Gardez", "Khost", "Maymana", "Farah",
            "Pul-i-Khumri", "Charikar", "Sheberghan", "Taloqan", "Mehtar Lam", "Aybak",

            // Nepal
            "Kathmandu", "Pokhara", "Lalitpur", "Bharatpur", "Biratnagar", "Birgunj",
            "Dharan", "Butwal", "Dhangadhi", "Nepalgunj", "Itahari", "Hetauda",
            "Madhyapur Thimi", "Janakpur", "Birendranagar", "Ghorahi", "Tikapur",

            // Sri Lanka
            "Colombo", "Dehiwala-Mount Lavinia", "Moratuwa", "Negombo", "Kandy",
            "Kalmunai", "Vavuniya", "Galle", "Trincomalee", "Batticaloa", "Jaffna",
            "Katunayake", "Dambulla", "Kolonnawa", "Anuradhapura", "Ratnapura",

            // Myanmar
            "Yangon", "Mandalay", "Naypyidaw", "Mawlamyine", "Taunggyi", "Monywa",
            "Myitkyina", "Meiktila", "Bago", "Pathein", "Sittwe", "Myeik",
            "Mergui", "Lashio", "Pyay", "Hinthada", "Pakokku", "Magway",

            // Cambodia
            "Phnom Penh", "Battambang", "Siem Reap", "Sihanoukville", "Kampong Cham",
            "Kampong Thom", "Kampong Chhnang", "Kampong Speu", "Takeo", "Kandal",
            "Pursat", "Kratie", "Kampot", "Koh Kong", "Prey Veng", "Svay Rieng",

            // Laos
            "Vientiane", "Pakse", "Savannakhet", "Luang Prabang", "Xam Neua",
            "Phonsavan", "Thakhek", "Vang Vieng", "Muang Xay", "Champasak",
            "Salavan", "Attapeu", "Sekong", "Bokeo", "Phongsaly", "Huaphanh",

            // Malaysia
            "Kuala Lumpur", "George Town", "Ipoh", "Shah Alam", "Petaling Jaya",
            "Klang", "Johor Bahru", "Subang Jaya", "Kuching", "Kota Kinabalu",
            "Kajang", "Sepang", "Ampang Jaya", "Kuantan", "Alor Setar", "Miri",

            // Singapore
            "Singapore", "Jurong East", "Woodlands", "Tampines", "Hougang",
            "Yishun", "Choa Chu Kang", "Punggol", "Sengkang", "Ang Mo Kio",
            "Bedok", "Pasir Ris", "Bukit Merah", "Queenstown", "Toa Payoh",

            // New Zealand
            "Auckland", "Wellington", "Christchurch", "Hamilton", "Tauranga",
            "Napier", "Hastings", "Dunedin", "Palmerston North", "Nelson",
            "Rotorua", "New Plymouth", "Whangarei", "Invercargill", "Whanganui",

            // Chile
            "Santiago", "Valparaíso", "Concepción", "La Serena", "Antofagasta",
            "Temuco", "Rancagua", "Talca", "Arica", "Chillán", "Iquique",
            "Los Ángeles", "Puerto Montt", "Valdivia", "Osorno", "Curicó",

            // Peru
            "Lima", "Arequipa", "Trujillo", "Chiclayo", "Piura", "Iquitos",
            "Cusco", "Chimbote", "Huancayo", "Tacna", "Ica", "Juliaca",
            "Ayacucho", "Cajamarca", "Pucallpa", "Tumbes", "Huaraz", "Puno",

            // Colombia
            "Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Cúcuta",
            "Bucaramanga", "Pereira", "Santa Marta", "Ibagué", "Pasto", "Manizales",
            "Neiva", "Villavicencio", "Armenia", "Valledupar", "Montería", "Sincelejo",

            // Venezuela
            "Caracas", "Maracaibo", "Valencia", "Barquisimeto", "Maracay", "Ciudad Guayana",
            "San Cristóbal", "Maturín", "Ciudad Bolívar", "Cumana", "Cabimas", "Turmero",
            "Puerto La Cruz", "Los Teques", "Punto Fijo", "Guarenas", "Acarigua", "Guanare",

            // Ecuador
            "Quito", "Guayaquil", "Cuenca", "Santo Domingo", "Machala", "Manta",
            "Portoviejo", "Ambato", "Riobamba", "Quevedo", "Milagro", "Ibarra",
            "Loja", "Esmeraldas", "Babahoyo", "Tulcán", "Latacunga", "Nueva Loja",

            // Bolivia
            "La Paz", "Santa Cruz", "Cochabamba", "Sucre", "Oruro", "Potosí",
            "Tarija", "Trinidad", "Cobija", "Riberalta", "Montero", "Guayaramerín",
            "Villazón", "Tupiza", "Llallagua", "Camiri", "Villa Montes", "San Ignacio",

            // Paraguay
            "Asunción", "Ciudad del Este", "San Lorenzo", "Luque", "Capiatá",
            "Lambaré", "Fernando de la Mora", "Limpio", "Ñemby", "Encarnación",
            "Mariano Roque Alonso", "Itauguá", "Villa Elisa", "Caaguazú", "Coronel Oviedo",
            "Concepción", "Villarrica", "Caacupé", "Itá", "Areguá",

            // Uruguay
            "Montevideo", "Salto", "Paysandú", "Las Piedras", "Rivera", "Maldonado",
            "Tacuarembó", "Melo", "Mercedes", "Artigas", "Minas", "San José de Mayo",
            "Durazno", "Florida", "Barros Blancos", "Ciudad de la Costa", "Paso de los Toros",
            "Rocha", "Fray Bentos", "Trinidad",

            // Cuba
            "Havana", "Santiago de Cuba", "Camagüey", "Holguín", "Santa Clara",
            "Guantánamo", "Bayamo", "Las Tunas", "Cienfuegos", "Pinar del Río",
            "Matanzas", "Ciego de Ávila", "Sancti Spíritus", "Mariel", "Artemisa",
            "Mayabeque", "Isla de la Juventud", "Baracoa", "Moa", "Nueva Gerona",

            // Dominican Republic
            "Santo Domingo", "Santiago", "La Romana", "San Pedro de Macorís",
            "San Francisco de Macorís", "Puerto Plata", "Higüey", "San Cristóbal",
            "Villa Altagracia", "Bonao", "Azua", "Moca", "Baní", "Barahona",
            "Hato Mayor", "La Vega", "Nagua", "San Juan de la Maguana", "Valverde",

            // Haiti
            "Port-au-Prince", "Carrefour", "Delmas", "Pétion-Ville", "Port-de-Paix",
            "Croix-des-Bouquets", "Jacmel", "Okap", "Léogâne", "Les Cayes",
            "Tigwav", "Jérémie", "Miragoâne", "Gonaïves", "Saint-Marc", "Cap-Haïtien",
            "Verrettes", "Petit-Goâve", "Dessalines", "Saint-Louis-du-Nord",

            // Jamaica
            "Kingston", "Spanish Town", "Portmore", "Montego Bay", "May Pen",
            "Mandeville", "Old Harbour", "Linstead", "Half Way Tree", "Savanna-la-Mar",
            "Port Antonio", "St. Ann's Bay", "Bog Walk", "Constant Spring", "Ewarton",
            "Hayes", "Ocho Rios", "Morant Bay", "Black River", "Falmouth",

            // Trinidad and Tobago
            "Port of Spain", "San Fernando", "Chaguanas", "Arima", "Couva",
            "Point Fortin", "Princes Town", "Rio Claro", "Sangre Grande", "Siparia",
            "Tunapuna", "Arouca", "Barrackpore", "Carapichaima", "Cunupia",
            "Debe", "Gasparillo", "La Brea", "Marabella", "Penal",

            // Barbados
            "Bridgetown", "Speightstown", "Oistins", "Crane", "Holetown",
            "Six Cross Roads", "Black Rock", "Cave Hill", "Wildey", "Warrens",
            "Hastings", "Worthing", "Maxwell", "Rockley", "St. Lawrence",
            "St. James", "St. Philip", "St. George", "St. Thomas", "St. John",

            // Bahamas
            "Nassau", "Freeport", "West End", "Coopers Town", "Marsh Harbour",
            "Freetown", "High Rock", "Andros Town", "Clarence Town", "Dunmore Town",
            "George Town", "Alice Town", "Sweeting Cay", "Spanish Wells", "Governor's Harbour",
            "Rock Sound", "Arthur's Town", "Cockburn Town", "Matthew Town", "Port Nelson",

            // Belize
            "Belize City", "San Ignacio", "Orange Walk", "San Pedro", "Corozal",
            "Dangriga", "Punta Gorda", "Benque Viejo del Carmen", "Placencia",
            "Hopkins", "Caye Caulker", "San Antonio", "Valley of Peace", "Ladyville",
            "Burrell Boom", "La Democracia", "Independence", "Mango Creek", "Big Falls",

            // Costa Rica
            "San José", "Cartago", "Alajuela", "Heredia", "Puntarenas", "Limón",
            "Liberia", "Paraíso", "Desamparados", "San Isidro", "Curridabat",
            "San Vicente", "Tibás", "San Rafael", "Aserrí", "Escazú", "Santa Ana",
            "San Pedro", "Montes de Oca", "Goicoechea", "Moravia",

            // Panama
            "Panama City", "San Miguelito", "Tocumen", "David", "Arraiján",
            "Colón", "Las Cumbres", "La Chorrera", "Pacora", "Santiago",
            "Chitré", "Vista Alegre", "Chilibre", "Cativá", "Nuevo Arraiján",
            "Alcalde Díaz", "24 de Diciembre", "Ancón", "Pedregal", "Juan Díaz",

            // Guatemala
            "Guatemala City", "Mixco", "Villa Nueva", "Petapa", "San Juan Sacatepéquez",
            "Quetzaltenango", "Villa Canales", "Escuintla", "Chinautla", "Chimaltenango",
            "Chichicastenango", "Huehuetenango", "Cobán", "Jalapa", "Mazatenango",
            "Retalhuleu", "Zacapa", "Chiquimula", "Jutiapa", "Santa Rosa",

            // Honduras
            "Tegucigalpa", "San Pedro Sula", "Choloma", "La Ceiba", "El Progreso",
            "Choluteca", "Comayagua", "Puerto Cortés", "La Lima", "Danlí",
            "Siquatepeque", "Juticalpa", "Villanueva", "Tela", "Santa Rosa de Copán",
            "Olanchito", "San Lorenzo", "Cofradía", "El Paraíso", "La Paz",

            // El Salvador
            "San Salvador", "Santa Ana", "San Miguel", "Mejicanos", "Soyapango",
            "Santa Tecla", "Apopa", "Delgado", "Sonsonate", "San Marcos",
            "Usulután", "Cojutepeque", "Cuscatancingo", "Zacatecoluca", "San Martín",
            "Ilopango", "Ahuachapán", "Antiguo Cuscatlán", "Chalchuapa", "Quezaltepeque",

            // Nicaragua
            "Managua", "León", "Masaya", "Matagalpa", "Chinandega", "Granada",
            "Estelí", "Tipitapa", "Jinotega", "Bluefields", "Juigalpa", "Nagarote",
            "Diriamba", "Rivas", "Nueva Guinea", "Jinotepe", "El Viejo", "Nandaime",
            "Camoapa", "Boaco",

            // Iceland
            "Reykjavík", "Kópavogur", "Hafnarfjörður", "Akureyri", "Reykjanesbær",
            "Garðabær", "Mosfellsbær", "Árborg", "Akranes", "Fjarðabyggð",
            "Seltjarnarnes", "Skagafjörður", "Ísafjörður", "Borgarbyggð", "Fljótsdalshérað",
            "Vestmannaeyjar", "Sveitarfélagið Hornafjörður", "Sveitarfélagið Skagaströnd",
            "Sveitarfélagið Skagafjörður", "Sveitarfélagið Árneshreppur",

            // Greenland
            "Nuuk", "Sisimiut", "Ilulissat", "Qaqortoq", "Aasiaat", "Maniitsoq",
            "Tasiilaq", "Paamiut", "Narsaq", "Nanortalik", "Qasigiannguit", "Uummannaq",
            "Upernavik", "Qeqertarsuaq", "Kangaatsiaq", "Kangerlussuaq", "Ittoqqortoormiit",
            "Kullorsuaq", "Sermiligaaq", "Kuummiit",

            // Faroe Islands
            "Tórshavn", "Klaksvík", "Runavík", "Eystur", "Fuglafjørður", "Vestmanna",
            "Vágur", "Nes", "Saltangará", "Leirvík", "Sandur", "Toftir", "Strendur",
            "Skála", "Hvalba", "Eiði", "Kvívík", "Sandavágur", "Hvannasund", "Sørvágur",

            // Malta
            "Valletta", "Birkirkara", "Mosta", "Qormi", "Żabbar", "San Pawl il-Baħar",
            "Sliema", "Żebbuġ", "Fgura", "Żejtun", "Attard", "Marsaskala", "Paola",
            "Naxxar", "Żurrieq", "San Ġwann", "Marsa", "Tarxien", "Gżira", "Rabat",

            // Cyprus
            "Nicosia", "Limassol", "Larnaca", "Paphos", "Famagusta", "Kyrenia",
            "Aradippou", "Paralimni", "Latsia", "Aglantzia", "Engomi", "Mesa Geitonia",
            "Morphou", "Polis", "Lefkara", "Ayia Napa", "Protaras", "Kato Polemidia",
            "Deryneia", "Livadia",

            // Luxembourg
            "Luxembourg", "Esch-sur-Alzette", "Differdange", "Dudelange", "Ettelbruck",
            "Diekirch", "Wiltz", "Echternach", "Rumelange", "Grevenmacher", "Remich",
            "Vianden", "Mondorf-les-Bains", "Steinfort", "Mamer", "Hesperange",
            "Leudelange", "Sandweiler", "Strassen", "Walferdange",

            // Monaco
            "Monaco", "Monte Carlo", "La Condamine", "Fontvieille", "Larvotto",
            "La Rousse", "Saint Michel", "La Colle", "Les Révoires", "Moneghetti",
            "Saint Roman", "Vallon de la Rousse", "Spélugues", "Jardin Exotique",
            "Les Moneghetti", "Ravin de Sainte-Dévote", "Boulevard des Moulins",
            "Place du Casino", "Avenue Princesse Grace", "Port Hercule",

            // Liechtenstein
            "Vaduz", "Schaan", "Triesen", "Balzers", "Eschen", "Mauren", "Triesenberg",
            "Ruggell", "Gamprin", "Schellenberg", "Planken", "Nendeln", "Bendern",
            "Schellenberg", "Mäls", "Steg", "Malbun", "Sücka", "Silum", "Steg",

            // San Marino
            "San Marino", "Serravalle", "Borgo Maggiore", "Domagnano", "Fiorentino",
            "Acquaviva", "Faetano", "Chiesanuova", "Montegiardino", "Città di San Marino",
            "Castello di San Marino", "Castello di Serravalle", "Castello di Borgo Maggiore",
            "Castello di Domagnano", "Castello di Fiorentino", "Castello di Acquaviva",
            "Castello di Faetano", "Castello di Chiesanuova", "Castello di Montegiardino",
            "Castello di San Marino",

            // Vatican City
            "Vatican City", "St. Peter's Basilica", "Vatican Museums", "Sistine Chapel",
            "Apostolic Palace", "Vatican Gardens", "St. Peter's Square", "Vatican Library",
            "Vatican Observatory", "Vatican Radio", "Vatican Pharmacy", "Vatican Bank",
            "Vatican Post Office", "Vatican Gendarmerie", "Vatican Fire Brigade",
            "Vatican Health Service", "Vatican Press Office", "Vatican Television Center",
            "Vatican Information Service", "Vatican Internet Service"));

    public static boolean isValidCity(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return false;
        }

        // Normalize the city name (trim, proper case)
        String normalizedCity = normalizeCityName(cityName.trim());

        // Check if the city exists in our list
        return VALID_CITIES.contains(normalizedCity);
    }

    /**
     * Get the normalized (properly formatted) version of a valid city name
     * 
     * @param cityName The input city name
     * @return The normalized city name if valid, null if not valid
     */
    public static String getNormalizedCityName(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return null;
        }

        // Normalize the city name (trim, proper case)
        String normalizedCity = normalizeCityName(cityName.trim());

        // Return the normalized name if it exists in our list
        if (VALID_CITIES.contains(normalizedCity)) {
            return normalizedCity;
        }

        return null;
    }

    public static String normalizeCityName(String cityName) {
        // Convert to proper case (first letter of each word capitalized)
        String[] words = cityName.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            if (!words[i].isEmpty()) {
                result.append(Character.toUpperCase(words[i].charAt(0)));
                if (words[i].length() > 1) {
                    result.append(words[i].substring(1));
                }
            }
        }

        return result.toString();
    }

    public static String getValidationMessage(String cityName) {
        if (isValidCity(cityName)) {
            return "Valid city: " + normalizeCityName(cityName);
        } else {
            List<String> suggestions = getSimilarCities(cityName, 3);
            if (!suggestions.isEmpty()) {
                return "City not found. Did you mean: " + String.join(", ", suggestions) + "?";
            } else {
                return "City not found. Please check the spelling and try again.";
            }
        }
    }

    public static List<String> getSimilarCities(String inputCity, int maxSuggestions) {
        if (inputCity == null || inputCity.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String normalizedInput = normalizeCityName(inputCity.trim());
        List<CitySimilarity> similarities = new ArrayList<>();

        for (String city : VALID_CITIES) {
            double similarity = calculateSimilarity(normalizedInput, city);
            if (similarity > 0.3) { // Only consider cities with >30% similarity
                similarities.add(new CitySimilarity(city, similarity));
            }
        }

        // Sort by similarity (highest first) and return top suggestions
        return similarities.stream()
                .sorted((a, b) -> Double.compare(b.similarity, a.similarity))
                .limit(maxSuggestions)
                .map(cs -> cs.cityName)
                .collect(Collectors.toList());
    }

    private static double calculateSimilarity(String str1, String str2) {
        // Use Levenshtein distance to calculate similarity
        int distance = levenshteinDistance(str1.toLowerCase(), str2.toLowerCase());
        int maxLength = Math.max(str1.length(), str2.length());

        if (maxLength == 0)
            return 1.0;

        return 1.0 - (double) distance / maxLength;
    }

    private static int levenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1));
                }
            }
        }

        return dp[str1.length()][str2.length()];
    }

    private static class CitySimilarity {
        final String cityName;
        final double similarity;

        CitySimilarity(String cityName, double similarity) {
            this.cityName = cityName;
            this.similarity = similarity;
        }
    }
}
