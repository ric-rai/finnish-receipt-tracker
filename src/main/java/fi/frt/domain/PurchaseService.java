package fi.frt.domain;

import fi.frt.dao.Dao;
import fi.frt.domain.textinput.TextInput;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fi.frt.utilities.MappingUtils.toStrMap;

public class PurchaseService {
    private Dao<Purchase, Long> purchaseDao;
    private List<Purchase> activePurchasesList;
    private Map<Long, List<Purchase>> allPurchases = new HashMap<>();
    private Map<String, Set<String>> nameTypeMap = new HashMap<>();
    private Map<String, Set<String>> typeSuggestionSets = new HashMap<>();
    private URLClassLoader urlLoader;

    public PurchaseService() {
    }

    /**
     * Luo uuden PurchaseService -olion. Konstruktori lataa tietokannasta kaikki ostokset oliomuuttujaan, sekä lataa
     * konfiguraatiotiedostosta ostostyyppeihin liittyvät määrittelyt.
     *
     * @param purchaseDao Dao-rajapinnan toteuttava olio
     */
    public PurchaseService(Dao<Purchase, Long> purchaseDao) {
        this.purchaseDao = purchaseDao;
        loadAllPurchases();
        initClassLoader();
        Properties props = readPropertiesFile();
        Map<String, String> purchaseTypeProperties = getPurchaseTypeProps(props);
        readPurchaseTypeProperties(purchaseTypeProperties);
    }

    private void loadAllPurchases() {
        List<Purchase> purchases = purchaseDao.list();
        purchases.forEach(p -> {
            allPurchases.putIfAbsent(p.getReceiptId(), new ArrayList<>());
            allPurchases.get(p.getReceiptId()).add(p);
            String purchaseType = p.getType();
            if (purchaseType != null && !purchaseType.isEmpty()) {
                nameTypeMap.putIfAbsent(p.getName(), new HashSet<>());
                nameTypeMap.get(p.getName()).add(p.getType());
            }
        });
    }

    /**
     * Asettaa kuittiin liittyvät ostokset activePurchaseList-listaan.
     *
     * @param receiptId Kuitin id-tunnus
     */
    public void setActivePurchases(Long receiptId) {
        activePurchasesList.clear();
        activePurchasesList.addAll(allPurchases.getOrDefault(receiptId, new ArrayList<>()));
    }

    /**
     * Asettaa uudet ostokset kuitille. Metodi poistaa ensin aiemmin kuittiin mahdollisesti liittyneet ostokset, minkä
     * jälkeen se tallentaa annetusta tekstisyötelistasta validit syötetekstit kuitin ostoksiksi.
     *
     * @param receiptId      Sen kuitin id-tunnus, jonka ostokset asetetaan
     * @param purchaseInputs Syötetekstilista, jonka syötetekstien tulee kuvata ostosten tietoja
     */
    public void setNewPurchases(Long receiptId, List<TextInput> purchaseInputs) {
        purchaseDao.deleteByValue(toStrMap("receiptId", receiptId));
        List<Purchase> purchases = purchaseInputs.stream()
                .filter(TextInput::isValid)
                .map(pi -> new Purchase(toStrMap(pi.getAttrMap(), "receiptId", receiptId)))
                .collect(Collectors.toList());
        purchases.forEach(p -> {
            purchaseDao.create(p);
            nameTypeMap.putIfAbsent(p.getName(), new HashSet<>());
            nameTypeMap.get(p.getName()).add(p.getType());
        });
        allPurchases.put(receiptId, purchases);
    }

    /**
     * Luo listan ehdotuksia ostostyypeistä annetulle ostosnimikkeelle. Jos järjestelmään on jo aiemmin syötetty ostos
     * samalla nimikkeellä, niin metodi palauttaa siihen ostokseen liitetyn ostostyypin ensisijassa. Jos
     * ostostyyppitiedostoja on ladattu, niin metodi etsii myös niistä nimikettä vastaavia ostostyyppejä.
     *
     * @param purchaseName Ostoksen nimike kuitissa
     * @return Ostostyyppejä sisältävä järjestetty merkkijonosetti, jonka järjestys pyrkii kuvaamaan ehdotusten
     * sopivuutta. Ensimmäisessä indeksissä on siis todennäköisesti sopivin tyyppi ja seuraavassa sitä sopivin jne.
     */
    public Set<String> getTypeSuggestions(String purchaseName) {
        Set<String> suggs = new LinkedHashSet<>();
        if (nameTypeMap.containsKey(purchaseName)) {
            suggs.addAll(nameTypeMap.get(purchaseName));
        }
        if (typeSuggestionSets != null) {
            String prepared = purchaseName.toLowerCase().replaceAll("[^a-ö]", "");
            suggs.addAll(getMatchesFromTypeSuggestionSets(prepared));
        }
        return suggs;
    }

    private ArrayList<String> getMatchesFromTypeSuggestionSets(String purchaseName) {
        String[] substrings = getAllSubStrings(purchaseName, 3);
        ArrayList<String> matches = new ArrayList<>();
        for (String substring : substrings) {
            for (Map.Entry<String, Set<String>> entry : typeSuggestionSets.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                if (value.contains(substring)) {
                    matches.add(key);
                }
            }
        }
        return matches;
    }

    private String[] getAllSubStrings(String string, int threshold) {
        char[] str = string.toCharArray();
        int n = str.length;
        int count = -1;
        String[] strings = new String[(n * (n + 1) / 2) - (n * 3 - 3)];
        StringBuilder builder = new StringBuilder();
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                for (int k = i; k <= j; k++) {
                    builder.append(str[k]);
                }
                if (builder.length() > threshold) {
                    strings[++count] = builder.toString();
                }
                builder.setLength(0);
            }
        }
        return strings;
    }


    public void setActivePurchasesList(List<Purchase> activePurchasesList) {
        this.activePurchasesList = activePurchasesList;
    }

    private Map<String, String> getPurchaseTypeProps(Properties props) {
        Map<String, String> propMap = new HashMap<>();
        props.forEach((k, v) -> {
            String key = (String) k;
            if (key.startsWith("purchase-type")) {
                propMap.put(StringUtils.capitalize(key.replace("purchase-type.", "")), (String) v);
            }
        });
        return propMap;
    }

    private Properties readPropertiesFile() {
        Properties props = new Properties();
        URL resource = urlLoader.findResource("application.properties");
        if (resource != null) {
            try {
                FileInputStream input = new FileInputStream(new File(resource.getPath()));
                props.load(new InputStreamReader(input, Charset.forName("UTF-8")));
            } catch (Exception e) {
                System.out.println("Loading of application.properties failed with the following message:");
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Application.properties file not found");
        }
        return props;
    }

    private void initClassLoader() {
        try {
            File classFile = new File(
                    PurchaseService.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            File folder;
            if (classFile.getName().matches(".+\\..+$")) {
                folder = new File(classFile.getPath().replace(classFile.getName(), ""));
            } else {
                folder = new File(classFile.getPath() + "/");
            }
            URL[] urls = new URL[]{folder.toURI().toURL()};
            urlLoader = new URLClassLoader(urls);
        } catch (Exception e) {
            System.out.println("Initialization of application containing directory class loader failed"
                    + " with the following message:");
            System.out.println(e.getMessage());
        }
    }

    private void readPurchaseTypeProperties(Map<String, String> purchaseTypeProperties) {
        Map<String, String[]> inclusionMap = new HashMap<>();
        purchaseTypeProperties.forEach((key, value) -> {
            if (key.endsWith("include")) {
                String type = key.replace(".include", "");
                String[] typesToInclude = Arrays.stream(value.split(",")).map(String::trim).toArray(String[]::new);
                inclusionMap.put(type, typesToInclude);
            } else if (key.endsWith("keywords")) {
                String type = key.replace(".keywords", "");
                readPurchaseTypeSuggestionsFromFile(type, value);
            }
        });
        includePurchaseTypes(inclusionMap);
    }

    private void readPurchaseTypeSuggestionsFromFile(String type, String path) {
        URL resource = urlLoader.findResource(path);
        if (resource != null) {
            try (Stream<String> lines = Files.lines(Paths.get(resource.getPath()))) {
                List<String> list = lines.collect(Collectors.toList());
                typeSuggestionSets.putIfAbsent(type, new HashSet<>(list));
            } catch (Exception e) {
                System.out.println("Loading of type suggestion file failed with the following message:");
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid type suggestion file path. Can't load file '" + path + "'");
        }

    }

    private void includePurchaseTypes(Map<String, String[]> inclusionMap) {
        inclusionMap.forEach((type, typesToInclude) -> {
            for (String typeToInclude : typesToInclude) {
                Set<String> suggestions = typeSuggestionSets.get(StringUtils.capitalize(typeToInclude));
                if (suggestions != null) {
                    typeSuggestionSets.get(type).addAll(suggestions);
                } else {
                    System.out.println("Can't include type '" + typeToInclude + "' in type '" + type + "'");
                    System.out.println("Type '" + typeToInclude + "' might not have been configured properly");
                }
            }
        });
    }
}
